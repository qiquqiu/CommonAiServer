package xyz.qiquqiu.aiserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import xyz.qiquqiu.aiserver.common.*;
import xyz.qiquqiu.aiserver.constant.Sender;
import xyz.qiquqiu.aiserver.context.BaseContext;
import xyz.qiquqiu.aiserver.entity.po.Conversation;
import xyz.qiquqiu.aiserver.entity.po.Message;
import xyz.qiquqiu.aiserver.mapper.ConversationMapper;
import xyz.qiquqiu.aiserver.properties.FileUploadProperties;
import xyz.qiquqiu.aiserver.service.IConversationService;
import xyz.qiquqiu.aiserver.service.IMessageService;
import xyz.qiquqiu.aiserver.util.ImageUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户会话表 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2025-06-28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements IConversationService {

    private final IMessageService messageService;
    private final ChatClient defaultChatClient;
    private final ChatClient multiChatClient;
    private final ChatMemory chatMemory;
    private final ChatClient titleClient;
    private final FileUploadProperties fileUploadProperties;

    // 创建新对话
    @Override
    public BaseResult<ConversationVO> newChat() {
        // 1.拿到用户id，并且校验是否存在
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return BaseResult.noLogin();
        }
        log.debug("创建新对话，当前用户id：{}", userId);

        // 2.创建新的对话，返回新会话的基本信息
        String conversationId = UUID.randomUUID().toString();
        ConversationVO conversationVO = new ConversationVO()
                .setConversationId(conversationId)
                .setTitle("新对话" + conversationId.substring(0, 8))
                .setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Conversation conversation = new Conversation()
                .setId(conversationVO.getConversationId())
                .setUserId(userId)
                .setTitle(conversationVO.getTitle());
        this.save(conversation);
        return BaseResult.success(conversationVO);
    }

    // 查询用户对话列表
    @Override
    public BaseResult<List<ConversationVO>> getConversations() {
        // 1.拿到用户id，并且校验是否存在
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return BaseResult.noLogin();
        }
        log.debug("查询对话列表，当前用户id：{}", userId);

        // 2.查询用户对话列表（根据id查询所有会话记录）
        List<Conversation> conversations = this.lambdaQuery()
                .eq(Conversation::getUserId, userId)
                .list();

        // 3.PO集合转VO集合
        List<ConversationVO> voList = conversations.stream()
                .map(c -> new ConversationVO()
                        .setConversationId(c.getId())
                        .setTitle(c.getTitle())
                        .setCreatedAt(c.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                )
                .toList();
        return BaseResult.success(voList);
    }

    // 根据会话id查询对话详情（对话记录列表）
    @Override
    public BaseResult<List<MessageVO>> getConversationMessages(String conversationId) {
        log.debug("查询会话id：{} 的对话详情", conversationId);
        // 1.查询PO集合
        List<Message> msgList = messageService.lambdaQuery()
                .eq(Message::getConversationId, conversationId)
                .orderBy(true, true, Message::getOrder)
                .list();

        // 2.PO集合转VO集合
        List<MessageVO> voList = BeanUtil.copyToList(msgList, MessageVO.class);
        return BaseResult.success(voList);
    }

    // 发送消息
    @Override
    @Transactional
    public Flux<String> sendMessage(SendMessageDTO dto) {
        // 将图片上传到本地（云服务器）
        String base64Image = dto.getImageFile();
        boolean hasImage = StrUtil.isNotBlank(base64Image);
        byte[] imageBytes = null;

        if (hasImage) {
            try {
                ImageUtil.ImageSaveResult result = ImageUtil.saveBase64Image(base64Image,
                        fileUploadProperties.getPath(),
                        fileUploadProperties.getUrlPrefix());
                dto.setImageFile(result.getUrl()); // 更新DTO中的URL
                imageBytes = result.getImageBytes();    // 将解码后的数据保存在局部变量中
            } catch (IOException e) {
                log.error("图片上传失败", e);
                throw new RuntimeException(e);
            }
        }

        // 保存用户消息，新增消息表消息数，统一执行更新（异步线程执行）
        log.debug("【即将保存 SendMessageDTO = {}】", dto);
        // dto.setSender(Sender.USER);
        messageService.saveAndUpdate(dto);

        String prompt = dto.getContent();
        if (hasImage) {
            log.debug("处理多模态消息，prompt={}", prompt);
            return processMultiMedia(dto, imageBytes);
        }
        log.debug("处理纯文本消息，prompt={}", prompt);
        return processText(dto);
    }

    // 处理纯文本的模型调用
    private Flux<String> processText(SendMessageDTO dto) {
        String prompt = dto.getContent();
        return defaultChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, dto.getConversationId()))
                .stream()
                .content();
    }

    // 处理含有多模态（暂时是图片）的模型调用
    private Flux<String> processMultiMedia(SendMessageDTO dto, byte[] imageBytes) {
        // 直接使用传递进来的 imageBytes
        Media media = ImageUtil.convertToMedia(imageBytes);
        if (media == null) {
            // 如果图片处理失败，可以降级为纯文本处理或返回错误
            throw new RuntimeException("图片处理失败");
            // return processText(dto);
        }

        String prompt = StrUtil.isNotBlank(dto.getContent()) ? dto.getContent() : "请描述这张图片";

        return multiChatClient.prompt()
                .user(u -> u.text(prompt).media(media)) // 构建多模态消息
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, dto.getConversationId()))
                .stream()
                .content();
    }

    // 保存上次ai对话记录保存db，并且产生新标题
    @Override
    public BaseResult saveAiResponse(String conversationId, FinalizeDTO dto) {
        if (StrUtil.isBlank(dto.getAiResponse())) {
            return BaseResult.error();
        }

        // 保存db
        log.debug("保存ai的回复到消息表");
        SendMessageDTO msgDTO = new SendMessageDTO()
                .setConversationId(conversationId)
                .setSender(Sender.ASSISTANT)
                .setContent(dto.getAiResponse());

        messageService.saveAndUpdate(msgDTO);

        return BaseResult.success();
    }

    @Override
    public BaseResult<String> generateTitle(String conversationId) {
        // 直接使用ChatMemory获取内存中的所有会话（并非全部，这与大多数app同样的逻辑），而维护的MessageWindow是有限的，所以内存基本没有压力
        List<org.springframework.ai.chat.messages.Message> messageList = chatMemory.get(conversationId);
        String totalPrompt = messageList.stream()
                .map(message -> new SimpleMessage(message.getMessageType(), message.getText()).toString())
                .collect(Collectors.joining(" "));
        log.debug("正在生成对话标题，对话id：{}，对话总prompt：【{}】", conversationId, totalPrompt);
        String title = titleClient.prompt() // 使用单独配置的标题生成ai
                .user(totalPrompt)
                .call() // 无需流式响应
                .content();
        log.debug("AI生成对话：{} 的标题为：【{}】", conversationId, title);
        this.lambdaUpdate()
                .eq(Conversation::getId, conversationId)
                .set(Conversation::getTitle, title)
                .update();
        log.debug("对话：{} 的标题已更新！", conversationId);
        return BaseResult.success(title);
    }
}
