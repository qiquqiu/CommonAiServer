package xyz.qiquqiu.aiserver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RenameConversationDTO {
    private String conversationId;
    private String newTitle;
}