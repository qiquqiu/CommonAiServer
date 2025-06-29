package xyz.qiquqiu.aiserver.service;

import reactor.core.publisher.Flux;
import xyz.qiquqiu.aiserver.common.*;
import xyz.qiquqiu.aiserver.entity.po.Conversation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户会话表 服务类
 * </p>
 *
 * @author lyh
 * @since 2025-06-28
 */
public interface IConversationService extends IService<Conversation> {

    BaseResult<ConversationVO> newChat();

    BaseResult<List<ConversationVO>> getConversations();

    BaseResult<List<MessageVO>> getConversationMessages(String conversationId);

    Flux<String> sendMessage(SendMessageDTO dto);
}
