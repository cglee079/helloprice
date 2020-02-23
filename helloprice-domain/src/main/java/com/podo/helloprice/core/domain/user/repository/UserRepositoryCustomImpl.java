package com.podo.helloprice.core.domain.user.repository;

import com.podo.helloprice.core.domain.user.User;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class UserRepositoryCustomImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {


    public UserRepositoryCustomImpl() {
        super(User.class);
    }

}
