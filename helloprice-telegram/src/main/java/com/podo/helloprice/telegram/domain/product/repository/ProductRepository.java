package com.podo.helloprice.telegram.domain.product.repository;

import com.podo.helloprice.code.model.ProductAliveStatus;
import com.podo.helloprice.code.model.ProductUpdateStatus;
import com.podo.helloprice.telegram.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(String productCode);

    List<Product> findByAliveStatusAndUpdateStatus(ProductAliveStatus productStatus, ProductUpdateStatus productUpdateStatus);
}
