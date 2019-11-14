package com.podo.helloprice.telegram.client.menu.home;

import com.podo.helloprice.telegram.client.menu.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HomeCommand implements Command {


    ITEM_SEARCH_ADD("검색 후, 알림추가"),
    ITEM_ADD("상품 알림 추가"),
    ITEM_DELETE("상품 알림 삭제"),
    HELP("도움말");


    private String value;

    public static HomeCommand from(String str) {
        return Command.from(values(), str, null);
    }


}
