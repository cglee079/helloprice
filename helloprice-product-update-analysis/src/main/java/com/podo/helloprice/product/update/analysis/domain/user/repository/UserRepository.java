package com.podo.helloprice.product.update.analysis.domain.user.repository;

import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.product.update.analysis.domain.tuser.TUser;
import com.podo.helloprice.product.update.analysis.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT u FROM User u WHERE u.id IN :userIds AND u.userStatus = :userStatus")
    List<User> findByUserIdsAndUserStatus(@Param("userIds") List<Long> userIds, @Param("userStatus") UserStatus userStatus);


}
