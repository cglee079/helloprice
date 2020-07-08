package com.podo.helloprice.telegram.domain.product.repository;

import com.podo.helloprice.telegram.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(String productCode);

}
