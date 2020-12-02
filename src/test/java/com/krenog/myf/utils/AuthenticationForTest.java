package com.krenog.myf.utils;

import com.krenog.myf.user.security.detail.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.krenog.myf.utils.TestUtils.*;

public class AuthenticationForTest implements Authentication {
    private boolean authenticated = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public Object getCredentials() {
        return new Object();
    }

    @Override
    public Object getDetails() {
        return new Object();
    }

    @Override
    public Object getPrincipal() {
        List<GrantedAuthority> authoritiesTest = new ArrayList<>();
        return new UserPrincipal(TEST_ID, TEST_PHONE_NUMBER,"test", authoritiesTest);
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.authenticated = b;
    }

    @Override
    public String getName() {
        return "Test";
    }

}
