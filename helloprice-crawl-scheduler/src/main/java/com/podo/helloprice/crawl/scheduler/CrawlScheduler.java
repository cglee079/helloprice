package com.podo.helloprice.crawl.scheduler;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CrawlScheduler {

    private final List<Worker> workers;

    @Scheduled(cron = "*/10 * * * * *")
    public void schedule() {
        for (Worker worker : workers) {
            worker.run();
        }
    }

}

