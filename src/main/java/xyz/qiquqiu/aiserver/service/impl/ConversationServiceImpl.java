package xyz.qiquqiu.aiserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import xyz.qiquqiu.aiserver.common.BaseResult;
import xyz.qiquqiu.aiserver.common.ConversationVO;
import xyz.qiquqiu.aiserver.common.MessageVO;
import xyz.qiquqiu.aiserver.common.SendMessageDTO;
import xyz.qiquqiu.aiserver.context.BaseContext;
import xyz.qiquqiu.aiserver.entity.po.Conversation;
import xyz.qiquqiu.aiserver.entity.po.Message;
import xyz.qiquqiu.aiserver.mapper.ConversationMapper;
import xyz.qiquqiu.aiserver.service.IConversationService;
import xyz.qiquqiu.aiserver.service.IMessageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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
        messageService.saveAndUpdate(dto);

        // TODO 将图片上传到本地服务器或者上传到云服务器保存url

        String prompt = dto.getContent();
        log.debug("用户说：【{}】", prompt);
        return defaultChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, dto.getConversationId()))
                .stream()
                .content();

        // TODO 保存ai消息
    }
}
