package epice.collectif;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Created by anthonyrey on 30/11/2016.
 */
@Configuration
public class ChatBotConfig  {

    @Value("${bot-token}")
    private String botToken;

    @Value("${build-url}")
    private String buildUrl;

    @Bean
    public Chatbot initBot(){
        Chatbot bot = new Chatbot(buildUrl);

        try {
            bot.connect(botToken);
            bot.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bot;
    }
}
