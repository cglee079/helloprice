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

    public void notifyUser(String username, String userEmail, String emailTitle, String contentImage, String contents) {
        log.debug("EMAIL :: {}({})님에게 이메일로 알림을 전송합니다, 보낸이메일 {}", username, userEmail, emailTitle);

        final StringBuilder htmlContents = new StringBuilder();

        htmlContents.append("<div>");

        if (Objects.nonNull(contentImage)) {
            htmlContents.append("<div>")
                    .append("<img src='")
                    .append(contentImage)
                    .append("'/>")
                    .append("<br/>")
                    .append("<br/>")
                    .append("</div>");
        }

        htmlContents.append("<div>")
                .append(contents.replaceAll("\n", "<br/>"))
                .append("</div>");

        htmlContents.append("</div>");

        gmailClient.sendEmail(username, userEmail, emailTitle, htmlContents.toString());
    }

    public void notifyAdmin(String emailTitle, String contents) {
        gmailClient.sendEmailToAdmin(emailTitle, contents);
    }
}
