package com.podo.itemwatcher.telegram.global.infra.telegram.message;

public class MessageMaker {

    public static String introduce(String username) {
        StringBuilder message = new StringBuilder();

        message.append(username)
                .append("님 안녕하세요! \n")
                .append("본 서비스는 최저가 알림을 주는 봇입니다! \n")
                .append("아이템을 등록하면, 최저가 갱신됬을때, \n")
                .append("텔레그램을 통해 알람을 드려요! \n");

        return message.toString();
    }

    public static String wrongInput() {
        return "잘못된 값을 입력하셨습니다";
    }

    public static String explainAddItem() {
        StringBuilder message = new StringBuilder();

        message.append("다나와에서 아이템페이지의 URL을 입력해주세요! \n");

        return message.toString();

    }

    public static String explainDeleteItem() {
        StringBuilder message = new StringBuilder();

        message.append("삭제할 아이템을 선택해주세요\n")
                .append("삭제된 아이템은 더 이상 알림이 가지않습니다");

        return message.toString();

    }

    public static String wrongItemUrl(String url) {
        StringBuilder message = new StringBuilder();

        message.append("아이템 페이지의 URL이 잘못되었습니다\n")
                .append("URL : ")
                .append(url);

        return message.toString();
    }
}
