package com.podo.helloprice.core.domain.user.repository;

import com.podo.helloprice.core.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTelegramId(String telegramId);

}
