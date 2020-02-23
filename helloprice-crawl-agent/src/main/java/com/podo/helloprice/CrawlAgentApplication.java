package com.podo.helloprice;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication
public class CrawlAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlAgentApplication.class, args);
    }

}
