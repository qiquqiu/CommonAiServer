package xyz.qiquqiu.aiserver.common.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 聊天消息表
 * </p>
 *
 * @author lyh
 * @since 2025-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息唯一标识，使用UUID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息所属的对话ID，关联到conversation表
     */
    private String conversationId;

    /**
     * 消息在对话中的顺序
     */
    @TableField("`order`")
    private Integer order;

    /**
     * 发送者，USER表示用户，ASSISTANT表示AI
     */
    private String sender;

    /**
     * 消息内容，文本类型
     */
    private String content;

    /**
     * 图片URL，如果消息含有image
     */
    private String imageUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

}
