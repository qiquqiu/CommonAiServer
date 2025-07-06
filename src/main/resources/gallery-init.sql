CREATE TABLE `gallery`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '图片唯一标识，自增主键',
    `image_url`   VARCHAR(255) NOT NULL COMMENT '图片静态资源路径，以/开头',
    `name`        VARCHAR(100) NOT NULL COMMENT '花卉名称',
    `description` TEXT COMMENT '花卉描述',
    `star`        INT          NOT NULL DEFAULT 0 COMMENT '点赞数，默认为0',
    `created_at`  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX         `idx_name` (`name`) -- 为名称字段添加索引，方便查询
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='花卉图库';
