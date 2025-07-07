package xyz.qiquqiu.aiserver.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 花卉图库
 * </p>
 *
 * @author lyh
 * @since 2025-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("gallery")
public class Gallery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图片唯一标识，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图片静态资源路径，以/开头
     */
    private String imageUrl;

    /**
     * 花卉名称
     */
    private String name;

    /**
     * 花卉描述
     */
    private String description;

    /**
     * 点赞数，默认为0
     */
    private Integer star;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;


}
