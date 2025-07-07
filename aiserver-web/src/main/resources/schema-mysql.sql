DROP TABLE IF EXISTS `SPRING_AI_CHAT_MEMORY`;
CREATE TABLE `SPRING_AI_CHAT_MEMORY`
(
    `conversation_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话唯一标识符',
    `content`         text COLLATE utf8mb4_unicode_ci        NOT NULL COMMENT '消息内容',
    `type`            varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息类型(USER|ASSISTANT|SYSTEM|TOOL)',
    `timestamp`       timestamp                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息创建时间',
    KEY               `SPRING_AI_CHAT_MEMORY_CONVERSATION_ID_TIMESTAMP_IDX` (`conversation_id`,`timestamp`) COMMENT '会话ID和时间戳的复合索引，优化对话历史查询',
    CONSTRAINT `TYPE_CHECK` CHECK ((`type` in (_utf8mb4'USER', _utf8mb4'ASSISTANT', _utf8mb4'SYSTEM', _utf8mb4'TOOL')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Spring AI jdbc 聊天记忆自动存储表';
