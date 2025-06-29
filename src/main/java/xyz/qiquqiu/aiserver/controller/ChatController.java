package xyz.qiquqiu.aiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import xyz.qiquqiu.aiserver.common.*;
import xyz.qiquqiu.aiserver.service.IConversationService;

import javax.validation.Valid;
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

    @GetMapping("/conversations/{conversationId}/messages")
    public BaseResult<List<MessageVO>> getConversationMessages(@PathVariable String conversationId) {
        if (!StringUtils.hasLength(conversationId)) {
            return BaseResult.error();
        }
        return chatService.getConversationMessages(conversationId);
    }

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> sendMessage(@RequestBody @Valid SendMessageDTO dto) {
        return chatService.sendMessage(dto);
    }
}
