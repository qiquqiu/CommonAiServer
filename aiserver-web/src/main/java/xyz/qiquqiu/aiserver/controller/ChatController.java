package xyz.qiquqiu.aiserver.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import xyz.qiquqiu.aiserver.common.*;
import xyz.qiquqiu.aiserver.common.dto.FinalizeDTO;
import xyz.qiquqiu.aiserver.common.dto.SendMessageDTO;
import xyz.qiquqiu.aiserver.common.vo.ConversationVO;
import xyz.qiquqiu.aiserver.common.vo.MessageVO;
import xyz.qiquqiu.aiserver.service.IConversationService;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final IConversationService chatService;

    // 创建新对话
    @PostMapping("/conversations")
    public BaseResult<ConversationVO> newChat() {
        return chatService.newChat();
    }

    // 根据用户id获取对话列表
    @GetMapping("/conversations")
    public BaseResult<List<ConversationVO>> getConversations() {
        return chatService.getConversations();
    }

    // 根据对话id获取对话消息
    @GetMapping("/conversations/{conversationId}/messages")
    public BaseResult<List<MessageVO>> getConversationMessages(@PathVariable String conversationId) {
        if (StrUtil.isBlank(conversationId)) {
            return BaseResult.error();
        }
        return chatService.getConversationMessages(conversationId);
    }

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> sendMessage(@RequestBody @Valid SendMessageDTO dto) {
        return chatService.sendMessage(dto);
    }

    // 保存AI回复
    @PostMapping("/conversations/{conversationId}/save")
    public BaseResult saveAiResponse(@PathVariable String conversationId,
                                     @RequestBody FinalizeDTO dto
    ) {
        if (StrUtil.isBlank(conversationId)) {
            return BaseResult.error();
        }
        return chatService.saveAiResponse(conversationId, dto);
    }

    // 请求生成会话标题
    @GetMapping("/conversations/{conversationId}/title")
    public BaseResult<String> generateTitle(@PathVariable String conversationId) {
        if (StrUtil.isBlank(conversationId)) {
            return BaseResult.error();
        }
        try {
            return chatService.generateTitle(conversationId);
        } catch (Exception e) {
            log.error("生成标题失败，尝试重试...", e);
            return chatService.generateTitle(conversationId);
        }
    }
}
