package xyz.qiquqiu.aiserver.service;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import xyz.qiquqiu.aiserver.common.*;
import xyz.qiquqiu.aiserver.common.dto.FinalizeDTO;
import xyz.qiquqiu.aiserver.common.dto.RenameConversationDTO;
import xyz.qiquqiu.aiserver.common.dto.SendMessageDTO;
import xyz.qiquqiu.aiserver.common.po.Conversation;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.qiquqiu.aiserver.common.vo.ConversationVO;
import xyz.qiquqiu.aiserver.common.vo.MessageVO;

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

    BaseResult saveAiResponse(String conversationId, FinalizeDTO dto);

    BaseResult<String> generateTitle(String conversationId);

    BaseResult<Void> renameConversation(@Valid RenameConversationDTO dto);

    BaseResult<Void> deleteConversation(List<String> ids);
}
