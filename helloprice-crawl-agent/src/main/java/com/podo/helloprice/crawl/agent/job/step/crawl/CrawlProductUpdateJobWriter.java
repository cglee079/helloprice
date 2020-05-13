package com.podo.helloprice.crawl.agent.job.step.crawl;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.parser.ProductSaleStatusParser;
import com.podo.helloprice.crawl.agent.domain.product.exception.InvalidProductIdException;
import com.podo.helloprice.crawl.agent.domain.product.Product;
import com.podo.helloprice.crawl.agent.domain.product.ProductRepository;
import com.podo.helloprice.crawl.agent.domain.productsale.ProductSale;
import com.podo.helloprice.crawl.agent.domain.productsale.ProductSaleRepository;
import com.podo.helloprice.crawl.agent.job.CrawlProductJobParameter;
import com.podo.helloprice.crawl.agent.job.ProductToCrawl;
import com.podo.helloprice.crawl.agent.job.ProductUpdateEvent;
import com.podo.helloprice.crawl.agent.job.ProductUpdateEventStore;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import com.podo.helloprice.crawl.worker.value.CrawledProductPrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.podo.helloprice.core.enums.SaleType.*;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class CrawlProductUpdateJobWriter implements ItemWriter<CrawledProduct> {


    @Value("${product.max_dead_count}")
    private Integer maxDeadCount;

    private final CrawlProductJobParameter crawlProductJobParameter;
    private final ProductUpdateEventStore productUpdateEventStore;

    private final ProductRepository productRepository;
    private final ProductSaleRepository productSaleRepository;

    @Override
    public void write(List<? extends CrawledProduct> crawledProducts) {
        for (CrawledProduct crawledProduct : crawledProducts) {
            updateProduct(crawledProduct);
        }
    }

    private void updateProduct(CrawledProduct crawledProduct) {
        final LocalDateTime crawledAt = crawledProduct.getCrawledAt();
        final ProductToCrawl productToCrawl = crawlProductJobParameter.getProductToCrawl();
        final String productName = productToCrawl.getProductName();
        final String productCode = productToCrawl.getProductCode();
        final Product product = productRepository.findByProductCode(productCode);
        final Long productId = product.getId();

        if (CrawledProduct.FAIL.equals(crawledProduct)) {
            log.debug("STEP :: WRITER :: {}({}) :: 상품의 정보 갱신 에러 발생", productName, productCode);
            if (product.increaseDeadCount(maxDeadCount)) {
                updateAllProductPrices(productId, 0, "", crawledAt);
                productUpdateEventStore.add(ProductUpdateEvent.create(productId, ProductUpdateStatus.UPDATE_DEAD));
            }
            return;
        }

        product.updateByCrawledProduct(crawledProduct);

        switch (crawledProduct.getSaleStatus()) {
            case UNKNOWN:
                updateAllProductPrices(productId, 0, "", crawledAt);
                productUpdateEventStore.add(ProductUpdateEvent.create(productId, ProductUpdateStatus.UPDATE_UNKNOWN));
            case DISCONTINUE:
                updateAllProductPrices(productId, 0, "", crawledAt);
                productUpdateEventStore.add(ProductUpdateEvent.create(productId, ProductUpdateStatus.UPDATE_DISCONTINUE));
            case NOT_SUPPORT:
                updateAllProductPrices(productId, 0, "", crawledAt);
                productUpdateEventStore.add(ProductUpdateEvent.create(productId, ProductUpdateStatus.UPDATE_NOT_SUPPORT));
            case EMPTY_AMOUNT:
                updateAllProductPrices(productId, 0, "", crawledAt);
                productUpdateEventStore.add(ProductUpdateEvent.create(productId, ProductUpdateStatus.UPDATE_EMPTY_AMOUNT));
            case SALE:
                productUpdateEventStore.addAll(updatePrices(productId, crawledProduct, crawledAt));
        }

        log.debug("STEP :: WRITER :: {}({}) ::  상품상태 : `{}`, 상품판매상태 `{}`", productName, productCode, product.getAliveStatus(), ProductSaleStatusParser.kr(product.getSaleStatus()));
    }

    private void updateAllProductPrices(Long productId, Integer price, String additionalInfo, LocalDateTime crawledAt) {
        final List<ProductSale> productSales = productSaleRepository.findByProductId(productId);

        for (ProductSale productSale : productSales) {
            productSale.update(price, additionalInfo, crawledAt);
        }
    }

    private List<ProductUpdateEvent> updatePrices(Long productId, CrawledProduct crawledProduct, LocalDateTime crawledAt) {
        final List<ProductUpdateEvent> productUpdateEvents = new ArrayList<>();

        if (updatePrice(productId, crawledProduct.getProductPriceByType(NORMAL), NORMAL, crawledAt)) {
            productUpdateEvents.add(ProductUpdateEvent.create(productId, ProductUpdateStatus.UPDATE_SALE_NORMAL_PRICE));
        }

        if (updatePrice(productId, crawledProduct.getProductPriceByType(CASH), CASH, crawledAt)) {
            productUpdateEvents.add(ProductUpdateEvent.create(productId, ProductUpdateStatus.UPDATE_SALE_CASH_PRICE));
        }

        if (updatePrice(productId, crawledProduct.getProductPriceByType(CARD), CARD, crawledAt)) {
            productUpdateEvents.add(ProductUpdateEvent.create(productId, ProductUpdateStatus.UPDATE_SALE_CARD_PRICE));
        }

        return productUpdateEvents;
    }

    private boolean updatePrice(Long productId, CrawledProductPrice crawledProductSale, SaleType saleType, LocalDateTime crawledAt) {

        final Optional<ProductSale> existedProductSale = productSaleRepository.findByProductIdAndSaleType(productId, saleType);

        //기존에 있는 경우, 업데이트
        if (existedProductSale.isPresent() && crawledProductSale != null) {
            return existedProductSale.get().update(crawledProductSale.getPrice(), crawledProductSale.getAdditionalInfo(), crawledAt);
        }

        //기존에 없는 경우, 새로 등록
        if (!existedProductSale.isPresent() && crawledProductSale != null) {
            final Product product = productRepository.findById(productId).orElseThrow(() -> new InvalidProductIdException(productId));

            final ProductSale productSale =
                    ProductSale.create(saleType, product, crawledProductSale.getPrice(), crawledProductSale.getAdditionalInfo(), crawledAt);
            productSaleRepository.save(productSale);
            return true;
        }

        return false;
    }

}
