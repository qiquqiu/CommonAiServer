package xyz.qiquqiu.aiserver.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowerImageVO {
    private String id;

    // 图片的url（静态资源路径），以/开头
    private String imageUrl;

    private String name;

    private String description;
}