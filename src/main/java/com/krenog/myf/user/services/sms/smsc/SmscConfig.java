package com.krenog.myf.user.services.sms.smsc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmscConfig {
    @Value("${sms.smsc.url}")
    private String url;
    @Value("${sms.smsc.originator}")
    private String originator;
    @Value("${sms.smsc.login}")
    private String login;
    @Value("${sms.smsc.password}")
    private String password;

    String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
