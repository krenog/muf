package com.krenog.myf.user.services.authentication;

import com.krenog.myf.user.dto.authentication.SignInRequestDto;
import com.krenog.myf.user.dto.authentication.SignUpRequestDto;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.security.detail.UserPrincipal;
import com.krenog.myf.user.security.jwt.JwtProvider;
import com.krenog.myf.user.services.authentication.codegenerator.RandomSmsCodeGenerator;
import com.krenog.myf.user.services.authentication.config.AuthenticationConfig;
import com.krenog.myf.user.services.authentication.exceptions.CodeDoesNotExistException;
import com.krenog.myf.user.services.authentication.exceptions.InvalidVerificationCodeException;
import com.krenog.myf.user.services.authentication.exceptions.NumberCodeAttemptsException;
import com.krenog.myf.user.services.cache.CacheService;
import com.krenog.myf.user.services.sms.SmsService;
import com.krenog.myf.user.services.user.CreateUserData;
import com.krenog.myf.user.services.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import static com.krenog.myf.user.services.authentication.config.Constants.*;

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
        Boolean isTrafficEnabled = isTrafficEnabledForPhone(phone);
        String code;
        if (isTrafficEnabled) {
            code = generateCodeAndSend(phone);
        } else {
            code = authenticationConfig.getTestCode();
        }
        saveCodeAndTryCountInCache(code, phone);
    }

    private Boolean isTrafficEnabledForPhone(String phone) {
        return authenticationConfig.isTrafficEnabled() && !phone.equals(authenticationConfig.getPhone());
    }

    private String generateCodeAndSend(String phone) {
        String code = RandomSmsCodeGenerator.generateCode(authenticationConfig.getLength());
        smsService.sendSms(phone, code);
        return code;
    }

    private void saveCodeAndTryCountInCache(String code, String phone) {
        saveCodeInCache(code, phone);
        saveCodeTryCount(phone);
    }

    private void saveCodeInCache(String code, String phone) {
        cacheService.setValue(phone, code);
    }

    private void saveCodeTryCount(String phone) {
        cacheService.setValue(getCodeTryKey(phone), START_COUNT_TRY_VALUE);
    }

    public AuthenticationData signIn(SignInRequestDto signInRequestDto) {
        validateAndDeleteSmsCode(signInRequestDto.getPhoneNumber(), signInRequestDto.getCode());
        User user = userService.getUserByPhoneNumber(signInRequestDto.getPhoneNumber());
        userService.updateLastLogin(user);
        return buildAuthenticationData(user);
    }

    public AuthenticationData signUp(SignUpRequestDto signUpRequestDto) {
        validateAndDeleteSmsCode(signUpRequestDto.getPhoneNumber(), signUpRequestDto.getCode());
        User user = userService.createUser(new CreateUserData(signUpRequestDto.getUsername(), signUpRequestDto.getPhoneNumber()));
        return buildAuthenticationData(user);
    }

    private void validateAndDeleteSmsCode(String phoneNumber, String code) {
        String cacheCode = getCodeFromCache(phoneNumber);
        checkAndIncrementCodeCountTry(phoneNumber);
        compareUserCodeWithCache(code, cacheCode);
        deleteCodeFromCache(phoneNumber);
    }

    private String getCodeFromCache(String phoneNumber) {
        String cacheCode = cacheService.getValue(phoneNumber);
        if (cacheCode != null) {
            return cacheCode;
        } else {
            throw new CodeDoesNotExistException("Code does not exist");
        }
    }

    private void checkAndIncrementCodeCountTry(String phone) {
        try {
            incrementCodeTry(phone);
            checkCountTryValue(phone);
        } catch (NullPointerException | NumberFormatException e) {
            logger.error("Checking sms code attempts error, message: {0}", e);
            throw new NumberCodeAttemptsException("Checking sms code attempts error");
        }
    }

    private void incrementCodeTry(String phone) {
        cacheService.incrementValue(getCodeTryKey(phone));
    }

    private void checkCountTryValue(String phone) {
        int count = Integer.parseInt(cacheService.getValue(getCodeTryKey(phone)));
        if (count >= MAX_COUNT_TRY_VALUE) {
            throw new NumberCodeAttemptsException("Number code attempts");
        }
    }

    private String getCodeTryKey(String phone) {
        return phone + CACHE_COUNT_STRING;
    }

    private void compareUserCodeWithCache(String userCode, String cacheCode) {
        if (!userCode.equals(cacheCode)) {
            throw new InvalidVerificationCodeException("Invalid Verification Code");
        }
    }

    private void deleteCodeFromCache(String phoneNumber) {
        cacheService.deleteValue(phoneNumber);
        cacheService.deleteValue(getCodeTryKey(phoneNumber));
    }

    private AuthenticationData buildAuthenticationData(User user) {
        String token = generateToken(user);
        return new AuthenticationData(token, user);
    }

    private String generateToken(User user) {
        return jwtProvider.generateJwtToken(UserPrincipal.build(user));
    }
}
