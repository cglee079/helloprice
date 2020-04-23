package com.podo.helloprice.crawl.agent.job;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ProductUpdate {
    private final Long productId;
    private final ProductUpdateStatus updateStatus;
}
