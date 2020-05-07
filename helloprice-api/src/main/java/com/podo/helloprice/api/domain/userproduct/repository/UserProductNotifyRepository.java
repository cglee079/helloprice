package com.podo.helloprice.api.domain.userproduct.repository;

import com.podo.helloprice.api.domain.userproduct.UserProductSaleNotify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProductNotifyRepository extends JpaRepository<UserProductSaleNotify, Long>{
}
