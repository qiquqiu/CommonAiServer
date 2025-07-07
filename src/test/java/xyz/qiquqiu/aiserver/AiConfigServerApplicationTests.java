package xyz.qiquqiu.aiserver;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.qiquqiu.aiserver.common.SimpleMessage;
import xyz.qiquqiu.aiserver.properties.JwtProperties;
import xyz.qiquqiu.aiserver.service.IConversationService;
import xyz.qiquqiu.aiserver.service.impl.ConversationServiceImpl;
import xyz.qiquqiu.aiserver.util.JwtUtil;
import xyz.qiquqiu.aiserver.util.MD5Util;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class AiConfigServerApplicationTests {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private ChatClient titleClient;

    @Test
    void createJWT() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1);
        String token = JwtUtil.createJWT("thisisasecretkeythatismorethan32byteslongandsecure", 7200000, claims);

        System.out.println("token = " + token);
    }

    @Test
    void parseJWT() {
//        String token1 = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjUsImV4cCI6MTc1MTE4MjkyNn0.DimgG7DFV3JTYlHN86txnZehZNn2g91S67xiNrZS2DA";
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjUsImV4cCI6MTc1MTE5MDIyMn0.Z_KLNExNvMNv8G-2Pqyzt4StfPKLOZZLfV87FAGkkW4";
//        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
//        System.out.println("claims = " + claims);
//        String userId = claims.get("userId").toString();
//        System.out.println("userId = " + userId);
    }

    @Test
    void testToString() {
        SimpleMessage sm = new SimpleMessage();
        sm.setType(MessageType.TOOL);
        sm.setContent("你是谁？");

        System.out.println(sm);
    }

    /**
     * 注意，对于标题生成模型的“系统提示词”，一定要严格设定，绝对禁止出现这种情况：
     * 当用户与专门的ai对话内容中出现某些问题的时候，被话题生成模型误以为是发给它的
     * 这就导致了它会直接以第一或者第二人称非客观的回答我们的问题，而非生成提取会话主题！
     */
    @Test
    void testGeneratorTitle() {
        /*String totalPrompt = "[对话](用户说：hello tell a chinese joke!)[对话](AI回复：当然可以！有个笑话是这样的：" +
                "有一天，一只猪去旅游，回来后它的朋友问它：“你去哪儿玩了？”猪回答说：“我去了一趟南京。”朋友惊讶地问：" +
                "“那你是不是变成了烤乳猪啊？”猪笑着说：“哪有这回事，我只是去逛了逛，并没有变成菜！”希望这个笑话能让你开心" +
                "\uD83D\uDE0A)[对话](用户说：more ! good)[对话](AI回复：好的，再来一个中文笑话吧！有个大象，在蚂蚁" +
                "家门口，等了好久才进门。为什么呢？因为蚂蚁家的门太小了，大象要等蚂蚁把门开大一点才能进去。希望这个笑话也能" +
                "让你会心一笑\uD83D\uDE04)[对话](用户说：good joke!)[对话](AI回复：哈哈，看来你是想听个更有趣的笑话呢" +
                "！那我再讲一个吧：两个人在讨论谁的老板更厉害。第一个人说：“我的老板可厉害了，他能让公司里的每个人都紧张到不" +
                "敢喘大气。”第二个人淡定地说：“这算什么，我们老板一进办公室，连电脑都吓得自动开机了！”希望这个笑话能让你笑出" +
                "声来\uD83D\uDE04)";*/
        String totalPrompt = "你觉得1+1应该是几？"; // 生成标题：数学运算1加1的值
        String totalPrompt2 = "你是谁？你是什么模型？"; // 生成标题：数学运算1加1的值

        String title = titleClient.prompt() // 使用单独配置的标题生成ai
                .user(totalPrompt2)
                .call() // 无需流式响应
                .content();

        System.out.println("ai-new-title = " + title);
    }

    @Test
    void testMD5() {
        String pwd = MD5Util.encode("admin");
        System.out.println("pwd = " + pwd);
    }

}
