package com.yevhenii.complimentBot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.TelegramBotsApi;

import java.io.File;

@Configuration
public class AppConfig {

    @Bean
    TelegramBotsApi telegramBotsApi(){
        return new TelegramBotsApi();
    }

}
