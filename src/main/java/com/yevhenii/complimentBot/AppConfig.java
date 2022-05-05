package com.yevhenii.complimentBot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.TelegramBotsApi;

import java.io.File;

@Configuration
public class AppConfig {

    @Value("${complimentFile}")
    private String complimentFilePath;

    @Bean
    TelegramBotsApi telegramBotsApi(){
        return new TelegramBotsApi();
    }

    @Bean
    File complimentFile(){
        File file = new File(complimentFilePath);
        System.out.println(file.getAbsolutePath());
        return file;
    }
}
