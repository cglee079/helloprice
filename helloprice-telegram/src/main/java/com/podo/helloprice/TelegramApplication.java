package com.podo.helloprice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class TelegramApplication {

    public static void main(String[] args) {
        //Telegram Api Initial
        ApiContextInitializer.init();

        SpringApplication.run(TelegramApplication.class, args);
    }

}
