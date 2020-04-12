package com.podo.helloprice.crawl.scheduler.domain.product;

import com.podo.helloprice.code.model.ProductAliveStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String productCode;

    private String productName;

    private LocalDateTime lastPublishAt;

    @Enumerated(EnumType.STRING)
    private ProductAliveStatus aliveStatus;

    public void updateLastPublishAt(LocalDateTime lastPublishAt){
        this.lastPublishAt = lastPublishAt;
    }

}
