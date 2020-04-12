package com.podo.helloprice.crawl.agent.job;


import com.podo.helloprice.core.util.DateTimeUtil;
import com.podo.helloprice.core.util.JsonUtil;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.LastPublishedProduct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Getter
public class CrawlProductJobParameter {

    private LocalDateTime createAt;
    private LastPublishedProduct lastPublishedProduct;

    @Value("#{jobParameters[createAt]}")
    public void setCreateAt(Date createAt) {
        this.createAt = DateTimeUtil.dateToLocalDateTime(createAt);
    }

    @Value("#{jobParameters[lastPublishedProduct]}")
    public void setLastPublishedProduct(String lastPublishedProduct) {
        this.lastPublishedProduct = JsonUtil.toObject(lastPublishedProduct, LastPublishedProduct.class);
    }

}
