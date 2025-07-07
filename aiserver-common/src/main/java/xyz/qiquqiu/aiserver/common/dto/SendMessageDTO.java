package xyz.qiquqiu.aiserver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageDTO {
    @NotNull(message = "会话id不能为空")
    private String conversationId;
    @NotNull(message = "消息内容不能为空")
    private String content;
    private String imageFile; // base64 string
    private String sender;
}