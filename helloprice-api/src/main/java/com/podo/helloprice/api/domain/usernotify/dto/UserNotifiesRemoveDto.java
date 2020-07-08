package com.podo.helloprice.api.domain.usernotify.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UserNotifiesRemoveDto {

    @NotNull
    private Long productId;
}
