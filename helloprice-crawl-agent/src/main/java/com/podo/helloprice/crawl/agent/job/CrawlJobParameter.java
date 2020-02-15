package com.podo.helloprice.crawl.agent.job;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.podo.helloprice.crawl.core.vo.LastCrawledItem;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Getter
public class CrawlJobParameter {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private LocalDateTime createAt;
    private LastCrawledItem lastCrawledItem;

    @Value("#{jobParameters[createAt]}")
    public void setCreateAt(Date createAt) {
        this.createAt = LocalDateTime.ofInstant(createAt.toInstant(), ZoneId.systemDefault());
    }

    @Value("#{jobParameters[lastCrawledItem]}")
    public void setLastCrawledItem(String lastCrawledItem) {
        try {
            this.lastCrawledItem = OBJECT_MAPPER.readValue(lastCrawledItem, LastCrawledItem.class);
        } catch (JsonProcessingException e) {
            log.error("Object Mapper Fail, {}", lastCrawledItem, e);
        }
    }

}
