package com.podo.helloprice.crawl.agent.domain.product;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.crawl.agent.domain.productsale.ProductSale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Product Price 단위 테스트")
class ProductSaleTest {

    @DisplayName("가격, 추가 정보 변동 없을 때")
    @Test
    void testUpdate01() {
        //given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime beforeUpdateAt = now.minusMinutes(1);
        final ProductSale productSale = ProductSale.create(SaleType.NORMAL, 1000, "", beforeUpdateAt);

        //when, then
        assertThat(productSale.update(1000, "", now)).isFalse();
        assertThat(productSale.getLastUpdateAt()).isEqualTo(beforeUpdateAt);
    }

    @DisplayName("가격 변동 있을 때")
    @Test
    void testUpdate02() {
        //given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime beforeUpdateAt = now.minusMinutes(1);
        final ProductSale productSale = ProductSale.create(SaleType.NORMAL, 1000, "", beforeUpdateAt);

        //when, then
        assertThat(productSale.update(1, "", now)).isTrue();
        assertThat(productSale.getLastUpdateAt()).isEqualTo(now);
    }

    @DisplayName("추가정보 변동 있을 때")
    @Test
    void testUpdate03() {
        //given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime beforeUpdateAt = now.minusMinutes(1);
        final ProductSale productSale = ProductSale.create(SaleType.NORMAL, 1000, "", beforeUpdateAt);

        //when, then
        assertThat(productSale.update(1000, "삼성", now)).isTrue();
        assertThat(productSale.getLastUpdateAt()).isEqualTo(now);
    }
}
