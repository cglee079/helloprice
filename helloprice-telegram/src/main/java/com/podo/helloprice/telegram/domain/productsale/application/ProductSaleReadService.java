package com.podo.helloprice.telegram.domain.productsale.application;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import com.podo.helloprice.telegram.domain.productsale.ProductSaleRepository;
import com.podo.helloprice.telegram.domain.productsale.dto.ProductSaleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

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

        //TODo
        final ProductSale productSale = productSaleRepository.findByProductIdAndSaleType(productId, saleType).get();
        return new ProductSaleDto(productSale);
    }

    public ProductSaleDto findByProductCodeAndSaleType(String productCode, SaleType saleType) {
        //TODO
        final ProductSale productSale = productSaleRepository.findByProductCodeAndSaleType(productCode, saleType).get();
        return new ProductSaleDto(productSale);
    }
}
