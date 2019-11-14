package com.podo.helloprice.telegram.client.menu.itemserachadd;

public class ItemSearchAddResponse {

    public static String explain() {
        return new StringBuilder()
                .append("해당 상품이 맞으신가요?\n")
                .append("\n")
                .append("알림을 추가하시겠습니까\n")
                .toString();

    }

}
