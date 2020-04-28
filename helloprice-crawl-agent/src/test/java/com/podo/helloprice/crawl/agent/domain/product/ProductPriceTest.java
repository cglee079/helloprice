package com.podo.helloprice.crawl.agent.domain.product;

import com.podo.helloprice.core.enums.PriceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Product Price 단위 테스트")
class ProductPriceTest {

    @DisplayName("가격, 추가 정보 변동 없을 때")
    @Test
    void testUpdate01() {
        //given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime beforeUpdateAt = now.minusMinutes(1);
        final ProductPrice productPrice = ProductPrice.create(PriceType.NORMAL, 1000, "", beforeUpdateAt);

        //when, then
        assertThat(productPrice.update(1000, "", now)).isFalse();
        assertThat(productPrice.getLastUpdateAt()).isEqualTo(beforeUpdateAt);
    }

    @DisplayName("가격 변동 있을 때")
    @Test
    void testUpdate02() {
        //given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime beforeUpdateAt = now.minusMinutes(1);
        final ProductPrice productPrice = ProductPrice.create(PriceType.NORMAL, 1000, "", beforeUpdateAt);

        //when, then
        assertThat(productPrice.update(1, "", now)).isTrue();
        assertThat(productPrice.getLastUpdateAt()).isEqualTo(now);
    }

    @DisplayName("추가정보 변동 있을 때")
    @Test
    void testUpdate03() {
        //given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime beforeUpdateAt = now.minusMinutes(1);
        final ProductPrice productPrice = ProductPrice.create(PriceType.NORMAL, 1000, "", beforeUpdateAt);

        //when, then
        assertThat(productPrice.update(1000, "삼성", now)).isTrue();
        assertThat(productPrice.getLastUpdateAt()).isEqualTo(now);
    }
}
