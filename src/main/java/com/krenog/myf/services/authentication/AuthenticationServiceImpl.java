package com.krenog.myf.services.authentication;

import com.krenog.myf.entities.User;
import com.krenog.myf.security.detail.UserPrincipal;
import com.krenog.myf.security.jwt.JwtProvider;
import com.krenog.myf.services.authentication.codegenerator.RandomSmsCodeGenerator;
import com.krenog.myf.services.authentication.config.AuthenticationConfig;
import com.krenog.myf.services.authentication.exceptions.CodeDoesNotExistException;
import com.krenog.myf.services.authentication.exceptions.InvalidVerificationCodeException;
import com.krenog.myf.services.authentication.exceptions.NumberCodeAttemptsException;
import com.krenog.myf.services.cache.CacheService;
import com.krenog.myf.services.sms.SmsService;
import com.krenog.myf.services.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import static com.krenog.myf.services.authentication.config.Constants.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);
    private final UserService userService;
    private final CacheService cacheService;
    private final SmsService smsService;
    private final AuthenticationConfig authenticationConfig;
    private final JwtProvider jwtProvider;

    public AuthenticationServiceImpl(UserService userService, CacheService cacheService, SmsService smsService, AuthenticationConfig authenticationConfig, JwtProvider jwtProvider) {
        this.userService = userService;
        this.cacheService = cacheService;
        this.smsService = smsService;
        this.authenticationConfig = authenticationConfig;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void sendSmsCode(String phone) {
        Boolean isTrafficEnabled = authenticationConfig.isTrafficEnabled() && !phone.equals(authenticationConfig.getPhone());
        String code = generateCode(isTrafficEnabled);
        saveCodeInCache(code, phone);
        if (Boolean.TRUE.equals(isTrafficEnabled)) {
            smsService.sendSms(phone, code);
        }
    }

    private String generateCode(Boolean isTrafficEnabled) {
        String code = authenticationConfig.getTestCode();
        if (Boolean.TRUE.equals(isTrafficEnabled)) {
            code = RandomSmsCodeGenerator.generateCode(authenticationConfig.getLength());
        }
        return code;
    }

    private void saveCodeInCache(String code, String phone) {
        //save code for phone in cache
        cacheService.setValue(phone, code);
        //save count code try
        String codeTryKey = phone + CACHE_COUNT_STRING;
        cacheService.setValue(codeTryKey, START_COUNT_TRY_VALUE);
    }

    public AuthenticationData findUserAndAuthenticate(String phoneNumber, String code) {
        User user = userService.getUserByPhoneNumber(phoneNumber);
        return authentication(user, code);
    }

    public AuthenticationData createUserAndAuthenticate(String phoneNumber, String username, String code) {
        User user = userService.createUser(phoneNumber, username);
        return authentication(user, code);
    }

    private AuthenticationData authentication(User user, String code) {
        validateSmsCode(user.getPhoneNumber(), code);
        userService.updateLastLogin(user);
        String token = getToken(user);
        return new AuthenticationData(token, user);
    }

    private void validateSmsCode(String phoneNumber, String code) {
        String cacheCode = getCodeFromCache(phoneNumber);
        compareUserCodeWithCache(phoneNumber, code, cacheCode);
        deleteCodeFromCache(phoneNumber);
    }

    private String getCodeFromCache(String phoneNumber) {
        return cacheService.getValue(phoneNumber);
    }

    private void compareUserCodeWithCache(String phoneNumber, String userCode, String cacheCode) {
        if (cacheCode == null) {
            throw new CodeDoesNotExistException("Code does not exist");
        }
        checkCodeCountTry(phoneNumber);
        if (!userCode.equals(cacheCode)) {
            throw new InvalidVerificationCodeException("Invalid Verification Code");
        }
    }

    private void checkCodeCountTry(String phone) {
        try {
            String codeTryKey = phone + CACHE_COUNT_STRING;
            cacheService.incrementValue(codeTryKey);
            Integer count = Integer.valueOf(cacheService.getValue(codeTryKey));
            if (count >= MAX_COUNT_TRY_VALUE) {
                throw new NumberCodeAttemptsException("Number code attempts");
            }
        } catch (NullPointerException | NumberFormatException e) {
            logger.error("Checking sms code attempts error, message: {0}", e);
            throw new NumberCodeAttemptsException("Checking sms code attempts error");
        }
    }

    private void deleteCodeFromCache(String phoneNumber) {
        cacheService.deleteValue(phoneNumber);
        cacheService.deleteValue(phoneNumber + CACHE_COUNT_STRING);
    }

    private String getToken(User user) {
        return jwtProvider.generateJwtToken(UserPrincipal.build(user));
    }
}
