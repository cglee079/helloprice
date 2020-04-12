package com.podo.helloprice.telegram.job;

import java.time.LocalDateTime;

public interface Worker {

    void doIt(LocalDateTime now1);
}
