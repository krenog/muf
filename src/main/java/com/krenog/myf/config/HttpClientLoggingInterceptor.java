package com.krenog.myf.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class HttpClientLoggingInterceptor implements HttpRequestInterceptor, HttpResponseInterceptor {
    private static final int MAX_CONTENT_LEN = 8048;
    private static final int DEFAULT_BUFFER_SIZE = MAX_CONTENT_LEN + 512;
    private static final String REQUEST_TEXT_ID = "http.client.logging.requestText";
    private static Logger logger = LogManager.getLogger(HttpClientLoggingInterceptor.class);

    @Override
    public void process(HttpRequest req, HttpContext context) {
        if (!logger.isDebugEnabled()) {
            return;
        }
        String requestText = getRequestText(req);
        String requestBody = getRequestBody(req);
        // Запоминаем текст запроса
        context.setAttribute(REQUEST_TEXT_ID, requestText);
        StringBuilder sb = new StringBuilder(DEFAULT_BUFFER_SIZE);
        sb.append("Outgoing request start: ").append(requestText);
        if (StringUtils.isNotEmpty(requestBody)) {
            sb.append("; payload=").append(requestBody);
        }
        logger.info("Message : {}", sb);
    }

    @Override
    public void process(HttpResponse response, HttpContext context) {
        int status = response.getStatusLine().getStatusCode();
        Level level = status >= 400 ? Level.WARN : Level.DEBUG;
        if (!logger.isEnabled(level)) {
            return;
        }
        // Получаем запомненный ранее текст запроса из контекста
        String requestText = (String) context.getAttribute(REQUEST_TEXT_ID);
        String responseBody = getResponseBody(response);
        StringBuilder sb = new StringBuilder(DEFAULT_BUFFER_SIZE);
        sb.append("Outgoing request ").append(status >= 400 ? "error" : "end");
        sb.append(": status=").append(status);
        sb.append("; request=").append(requestText);
        sb.append("; response=").append(StringUtils.defaultIfEmpty(responseBody, "<empty>"));
        logger.info("Message : {}", sb);
    }

    private String getRequestText(HttpRequest req) {
        if (req instanceof HttpRequestWrapper) {
            HttpRequestWrapper wrapper = (HttpRequestWrapper) req;
            String targetUri;
            if (wrapper.getURI().isAbsolute()) {
                targetUri = wrapper.getURI().toString();
            } else {
                targetUri = wrapper.getTarget().toURI() + wrapper.getURI();
            }
            return wrapper.getMethod() + " " + targetUri;
        }
        RequestLine reqLine = req.getRequestLine();
        return reqLine.getMethod() + " " + reqLine.getUri();
    }

    private String getRequestBody(HttpRequest req) {
        if (!(req instanceof HttpEntityEnclosingRequest)) {
            return null;
        }
        HttpEntityEnclosingRequest request = (HttpEntityEnclosingRequest) req;
        try {
            HttpEntity entity = request.getEntity();
            request.setEntity(new BufferedHttpEntity(entity)); // буферизуем тело запроса
            InputStream content = request.getEntity().getContent();
            return getStreamContent(content);
        } catch (Exception ex) {
            logger.warn("An error occurred while getting http request body: {}", ex.getMessage());
            return null;
        }
    }

    private String getResponseBody(HttpResponse response) {
        try {
            HttpEntity entity = response.getEntity();
            response.setEntity(new BufferedHttpEntity(entity)); // буферизуем тело ответа
            InputStream content = response.getEntity().getContent();
            return getStreamContent(content);
        } catch (Exception ex) {
            logger.warn("An error occurred while getting http response body: {}", ex.getMessage());
            return null;
        }
    }

    private String getStreamContent(InputStream stream) throws IOException {
        stream.mark(MAX_CONTENT_LEN + 1);
        byte[] buffer = new byte[MAX_CONTENT_LEN];
        int bytesRead = stream.read(buffer);
        stream.reset();
        if (bytesRead < 0) {
            return null;
        }
        String text = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        if (bytesRead >= MAX_CONTENT_LEN) {
            text += "... ";
        }
        return text;
    }
}
