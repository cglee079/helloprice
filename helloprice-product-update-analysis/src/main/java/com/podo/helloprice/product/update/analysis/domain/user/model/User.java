package com.podo.helloprice.product.update.analysis.domain.user.model;

import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.product.update.analysis.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userKey;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private String username;

    private String picture;

    private UserRole role;
}
