package com.krenog.myf.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

public class RequestLoggingFilter extends AbstractRequestLoggingFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final int DEFAULT_MAX_VALUE = 8048;

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        // Логируем только обращения к API
        return request.getRequestURI().startsWith("/api");
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        log.info(getFormattedMessage(message));
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        log.info(getFormattedMessage(message));
    }

    @Override
    protected String getMessagePayload(HttpServletRequest request) {
        // Скрываем пароли пользователей при аутентификации
        if (request.getRequestURI().startsWith("/api/v1/sign-in")) {
            return "***HIDDEN***";
        }
        String messagePayload = super.getMessagePayload(request);
        return getFormattedMessage(messagePayload);
    }

    private static String getFormattedMessage(String messagePayload) {
        if (StringUtils.isNotBlank(messagePayload)
                && messagePayload.length() > DEFAULT_MAX_VALUE) {
            messagePayload = messagePayload.substring(0, DEFAULT_MAX_VALUE);
        }
        return messagePayload;
    }


}
