package com.podo.itemwatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

import java.util.TimeZone;

@SpringBootApplication
public class ItemwatcherTelegramApplication {

    public ItemwatcherTelegramApplication() {
        //Telegram Api Initial
        ApiContextInitializer.init();


        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ItemwatcherTelegramApplication.class, args);
    }

}
