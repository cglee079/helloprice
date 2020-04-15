package com.podo.helloprice.crawl.agent.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static com.podo.helloprice.code.model.ProductAliveStatus.DEAD;
import static com.podo.helloprice.code.model.PriceUpdateStatus.UPDATED;
import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @DisplayName("DEAD_COUNT 증가")
    @Test
    void testIncreaseDeadCount(){
        //given
        final Product product = new Product();
        ReflectionTestUtils.setField(product, "deadCount", 0);
        final LocalDateTime lastCrawledAt = LocalDateTime.now();

        //when
        product.increaseDeadCount(100, lastCrawledAt);

        //then
        assertThat(product.getDeadCount()).isEqualTo(1);
    }

    @DisplayName("DEAD_COUNT 초과")
    @Test
    void testIncreaseDeadCountOverMax(){
        //given
        final int productPrice = 1000;
        final Product product = new Product();
        ReflectionTestUtils.setField(product, "deadCount", 0);
        ReflectionTestUtils.setField(product, "price", productPrice);
        final LocalDateTime lastCrawledAt = LocalDateTime.now();

        //when
        product.increaseDeadCount(0, lastCrawledAt);

        //then
        assertThat(product.getDeadCount()).isEqualTo(1);
        assertThat(product.getProductPrice()).isEqualTo(0);
        assertThat(product.getBeforePrice()).isEqualTo(productPrice);
        assertThat(product.getAliveStatus()).isEqualTo(DEAD);
        assertThat(product.getUpdateStatus()).isEqualTo(UPDATED);
        assertThat(product.getLastUpdatedAt()).isEqualTo(lastCrawledAt);
    }

    

}