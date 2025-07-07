package xyz.qiquqiu.aiserver.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageVO {
    private String id; // 消息id
    private String conversationId; // 消息所属的对话id
    private String sender; // 发送者，用户或者ai
    private String content;
    private String imageUrl; // 图片url（如果有）
    private String createdAt; // 创建时间
}