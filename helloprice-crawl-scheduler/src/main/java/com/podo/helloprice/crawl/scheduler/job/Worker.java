package com.podo.helloprice.crawl.scheduler.job;

import java.time.LocalDateTime;

public interface Worker {

    void run(LocalDateTime now);
}
