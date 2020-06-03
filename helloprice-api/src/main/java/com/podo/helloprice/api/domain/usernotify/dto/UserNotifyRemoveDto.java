package com.podo.helloprice.api.domain.usernotify.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class UserNotifyRemoveDto {

    @NotNull
    private Long productSaleId;
}
