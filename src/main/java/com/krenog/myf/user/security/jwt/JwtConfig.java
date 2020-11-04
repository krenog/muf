package com.krenog.myf.user.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

    @Value("${security.jwt.token.header-string}")
    private String header;

    @Value("${security.jwt.token.expire-length}")
    private long expiration;

    @Value("${security.jwt.token.secret-key}")
    private String secret;

    @Value("${security.jwt.claim.userAuthorities}")
    private String userAuthoritiesClaimKey;

    @Value("${security.jwt.claim.userId}")
    private String userIdClaimKey;

    @Value("${security.jwt.audience}")
    private String appName;

    String getUserAuthoritiesClaimKey() {
        return userAuthoritiesClaimKey;
    }

    public void setUserAuthoritiesClaimKey(String userAuthoritiesClaimKey) {
        this.userAuthoritiesClaimKey = userAuthoritiesClaimKey;
    }

    String getUserIdClaimKey() {
        return userIdClaimKey;
    }

    public void setUserIdClaimKey(String userIdClaimKey) {
        this.userIdClaimKey = userIdClaimKey;
    }

    String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
