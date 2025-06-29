use ai_application;

drop table conversation;

CREATE TABLE `conversation`
(
    `id`         VARCHAR(36) NOT NULL COMMENT '会话ID，主键，使用UUID',
    `user_id`    BIGINT      NOT NULL COMMENT '用户ID，逻辑外键，关联用户表的主键',
    `title`      VARCHAR(255) DEFAULT '新会话' COMMENT '会话标题',
    `created_at` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`) COMMENT '为user_id创建索引以优化查询'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户会话表';

ALTER TABLE `conversation`
    ADD COLUMN `message_count` INT NOT NULL DEFAULT 0 COMMENT '当前会话的消息数量';

