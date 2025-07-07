package xyz.qiquqiu.aiserver.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("file.gallery")
public class GalleryProperties {
    private String path; // 本地路径
    private String urlPrefix; // 映射的访问路径前缀
}
