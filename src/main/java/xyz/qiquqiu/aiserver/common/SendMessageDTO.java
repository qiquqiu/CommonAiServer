package xyz.qiquqiu.aiserver.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageDTO {
    @NotNull(message = "会话id不能为空")
    private String conversationId;
    private String contentType;
    @NotNull(message = "消息内容不能为空")
    private String content;
    private String imageFile; // base64 string
}