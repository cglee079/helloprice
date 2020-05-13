package com.podo.helloprice.product.update.analysis.processor.notify;

import com.podo.helloprice.product.update.analysis.domain.tuser.TUserDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class NotifyTarget {

    public static final NotifyTarget EMPTY = new NotifyTarget();

    private List<TUserDto> users;
    private String imageUrl;
    private String title;
    private String contents;
}
