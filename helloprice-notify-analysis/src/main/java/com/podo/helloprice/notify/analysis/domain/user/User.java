package com.podo.helloprice.notify.analysis.domain.user;

import com.podo.helloprice.core.model.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String telegramId;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
}
