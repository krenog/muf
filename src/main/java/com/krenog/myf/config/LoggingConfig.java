package com.krenog.myf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {
    @Bean
    public RequestLoggingFilter logFilter() {
        RequestLoggingFilter filter = new RequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setIncludeClientInfo(true);
        filter.setMaxPayloadLength(900_000);
        filter.setIncludeHeaders(false);
        filter.setBeforeMessagePrefix("Incoming request start: ");
        filter.setBeforeMessageSuffix("");
        filter.setAfterMessagePrefix("Incoming request end: ");
        filter.setAfterMessageSuffix("");
        return filter;
    }
}
