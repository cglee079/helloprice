package com.podo.helloprice.telegram.domain.productsale.application;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.crawl.worker.value.CrawledProductPrice;
import com.podo.helloprice.telegram.domain.product.application.helper.ProductReadHelper;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.product.repository.ProductRepository;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import com.podo.helloprice.telegram.domain.productsale.ProductSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.podo.helloprice.core.enums.SaleType.*;

@RequiredArgsConstructor
@Service
public class ProductSaleWriteService {

    private final ProductRepository productRepository;
    private final ProductSaleRepository productSaleRepository;

    public void setPriceZeroByProductId(Long productId, LocalDateTime crawledAt) {
        final List<ProductSale> productSales = productSaleRepository.findByProductId(productId);
        for (ProductSale productSale : productSales) {
            productSale.update(0, "", crawledAt);
        }
    }

    public void updateSalePrice(Long productId, Map<SaleType, CrawledProductPrice> saleTypeToPrice, LocalDateTime crawledAt) {
        updatePrice(productId, saleTypeToPrice.get(NORMAL), NORMAL, crawledAt);
        updatePrice(productId, saleTypeToPrice.get(CASH), CASH, crawledAt);
        updatePrice(productId, saleTypeToPrice.get(CARD), CARD, crawledAt);
    }

    private boolean updatePrice(Long productId, CrawledProductPrice crawledProductSale, SaleType saleType, LocalDateTime crawledAt) {

        final Optional<ProductSale> existedProductSale = productSaleRepository.findByProductIdAndSaleType(productId, saleType);

        //기존에 있는 경우, 업데이트
        if (existedProductSale.isPresent() && crawledProductSale != null) {
            return existedProductSale.get().update(crawledProductSale.getPrice(), crawledProductSale.getAdditionalInfo(), crawledAt);
        }

        //기존에 없는 경우, 새로 등록
        if (!existedProductSale.isPresent() && crawledProductSale != null) {
            final ProductSale productSale = ProductSale.create(saleType, crawledProductSale.getPrice(), crawledProductSale.getAdditionalInfo(), crawledAt);
            productSaleRepository.save(productSale);

            final Product product = ProductReadHelper.findProductById(productRepository, productId);
            product.putProductSale(productSale.getSaleType(), productSale);

            return true;
        }

        return false;
    }
}
