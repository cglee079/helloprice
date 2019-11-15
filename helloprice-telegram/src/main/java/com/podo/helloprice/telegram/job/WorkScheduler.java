package com.podo.helloprice.telegram.job;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class WorkScheduler {

    private final List<Worker> workers;

    @Scheduled(cron = "*/30 * * * * *")
    public void schedule() {
        for (Worker worker : workers) {
            worker.doIt();
        }
    }
}
