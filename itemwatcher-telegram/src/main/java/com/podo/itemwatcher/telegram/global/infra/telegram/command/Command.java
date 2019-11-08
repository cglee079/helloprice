package com.podo.itemwatcher.telegram.global.infra.telegram.command;

public interface Command {

    String getValue();

    static <E extends Enum<E> & Command> E from(E[] values, String str, E defaultCommand) {

        for (E b : values) {
            if (b.getValue().equalsIgnoreCase(str)) {
                return b;
            }
        }

        return defaultCommand;
    }

}
