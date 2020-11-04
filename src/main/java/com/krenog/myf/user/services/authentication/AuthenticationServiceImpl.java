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
import com.krenog.myf.user.services.authentication.exceptions.UserAlreadyExistException;
import com.krenog.myf.user.services.cache.CacheService;
import com.krenog.myf.user.services.sms.SmsService;
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

    public AuthenticationData signIn(SignInRequestDto signInRequestDto) {
        validateSmsCode(signInRequestDto.getPhoneNumber(), signInRequestDto.getCode());
        User user = userService.getUserByPhoneNumber(signInRequestDto.getPhoneNumber());
        userService.updateLastLogin(user);
        return buildAuthenticationData(user);
    }

    public AuthenticationData signUp(SignUpRequestDto signUpRequestDto) {
        validateSmsCode(signUpRequestDto.getPhoneNumber(), signUpRequestDto.getCode());
        checkUserInfo(signUpRequestDto);
        User user = userService.createUser(signUpRequestDto.getPhoneNumber(), signUpRequestDto.getUsername());
        return buildAuthenticationData(user);
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
            int count = Integer.parseInt(cacheService.getValue(codeTryKey));
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

    private void checkUserInfo(SignUpRequestDto signUpRequestDto) {
        Boolean phoneExist = userService.checkPhoneNumberIsExist(signUpRequestDto.getPhoneNumber());
        Boolean usernameExist = userService.checkUsernameIsExist(signUpRequestDto.getUsername());
        if (phoneExist || usernameExist) {
            throw new UserAlreadyExistException();
        }
    }

    private AuthenticationData buildAuthenticationData(User user) {
        String token = getToken(user);
        return new AuthenticationData(token, user);
    }

    private String getToken(User user) {
        return jwtProvider.generateJwtToken(UserPrincipal.build(user));
    }
}
