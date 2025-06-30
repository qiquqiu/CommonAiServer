package xyz.qiquqiu.aiserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.qiquqiu.aiserver.constant.SystemPrompt;

@Slf4j
@Configuration
public class AiConfig {

    /**
     * 普通对话聊天功能的模型
     * @param model 百炼的qwen-max模型，需要在yaml中做必要配置
     * @param chatMemory 聊天记忆支持
     */
    @Bean
    public ChatClient defaultChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        log.debug("初始化 DefaultChatClient...");
        return ChatClient
                .builder(model)
                .defaultSystem(SystemPrompt.DEFAULT_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    @Bean
    public ChatClient multiChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        log.debug("初始化 MultiChatClient...");
        return ChatClient
                .builder(model)
                .defaultOptions(ChatOptions.builder().model("qwen-omni-turbo-latest").build())
                .defaultSystem(SystemPrompt.DEFAULT_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    // 单次使用的标题生成模型，无需配置聊天记忆
    @Bean
    public ChatClient titleClient(OpenAiChatModel model) {
        log.debug("初始化 TitleClient...");
        return ChatClient
                .builder(model)
                .defaultOptions(ChatOptions.builder().model("qwen-turbo-latest").build())
                .defaultSystem(SystemPrompt.TITLE_GENERATOR_SYSTEM_PROMPT)
                .build();
    }

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        log.debug("初始化 ChatMemory...");
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(20) // 最大消息数
                .build();
    }
}
