package xyz.qiquqiu.aiserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.qiquqiu.aiserver.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class AiConfigServerApplicationTests {

    @Test
    void contextLoads() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1);
        String token = JwtUtil.createJWT("thisisasecretkeythatismorethan32byteslongandsecure", 7200000, claims);

        System.out.println("token = " + token);
    }

}
