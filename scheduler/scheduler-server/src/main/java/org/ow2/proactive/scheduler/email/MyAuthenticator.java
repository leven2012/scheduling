package org.ow2.proactive.scheduler.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by leven2 on 16-4-12.
 */
public class MyAuthenticator extends Authenticator {
    private String username = null;
    private String password = null;

    public MyAuthenticator() {
    };

    public MyAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}