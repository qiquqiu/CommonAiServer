package xyz.qiquqiu.aiserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xyz.qiquqiu.aiserver.common.SendMessageDTO;
import xyz.qiquqiu.aiserver.entity.po.Conversation;
import xyz.qiquqiu.aiserver.entity.po.Message;
import xyz.qiquqiu.aiserver.mapper.ConversationMapper;
import xyz.qiquqiu.aiserver.mapper.MessageMapper;
import xyz.qiquqiu.aiserver.service.IMessageService;

/**
 * <p>
 * 聊天消息表 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2025-06-28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    private final ConversationMapper conversationMapper;

    @Override
    @Async("customThreadPoolTaskExecutor")
    public void saveAndUpdate(SendMessageDTO dto) {
        log.debug("执行对话相关dml的异步线程：{}", Thread.currentThread().getName());
        // 1.查询会话表消息数量
        Conversation c = conversationMapper.selectById(dto.getConversationId());
        Integer cnt = c.getMessageCount();

        // 2.插入新消息
        Message message = new Message()
                .setConversationId(dto.getConversationId())
                .setOrder(++cnt) // 消息数加一
                .setSender(dto.getSender())
                .setContent(dto.getContent())
                .setImageUrl(dto.getImageFile()); // 非空就说明是已经存成功
        this.save(message);
        log.debug("插入 {} 的消息到消息表：{}", dto.getSender(), message);

        // 3.更新会话表消息数量
        log.debug("更新会话：{} 的消息数量为：{}", c.getId(), cnt);
        c.setMessageCount(cnt);
        conversationMapper.updateById(c);
    }
}
