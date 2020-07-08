package com.podo.helloprice.product.update.analysis.domain.tuser;

import com.podo.helloprice.core.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TUserRepository extends JpaRepository<TUser, Long> {

    @Query("SELECT u FROM TUser u WHERE u.id IN :userIds AND u.userStatus = :userStatus")
    List<TUser> findByUserIdsAndUserStatus(@Param("userIds") List<Long> userIds, @Param("userStatus") UserStatus userStatus);

}
