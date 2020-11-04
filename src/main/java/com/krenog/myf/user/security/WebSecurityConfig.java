package com.krenog.myf.user.security;

import com.krenog.myf.user.security.jwt.JwtAuthTokenFilter;
import com.krenog.myf.user.security.jwt.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final List<String> excludedPaths;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthTokenFilter jwtAuthTokenFilter;

    public WebSecurityConfig(List<String> excludedPaths, JwtAuthenticationEntryPoint unauthorizedHandler, JwtAuthTokenFilter jwtAuthTokenFilter) {
        this.excludedPaths = excludedPaths;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtAuthTokenFilter = jwtAuthTokenFilter;
    }

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(loggingFilter(), JwtAuthTokenFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        excludedPaths.forEach(excludedPath -> web.ignoring().antMatchers(excludedPath));
    }
}
