package com.podo.helloprice.product.update.analysis.domain.user;

import com.podo.helloprice.core.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id IN :userIds AND u.userStatus = :userStatus")
    List<User> findTelegramIdsByUserIdsAndUserStatus(@Param("userIds") List<Long> userIds, @Param("userStatus") UserStatus userStatus);

}