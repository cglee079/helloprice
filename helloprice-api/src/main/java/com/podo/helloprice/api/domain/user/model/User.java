package com.podo.helloprice.api.domain.user.model;

import com.podo.helloprice.core.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String telegramId;

    private String email;

    private Integer errorCount;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private LocalDateTime lastSendAt;

}
