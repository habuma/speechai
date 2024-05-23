package habuma.speechai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpeechAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpeechAiApplication.class, args);
    }

    @Bean
    ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

}
