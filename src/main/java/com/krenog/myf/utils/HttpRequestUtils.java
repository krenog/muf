package com.krenog.myf.utils;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtils {
    private HttpRequestUtils() {
    }

    private static final String IP_HEADER = "X-FORWARDED-FOR";
    private static final String AGENT_HEADER = "User-Agent";
    private static final String VERSION_HEADER = "appVersion";
    private static final String EMPTY_STRING = "";
    private static final String IOS = "ios";
    private static final String ANDROID = "android";

    public static String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader(IP_HEADER);
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress == null) {
            ipAddress = EMPTY_STRING;
        }
        return ipAddress;
    }

    public static String getAgent(HttpServletRequest request) {
        String agent = request.getHeader(AGENT_HEADER);
        if (agent == null) {
            return EMPTY_STRING;
        } else {
            return getAgent(agent);
        }
    }

    private static String getAgent(String agent) {
        if (agent.toLowerCase().startsWith(ANDROID)) {
            return ANDROID;
        } else if (agent.toLowerCase().contains(IOS)) {
            return IOS;
        } else {
            return EMPTY_STRING;
        }
    }

    public static String getAppVersion(HttpServletRequest request) {
        String version = request.getHeader(VERSION_HEADER);
        if (version == null) {
            return EMPTY_STRING;
        } else {
            return version;
        }
    }
}
