package com.podo.helloprice.telegram.global.infra.gmail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class GmailNotifier {

    private final GmailClient gmailClient;

    public void notifyUser(String userName, String userEmail, String title, String image, String contents) {
        log.info("{}({})님에게 이메일로 알림을 전송합니다, 보낸이메일 {}", userName, userEmail, title);

        final StringBuilder htmlContents = new StringBuilder();

        htmlContents.append("<div>");

        if (Objects.nonNull(image)) {
            htmlContents.append("<div>")
                    .append("<img src='")
                    .append(image)
                    .append("'/>")
                    .append("<br/>")
                    .append("<br/>")
                    .append("</div>");
        }

        htmlContents.append("<div>")
                .append(contents.replaceAll("\n", "<br/>"))
                .append("</div>");

        htmlContents.append("</div>");

        gmailClient.sendEmail(userName, userEmail, title, htmlContents.toString());
    }

    public void notifyAdmin(String title, String contents) {
        gmailClient.sendAdmin(title, contents);
    }
}
