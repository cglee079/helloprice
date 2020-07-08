package com.podo.helloprice.product.update.analysis.infra.mq.consumer;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.infra.mq.message.ProductUpdateMessage;
import com.podo.helloprice.product.update.analysis.processor.Processor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductUpdateConsumer implements Consumer<ProductUpdateMessage> {

    private final List<Processor> processors;

    @Override
    public void accept(ProductUpdateMessage productUpdateMessage) {
        log.debug("MQ :: CONSUME :: payload : {}", productUpdateMessage);

        final LocalDateTime now = LocalDateTime.now();
        final Long productId = productUpdateMessage.getProductId();
        final ProductUpdateStatus updateStatus = productUpdateMessage.getUpdateStatus();

        for (Processor processor : processors) {
            processor.process(productId, updateStatus, now);
        }
    }
}
