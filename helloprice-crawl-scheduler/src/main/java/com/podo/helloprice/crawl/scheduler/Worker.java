package com.podo.helloprice.crawl.scheduler;

import java.time.LocalDateTime;

public interface Worker {

    void run(LocalDateTime now);
}
