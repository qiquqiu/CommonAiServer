package xyz.qiquqiu.aiserver.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户会话表
 * </p>
 *
 * @author lyh
 * @since 2025-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("conversation") // 指定表名
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话ID，主键，使用UUID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 用户ID，逻辑外键，关联用户表的主键
     */
    private Long userId;

    /**
     * 会话标题
     */
    private String title;

    /**
     * 会话下消息数量
     */
    private Integer messageCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;


}
