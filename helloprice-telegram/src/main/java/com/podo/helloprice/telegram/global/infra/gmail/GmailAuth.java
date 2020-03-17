package com.podo.helloprice.telegram.global.infra.gmail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


@Component
public class GmailAuth extends Authenticator {

    private final PasswordAuthentication passwordAuthentication;

    public GmailAuth(
            @Value("${infra.gmail.admin.id}") String id,
            @Value("${infra.gmail.admin.password}") String password) {
        passwordAuthentication = new PasswordAuthentication(id, password);
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return passwordAuthentication;
    }
}
