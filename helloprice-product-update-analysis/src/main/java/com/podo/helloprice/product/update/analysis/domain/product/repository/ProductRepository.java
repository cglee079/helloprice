package com.podo.helloprice.product.update.analysis.domain.product.repository;

import com.podo.helloprice.product.update.analysis.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
