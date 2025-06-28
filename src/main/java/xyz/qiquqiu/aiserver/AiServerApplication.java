package xyz.qiquqiu.aiserver;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan("xyz.qiquqiu.aiserver.mapper")
public class AiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiServerApplication.class, args);
        log.debug(">>>>>>>>>>>>>> 服务启动成功！");
    }

}
