package com.podo.helloprice.product.update.analysis.facade;

import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import lombok.Getter;

import java.util.List;

@Getter
public class NotifyTarget {
    private List<UserDto> users;
    private ProductDetailDto product;

    public NotifyTarget(List<UserDto> users, ProductDetailDto product) {
        this.users = users;
        this.product = product;
    }
}
