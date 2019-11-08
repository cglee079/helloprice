package com.podo.itemwatcher.core.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByTelegramId(String telegramId);

    boolean hasNotifyItem(Long itemId);
}
