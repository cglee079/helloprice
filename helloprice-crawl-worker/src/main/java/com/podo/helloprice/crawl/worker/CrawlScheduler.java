package com.podo.helloprice.crawl.worker;

import com.podo.helloprice.crawl.worker.job.Worker;
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
