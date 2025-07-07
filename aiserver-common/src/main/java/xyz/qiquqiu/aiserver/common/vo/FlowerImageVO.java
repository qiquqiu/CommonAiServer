package xyz.qiquqiu.aiserver.common.vo;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.qiquqiu.aiserver.common.po.Gallery;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowerImageVO {
    private String id;

    // 图片的url（静态资源路径），以/开头
    private String imageUrl;

    private String name;

    private String description;

    public static FlowerImageVO of(Gallery po) {
        return BeanUtil.copyProperties(po, FlowerImageVO.class);
    }
}