package com.podo.helloprice.api.domain.user.repository;

import com.podo.helloprice.api.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserKey(String userKey);

}
