package com.podo.helloprice.crawlworker;

import com.podo.helloprice.crawlworker.job.Worker;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CrawlScheduler {

    private final List<Worker> workers;

    @Scheduled(cron = "*/5 * * * * *")
    public void schedule() {
        for (Worker worker : workers) {
            worker.run();
        }
    }

}
