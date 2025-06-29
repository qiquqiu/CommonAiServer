USE ai_application;

drop table if exists SPRING_AI_CHAT_MEMORY;

-- 创建聊天记忆表（如果不存在）
CREATE TABLE IF NOT EXISTS SPRING_AI_CHAT_MEMORY
(
    -- 会话ID，用于关联同一对话中的所有消息
    conversation_id VARCHAR(36) NOT NULL COMMENT '会话唯一标识符',
    -- 消息内容，存储对话的实际文本内容
    content         TEXT        NOT NULL COMMENT '消息内容',
    -- 消息类型，限定为特定值
    type            VARCHAR(10) NOT NULL COMMENT '消息类型(USER|ASSISTANT|SYSTEM|TOOL)',
    -- 消息时间戳，自动记录创建时间
    `timestamp`     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息创建时间',
    -- 检查约束，确保type字段只能是预定义的值
    CONSTRAINT TYPE_CHECK CHECK (type IN ('USER', 'ASSISTANT', 'SYSTEM', 'TOOL'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Spring AI jdbc 聊天记忆自动存储表';

-- 创建复合索引（如果不存在），优化按会话ID和时间戳的查询性能
CREATE INDEX SPRING_AI_CHAT_MEMORY_CONVERSATION_ID_TIMESTAMP_IDX
    ON SPRING_AI_CHAT_MEMORY (conversation_id, `timestamp`)
    COMMENT '会话ID和时间戳的复合索引，优化对话历史查询';
