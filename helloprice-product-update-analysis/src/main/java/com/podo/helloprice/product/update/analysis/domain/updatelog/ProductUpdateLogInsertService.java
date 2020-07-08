package com.podo.helloprice.product.update.analysis.domain.updatelog;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.product.update.analysis.domain.product.exception.InvalidProductIdException;
import com.podo.helloprice.product.update.analysis.domain.product.model.Product;
import com.podo.helloprice.product.update.analysis.domain.product.repository.ProductRepository;
import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSale;
import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSaleRepository;
import com.podo.helloprice.product.update.analysis.domain.productsale.exception.InvalidProductSaleByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductUpdateLogInsertService {

    private final ProductUpdateLogRepository productUpdateLogRepository;
    private final ProductSaleRepository productSaleRepository;
    private final ProductRepository productRepository;

    public void insertNew(Long productId, ProductUpdateStatus productUpdateStatus, LocalDateTime now) {

        final Optional<Product> productOptional = productRepository.findById(productId);

        final Product product = productOptional.orElseThrow(() -> new InvalidProductIdException(productId));

        if (productUpdateStatus.isSale()) {
            final ProductSale productSale = getProductSaleByUpdateStatus(productId, productUpdateStatus)
                    .orElseThrow(() -> new InvalidProductSaleByIdException(productId));

            productUpdateLogRepository.save(new ProductUpdateLog(product, productSale.getPrice(), productSale.getPrevPrice(), productUpdateStatus, now));
            return;
        }

        productUpdateLogRepository.save(new ProductUpdateLog(product, productUpdateStatus, now));
    }

    private Optional<ProductSale> getProductSaleByUpdateStatus(Long productId, ProductUpdateStatus productUpdateStatus) {
        switch (productUpdateStatus) {
            case UPDATE_SALE_NORMAL_PRICE:
                return productSaleRepository.findByProductIdAndSaleType(productId, SaleType.NORMAL);
            case UPDATE_SALE_CASH_PRICE:
                return productSaleRepository.findByProductIdAndSaleType(productId, SaleType.CASH);
            case UPDATE_SALE_CARD_PRICE:
                return productSaleRepository.findByProductIdAndSaleType(productId, SaleType.CARD);
        }

        return Optional.empty();
    }
}
