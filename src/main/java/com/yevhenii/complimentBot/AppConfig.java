package com.yevhenii.complimentBot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.TelegramBotsApi;

@Configuration
public class AppConfig {

    @Bean
    TelegramBotsApi telegramBotsApi(){
        return new TelegramBotsApi();
    }

}
