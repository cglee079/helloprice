package com.podo.helloprice.telegram.domain.user.repository;

import com.podo.helloprice.telegram.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTelegramId(String telegramId);

}
