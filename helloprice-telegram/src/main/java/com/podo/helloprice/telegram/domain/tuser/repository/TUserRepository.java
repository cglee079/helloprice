package com.podo.helloprice.telegram.domain.tuser.repository;

import com.podo.helloprice.telegram.domain.tuser.model.TUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TUserRepository extends JpaRepository<TUser, Long> {

    Optional<TUser> findByTelegramId(String telegramId);

}
