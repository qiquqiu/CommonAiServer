use ai_application;

drop table message;

create table message
(
    id              bigint auto_increment                 not null comment '消息唯一标识' primary key,
    conversation_id varchar(36)                           not null comment '消息所属的对话ID，关联到conversations表',
    `order`         int                                   not null comment '消息在会话中的排序',
    sender          enum ('USER', 'ASSISTANT')            not null comment '发送者身份',
    content         text                                  null comment '消息内容，文本类型',
    image_url       varchar(255)                          null comment '图片URL，如果消息含有image',
    created_at      timestamp   default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at      timestamp   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_conversation_order unique (conversation_id, `order`) -- 联合唯一约束
)
    comment '聊天消息表';

create index idx_conversation_id on message (conversation_id);