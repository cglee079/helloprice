package com.podo.helloprice.api.domain.usernotify.api;

import com.podo.helloprice.api.domain.usernotify.application.UserNotifyAddService;
import com.podo.helloprice.api.domain.usernotify.application.UserNotifyReadService;
import com.podo.helloprice.api.domain.usernotify.application.UserNotifyRemoveService;
import com.podo.helloprice.api.domain.usernotify.dto.UserNotifyAddDto;
import com.podo.helloprice.api.domain.usernotify.dto.UserNotifyRemoveDto;
import com.podo.helloprice.api.global.rest.response.CollectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.podo.helloprice.api.global.security.SecurityUtil.getUserId;


@RequiredArgsConstructor
@RestController
public class UserNotifyApi {

    private final UserNotifyReadService userNotifyReadService;
    private final UserNotifyAddService userNotifyAddService;
    private final UserNotifyRemoveService userNotifyRemoveService;

    @GetMapping("/api/v0/my/notifies")
    public CollectionResponse list(){
        return CollectionResponse.ok(userNotifyReadService.list(getUserId()));
    }

    @PostMapping("/api/v0/my/notifies")
    public void insert(@Valid @RequestBody UserNotifyAddDto userProductSaleNotifyInsert){
        userNotifyAddService.insert(getUserId(), userProductSaleNotifyInsert);
    }

    @DeleteMapping("/api/v0/my/notifies")
    public void deleteByProductSale(@Valid UserNotifyRemoveDto userNotifyRemoveDto){
        userNotifyRemoveService.removeByProductSale(getUserId(), userNotifyRemoveDto);
    }


}
