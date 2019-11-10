package com.podo.helloprice.poolworker.job;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
public class PoolerJobParameter {

    private LocalDateTime createAt;

    @Value("#{jobParameters[createAt]}")
    public void setCreateAt(Date createAt) {
        this.createAt = LocalDateTime.ofInstant(createAt.toInstant(), ZoneId.systemDefault());
    }
}
