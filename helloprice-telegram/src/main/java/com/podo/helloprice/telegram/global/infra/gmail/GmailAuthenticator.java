package com.podo.helloprice.telegram.global.infra.gmail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


@Component
public class GmailAuthenticator extends Authenticator {

    private final PasswordAuthentication passwordAuthentication;

    public GmailAuthenticator(
            @Value("${infra.gmail.admin.id}") String id,
            @Value("${infra.gmail.admin.password}") String password) {
        passwordAuthentication = new PasswordAuthentication(id, password);
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return passwordAuthentication;
    }
}
