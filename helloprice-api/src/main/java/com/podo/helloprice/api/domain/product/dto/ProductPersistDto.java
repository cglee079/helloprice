package com.podo.helloprice.api.domain.product.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ProductPersistDto {

    @NotBlank
    private String productCode;

}
