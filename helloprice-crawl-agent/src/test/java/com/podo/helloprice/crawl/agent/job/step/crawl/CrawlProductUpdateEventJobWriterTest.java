package com.podo.helloprice.crawl.agent.job.step.crawl;

import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.crawl.agent.domain.product.Product;
import com.podo.helloprice.crawl.agent.domain.product.ProductRepository;
import com.podo.helloprice.crawl.agent.job.CrawlProductJobParameter;
import com.podo.helloprice.crawl.agent.job.ProductUpdateEventStore;
import com.podo.helloprice.crawl.agent.job.ProductToCrawl;
import com.podo.helloprice.crawl.agent.job.ProductUpdateEvent;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("JOB :: CRAWL :: CrawlProductUpdateJobWriter 단위 테스트")
class CrawlProductUpdateEventJobWriterTest {

    @DisplayName("크롤 실패 했을 때, MAX_DEAD 카운트 초과")
    @Test
    void testFailCrawl(){
        //given - mock product
        final Product product = Mockito.mock(Product.class);
        given(product.increaseDeadCount(any(), any())).willReturn(true);
        given(product.getAliveStatus()).willReturn(ProductAliveStatus.DEAD); // mock
        given(product.getSaleStatus()).willReturn(ProductSaleStatus.DISCONTINUE); // mock

        //given - mock - parameter
        final CrawlProductJobParameter crawlProductJobParameter = Mockito.mock(CrawlProductJobParameter.class);
        given(crawlProductJobParameter.getProductToCrawl()).willReturn(new ProductToCrawl("123", "abc"));

        //given - mock repository
        final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        given(productRepository.findByProductCode(any())).willReturn(product);

        //given -
        final ProductUpdateEventStore productUpdateEventStore = new ProductUpdateEventStore();

        //when
        final CrawlProductUpdateJobWriter crawlProductUpdateJobWriter = new CrawlProductUpdateJobWriter(crawlProductJobParameter, productRepository, productUpdateEventStore);
        crawlProductUpdateJobWriter.write(Collections.singletonList(CrawledProduct.FAIL));

        //then
        then(product).should(times(1)).increaseDeadCount(any(), any());
        assertThat(productUpdateEventStore.getProductUpdateEvents().size()).isEqualTo(1);
        assertThat(productUpdateEventStore.getProductUpdateEvents().get(0))
                .extracting(ProductUpdateEvent::getUpdateStatus)
                .isEqualTo(ProductUpdateStatus.UPDATE_DEAD);
    }

}
