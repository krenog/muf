package com.krenog.myf.security.jwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    private static final Logger log = LogManager.getLogger(JwtAuthTokenFilter.class);
    private final JwtConfig jwtConfig;
    private final JwtProvider tokenProvider;

    public JwtAuthTokenFilter(JwtConfig jwtConfig, JwtProvider tokenProvider) {
        this.jwtConfig = jwtConfig;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwt(httpServletRequest);
            // 2. validate the header and check the prefix
            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                UserDetails userDetails = tokenProvider.parseToken(jwt);
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception e) {
            log.error("Can NOT set user authentication -> Message: {0}", e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwt(HttpServletRequest request) {
        return request.getHeader(jwtConfig.getHeader());
    }
}
