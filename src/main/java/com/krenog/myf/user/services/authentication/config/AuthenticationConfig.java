package com.krenog.myf.user.services.authentication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationConfig {
    @Value("${sms.sms-length}")
    private int length;
    @Value("${sms.enabled}")
    private boolean trafficEnabled;
    @Value("${sms.test.code}")
    private String testCode;
    @Value("${sms.test.phone}")
    private String phone;


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public boolean isTrafficEnabled() {
        return trafficEnabled;
    }

    public void setTrafficEnabled(boolean trafficEnabled) {
        this.trafficEnabled = trafficEnabled;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
