package com.podo.helloprice.telegram.client.menu;

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
