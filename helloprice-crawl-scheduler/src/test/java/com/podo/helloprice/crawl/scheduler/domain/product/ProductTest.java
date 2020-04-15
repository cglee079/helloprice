package com.podo.helloprice.crawl.scheduler.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @DisplayName("메세지 퍼블리시 시간 갱신")
    @Test
    void testUpdateLastPublishAt(){
        //given
        final LocalDateTime lastPublishAt = LocalDateTime.now();
        final Product item = new Product();

        //when
        item.updateLastPublishAt(lastPublishAt);

        //then
        assertThat(item.getLastPublishAt()).isEqualTo(lastPublishAt);
    }

}