package com.podo.helloprice.notify.analysis.infra.mq.message;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Setter
public class NotifyEventMessage {
    private Long productId;
    private ProductUpdateStatus updateStatus;
}
