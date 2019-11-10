package com.podo.sadream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class SadreamPoolWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SadreamPoolWorkerApplication.class, args);
    }

}
