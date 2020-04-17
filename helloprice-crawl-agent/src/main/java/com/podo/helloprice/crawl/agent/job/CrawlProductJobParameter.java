package com.podo.helloprice.crawl.agent.job;


import com.podo.helloprice.core.util.DateTimeUtil;
import com.podo.helloprice.core.util.JsonUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Getter
public class CrawlProductJobParameter {

    private LocalDateTime createAt;
    private DoCrawlProduct doCrawlProduct;

    @Value("#{jobParameters[createAt]}")
    public void setCreateAt(Date createAt) {
        this.createAt = DateTimeUtil.dateToLocalDateTime(createAt);
    }

    @Value("#{jobParameters[doCrawlProduct]}")
    public void setDoCrawlProduct(String doCrawlProduct) {
        this.doCrawlProduct = JsonUtil.toObject(doCrawlProduct, DoCrawlProduct.class);
    }

}
