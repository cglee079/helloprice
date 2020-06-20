package com.podo.helloprice.telegram.domain.tuser.repository;

import com.podo.helloprice.telegram.domain.tuser.model.TUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TUserRepository extends JpaRepository<TUser, Long> {

    Optional<TUser> findByTelegramId(String telegramId);

    @Query("SELECT tu.telegramId from TUser tu")
    List<String> findAllId();

}
