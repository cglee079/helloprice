package com.podo.helloprice.crawl.scheduler.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @DisplayName("메세지 퍼블리시 시간 갱신")
    @Test
    void testUpdateLastPublishAt(){
        //given
        final LocalDateTime lastPublishAt = LocalDateTime.now();
        final Product product = new Product();

        //when
        product.updateLastPublishAt(lastPublishAt);

        //then
        assertThat(product.getLastPublishAt()).isEqualTo(lastPublishAt);
    }

}
