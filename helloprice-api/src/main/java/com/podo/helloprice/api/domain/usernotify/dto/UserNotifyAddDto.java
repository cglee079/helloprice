package com.podo.helloprice.api.domain.usernotify.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class UserNotifyAddDto {

    @NotNull
    private Long productSaleId;
}
