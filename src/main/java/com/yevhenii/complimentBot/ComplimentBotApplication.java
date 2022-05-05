package com.yevhenii.complimentBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class ComplimentBotApplication {

    public static void main(String[] args) {

        ApiContextInitializer.init();

        SpringApplication.run(ComplimentBotApplication.class, args);

    }

}
