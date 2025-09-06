package xyz.qiquqiu.aiserver;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication
@EnableAsync
@MapperScan("xyz.qiquqiu.aiserver.mapper")
public class AiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiServerApplication.class, args);
        System.out.println("  _____ _                         _    ___ \n" +
                " |  ___| | _____      _____ _ __ / \\  |_ _|\n" +
                " | |_  | |/ _ \\ \\ /\\ / / _ \\ '__/ _ \\  | | \n" +
                " |  _| | | (_) \\ V  V /  __/ | / ___ \\ | | \n" +
                " |_|   |_|\\___/ \\_/\\_/ \\___|_|/_/   \\_\\___|  v1.0.0");
        log.debug(">>>>>>>>>>>>>> 服务启动成功！");
    }

}
