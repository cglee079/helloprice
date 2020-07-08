package com.podo.helloprice.telegram.domain.productsale.application;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import com.podo.helloprice.telegram.domain.productsale.ProductSaleRepository;
import com.podo.helloprice.telegram.domain.productsale.dto.ProductSaleDto;
import com.podo.helloprice.telegram.domain.productsale.exception.InvalidProductCodeAndSaleTypeException;
import com.podo.helloprice.telegram.domain.productsale.exception.InvalidProductIdAndSaleTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProductSaleReadService {

    private final ProductSaleRepository productSaleRepository;

    public Map<SaleType, ProductSaleDto> findByProductId(Long productId) {
        List<ProductSale> productSales = productSaleRepository.findByProductId(productId);

        return productSales.stream()
                .map(ProductSaleDto::new)
                .collect(Collectors.toMap(ProductSaleDto::getSaleType, s -> s));
    }

    public ProductSaleDto findByProductIdAndSaleType(Long productId, SaleType saleType) {
        final ProductSale productSale = productSaleRepository.findByProductIdAndSaleType(productId, saleType)
                .orElseThrow(() -> new InvalidProductIdAndSaleTypeException(productId, saleType));

        return new ProductSaleDto(productSale);
    }

    public ProductSaleDto findByProductCodeAndSaleType(String productCode, SaleType saleType) {
        final ProductSale productSale = productSaleRepository.findByProductCodeAndSaleType(productCode, saleType)
                .orElseThrow(() -> new InvalidProductCodeAndSaleTypeException(productCode, saleType));

        return new ProductSaleDto(productSale);
    }
}
