package xyz.qiquqiu.aiserver.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ConversationVO {
    private String conversationId; // 会话id，后端生成返回
    private String title; // 会话标题
    private String createdAt; // 会话创建时间（可选）
}