package com.krenog.myf.services.sms.smsc;

import com.krenog.myf.services.sms.SmsService;
import com.krenog.myf.services.sms.exceptions.SendSmsException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Service("smsc")
@Primary
public class SmsServiceImpl implements SmsService {
    private static final Logger logger = LogManager.getLogger(SmsServiceImpl.class);
    private final SmscConfig smscConfig;
    private final RestTemplate restTemplate;

    public SmsServiceImpl(SmscConfig smscConfig, RestTemplate restTemplate) {
        this.smscConfig = smscConfig;
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendSms(String phone, String message) {
        try {
            URI uri = new URIBuilder(smscConfig.getUrl())
                    .setCharset(StandardCharsets.UTF_8)
                    .addParameter("login", smscConfig.getLogin())
                    .addParameter("psw", smscConfig.getPassword())
                    .addParameter("phones", phone)
                    .addParameter("mes", message)
                    .build();
            restTemplate.postForObject(uri, null, String.class);
        } catch (URISyntaxException | RestClientException e) {
            logger.error("Send sms message error", e);
            throw new SendSmsException();
        }

    }
}
