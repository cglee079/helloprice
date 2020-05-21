package com.podo.helloprice.product.update.analysis.processor.pushnotify;

import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PushTarget {

    public static final PushTarget EMPTY = new PushTarget();

    private List<UserDto> users;
    private String imageUrl;
    private String redirectUrl;
    private String contents;

    public PushTarget(List<UserDto> users, String imageUrl, String contents) {
        this.users = users;
        this.imageUrl = imageUrl;
        this.contents = contents;
    }

    public PushTarget(List<UserDto> users, String imageUrl, String redirectUrl, String contents) {
        this.users = users;
        this.imageUrl = imageUrl;
        this.redirectUrl = redirectUrl;
        this.contents = contents;
    }
}
