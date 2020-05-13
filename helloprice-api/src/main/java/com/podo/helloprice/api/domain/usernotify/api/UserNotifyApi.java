package com.podo.helloprice.api.domain.usernotify.api;

import com.podo.helloprice.api.domain.usernotify.application.UserNotifyAddService;
import com.podo.helloprice.api.domain.usernotify.application.UserNotifyRemoveService;
import com.podo.helloprice.api.domain.usernotify.dto.UserNotifyAddDto;
import com.podo.helloprice.api.domain.usernotify.dto.UserNotifyRemoveDto;
import com.podo.helloprice.api.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
public class UserNotifyApi {

    private final UserNotifyAddService userNotifyAddService;
    private final UserNotifyRemoveService userNotifyRemoveService;

    @PostMapping("/api/v0/my/notifies")
    public void insert(@Valid @RequestBody UserNotifyAddDto userProductSaleNotifyInsert){
        userNotifyAddService.insert(SecurityUtil.getUserId(), userProductSaleNotifyInsert);
    }

    @DeleteMapping("/api/v0/my/notifies")
    public void insert(@Valid @RequestBody UserNotifyRemoveDto userProductSaleNotifyDelete){
        userNotifyRemoveService.remove(SecurityUtil.getUserId(), userProductSaleNotifyDelete);
    }


}
