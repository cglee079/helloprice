package com.podo.helloprice.crawl.agent.domain.product;

import com.podo.helloprice.core.enums.ProductAliveStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Product 단위 테스트")
class ProductTest {

    @DisplayName("DEAD_COUNT 증가")
    @Test
    void testIncreaseDeadCount(){
        //given
        final Product product = new Product();
        ReflectionTestUtils.setField(product, "deadCount", 0);

        //when
        product.increaseDeadCount(100);

        //then
        assertThat(product.getDeadCount()).isEqualTo(1);
    }

    @DisplayName("DEAD_COUNT 초과")
    @Test
    void testIncreaseDeadCountOverMax(){
        //given
        final Product product = new Product();
        ReflectionTestUtils.setField(product, "deadCount", 0);

        //when
        product.increaseDeadCount(0);

        //then
        assertThat(product.getDeadCount()).isEqualTo(1);
        assertThat(product.getAliveStatus()).isEqualTo(ProductAliveStatus.DEAD);
    }

}
