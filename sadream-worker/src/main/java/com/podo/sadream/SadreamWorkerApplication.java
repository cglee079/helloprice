package com.podo.sadream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class SadreamWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SadreamWorkerApplication.class, args);
    }

}
