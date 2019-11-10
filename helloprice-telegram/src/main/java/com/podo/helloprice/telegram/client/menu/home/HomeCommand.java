package com.podo.helloprice.telegram.client.menu.home;

import com.podo.helloprice.telegram.client.menu.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HomeCommand implements Command {


    ADD_ITEM("상품 알림 추가"),
    DELETE_ITEM("상품 알림 삭제"),
    HELP("도움말");


    private String value;

    public static HomeCommand from(String str) {
        return Command.from(values(), str, null);
    }


}
