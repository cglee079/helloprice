package com.podo.helloprice.product.update.analysis.domain.productsale;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.product.update.analysis.domain.productsale.dto.ProductSaleDto;
import com.podo.helloprice.product.update.analysis.domain.productsale.exception.InvalidProductIdAndSaleTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductSaleReadService {

    private final ProductSaleRepository productSaleRepository;

    public ProductSaleDto findByProductIdAndSaleType(Long productId, SaleType saleType) {
        final ProductSale productSale = productSaleRepository.findByProductIdAndSaleType(productId, saleType)
                .orElseThrow(() -> new InvalidProductIdAndSaleTypeException(productId, saleType));
        return new ProductSaleDto(productSale);
    }
}
