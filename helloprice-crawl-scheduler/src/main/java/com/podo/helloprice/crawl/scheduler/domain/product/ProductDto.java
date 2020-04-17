package com.podo.helloprice.crawl.scheduler.domain.product;

import com.podo.helloprice.core.model.ProductAliveStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductDto {
    private String productName;
    private String productCode;
}
