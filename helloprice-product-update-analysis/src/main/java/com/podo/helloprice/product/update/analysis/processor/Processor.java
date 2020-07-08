package com.podo.helloprice.product.update.analysis.processor;

import com.podo.helloprice.core.enums.ProductUpdateStatus;

import java.time.LocalDateTime;

public interface Processor {

    void process(Long productId, ProductUpdateStatus updateStatus, LocalDateTime now);

}
