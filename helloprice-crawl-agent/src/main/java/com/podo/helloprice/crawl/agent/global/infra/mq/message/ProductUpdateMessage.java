package com.podo.helloprice.crawl.agent.global.infra.mq.message;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ProductUpdateMessage {

    private Long productId;
    private ProductUpdateStatus updateStatus;

    public ProductUpdateMessage(Long productId, ProductUpdateStatus updateStatus) {
        this.productId = productId;
        this.updateStatus = updateStatus;
    }
}
