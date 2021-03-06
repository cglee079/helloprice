package com.podo.helloprice.product.update.analysis.domain.tuser;

import com.podo.helloprice.core.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tuser")
@Entity
public class TUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String telegramId;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
}
