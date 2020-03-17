package com.podo.helloprice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
public class HellopriceTelegramApplication {

    public static void main(String[] args) {
        //Telegram Api Initial
        ApiContextInitializer.init();

        SpringApplication.run(HellopriceTelegramApplication.class, args);
    }

}
