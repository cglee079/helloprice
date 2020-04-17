package com.podo.helloprice.crawl.scheduler.job;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductCrawlScheduler {

    private final List<Worker> workers;

    @Scheduled(cron = "*/10 * * * * *")
    public void schedule() {
        LocalDateTime now = LocalDateTime.now();

        for (Worker worker : workers) {
            worker.run(now);
        }
    }

}

