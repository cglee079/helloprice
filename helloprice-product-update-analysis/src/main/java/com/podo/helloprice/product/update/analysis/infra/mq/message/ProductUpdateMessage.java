package com.podo.helloprice.product.update.analysis.infra.mq.message;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Setter
public class ProductUpdateMessage {
    private Long productId;
    private ProductUpdateStatus updateStatus;
}
