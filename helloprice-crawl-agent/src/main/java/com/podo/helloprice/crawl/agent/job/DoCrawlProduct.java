package com.podo.helloprice.crawl.agent.job;

import com.podo.helloprice.crawl.agent.global.infra.mq.message.CrawlProductMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DoCrawlProduct {

    private String productCode;
    private String productName;
}
