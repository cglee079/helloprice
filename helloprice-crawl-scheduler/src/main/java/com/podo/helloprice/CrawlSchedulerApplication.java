package com.podo.helloprice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CrawlSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlSchedulerApplication.class, args);
    }

}
