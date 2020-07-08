package com.podo.helloprice.product.update.analysis.processor.log;


import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.updatelog.ProductUpdateLogInsertService;
import com.podo.helloprice.product.update.analysis.processor.Processor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductUpdateLogProcessor implements Processor {

    private final ProductUpdateLogInsertService productUpdateLogInsertService;

    @Override
    public void process(Long productId, ProductUpdateStatus updateStatus, LocalDateTime now) {
        productUpdateLogInsertService.insertNew(productId, updateStatus, now);
    }
}
