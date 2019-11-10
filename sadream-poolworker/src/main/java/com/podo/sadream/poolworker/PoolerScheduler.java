package com.podo.sadream.poolworker;

import com.podo.sadream.poolworker.job.Worker;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PoolerScheduler {

    private final List<Worker> workers;

    @Scheduled(cron = "*/10 * * * * *")
    public void schedule() {
        for (Worker worker : workers) {
            worker.run();
        }
    }

}
