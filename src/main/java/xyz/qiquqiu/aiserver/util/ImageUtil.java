package xyz.qiquqiu.aiserver.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Slf4j
public class ImageUtil {

    @Data
    @AllArgsConstructor
    public static class ImageSaveResult {
        private String url;
        private byte[] imageBytes;
    }

    /**
     * 将Base64编码的图片字符串保存到本地，并返回可访问的URL
     * @param imageBase64 Base64编码的图片字符串
     * @return 可通过网络访问的图片URL
     * @throws IOException 文件写入异常
     */
    public static ImageSaveResult saveBase64Image(String imageBase64, String uploadPath, String urlPrefix) throws IOException {
        // log.debug("原始base64图片字符串: {}", imageBase64);

        // 1.解码
        // Base64字符串可能包含MIME头，如 "data:image/jpeg;base64,"，需要去掉
        if (imageBase64.contains(",")) {
            imageBase64 = imageBase64.split(",")[1];
        }
        byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

        // 2.生成唯一文件名
        // 这里我们简单地默认为.jpg，因为Base64本身不包含文件类型信息
        String fileName = UUID.randomUUID() + ".jpg";

        // 3,4.确定存储路径并保存文件
        // 使用 Paths.get() 来创建路径
        Path directoryPath = Paths.get(uploadPath);

        // 确保目录存在，如果不存在则创建
        if (!Files.exists(directoryPath)) {
            log.debug("资源上传目录不存在，创建目录: {}", directoryPath);
            Files.createDirectories(directoryPath);
        }

        Path filePath = directoryPath.resolve(fileName);

        // 将解码图片后的字节数组写入文件
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            log.debug("正在写入文件: {}", filePath);
            fos.write(imageBytes);
        }

        // 5.生成可访问的URL
        // 例如：/resources/images/ + 123e4567-e89b-12d3-a456-426614174000.jpg
        String url = urlPrefix + fileName;
        return new ImageSaveResult(url, imageBytes);
    }

    public static Media convertToMedia(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        // 假设MIME类型，或者从 ImageSaveResult 中传递过来
        return new Media(MediaType.IMAGE_JPEG, resource);
    }
}
