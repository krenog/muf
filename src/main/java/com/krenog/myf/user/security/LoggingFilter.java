package com.krenog.myf.user.security;

import com.krenog.myf.user.security.detail.UserPrincipal;
import com.krenog.myf.utils.HttpRequestUtils;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoggingFilter extends OncePerRequestFilter {
    private static final String USERNAME_FIELD = "Username";
    private static final String IP_FIELD = "IP";
    private static final String AGENT_FIELD = "AGENT";
    private static final String VERSION_FIELD = "APPVERSION";
    private static final String ANONYMOUS_USER = "ANONYMOUS_USER";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            UserPrincipal user;
            Authentication auth = context.getAuthentication();
            if (auth != null && auth.getPrincipal() != null) {
                if (auth.getPrincipal() instanceof UserPrincipal) {
                    user = (UserPrincipal) auth.getPrincipal();
                    MDC.put(USERNAME_FIELD, user.getUsername());
                }
            } else {
                MDC.put(USERNAME_FIELD, ANONYMOUS_USER);
            }
            MDC.put(IP_FIELD, HttpRequestUtils.getIp(request));

            MDC.put(AGENT_FIELD, HttpRequestUtils.getAgent(request));
            MDC.put(VERSION_FIELD, HttpRequestUtils.getAppVersion(request));
        }
        chain.doFilter(request, response);
    }

}
