package com.podo.itemwatcher.telegram.global.infra.telegram.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HomeCommand implements Command {


    ADD_ITEM("추가"),
    DELETE_ITEM("삭제");

    private String value;

    public static HomeCommand from(String str) {
        return Command.from(values(), str, null);
    }


}
