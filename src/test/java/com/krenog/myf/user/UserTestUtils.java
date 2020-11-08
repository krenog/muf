package com.krenog.myf.user;

import com.krenog.myf.user.dto.authentication.SendSmsDto;
import com.krenog.myf.user.dto.authentication.SignInRequestDto;
import com.krenog.myf.user.dto.authentication.SignUpRequestDto;
import com.krenog.myf.user.dto.user.CheckPhoneNumberRequestDto;
import com.krenog.myf.user.dto.user.CheckUsernameRequestDto;
import com.krenog.myf.user.services.authentication.AuthenticationData;

import static com.krenog.myf.utils.TestUtils.*;

public class UserTestUtils {

    public static final String TEST_VALUE = "0";
    public static final String TEST_CODE = "389153";
    public static final String TEST_CACHE_COUNT_STRING = "_count_try_code";
    public static final String TEST_CODE_TRY_KEY = TEST_PHONE_NUMBER + TEST_CACHE_COUNT_STRING;
    public static final String TEST_START_COUNT_TRY_VALUE = "0";
    public static final String TEST_TOKEN = "token";
    public static final SignInRequestDto SIGN_IN_REQUEST_DTO;
    public static final SignUpRequestDto SIGN_UP_REQUEST_DTO;
    public static final SendSmsDto SEND_SMS_DTO;
    public static final AuthenticationData AUTHENTICATION_DATA;
    public static final CheckPhoneNumberRequestDto CHECK_PHONE_NUMBER_REQUEST_DTO;
    public static final CheckUsernameRequestDto CHECK_USERNAME_REQUEST_DTO;

    static {
        SIGN_IN_REQUEST_DTO = new SignInRequestDto(TEST_PHONE_NUMBER, TEST_CODE);

        SIGN_UP_REQUEST_DTO = new SignUpRequestDto(TEST_PHONE_NUMBER, TEST_CODE, TEST_USERNAME);

        SEND_SMS_DTO = new SendSmsDto(TEST_PHONE_NUMBER);

        AUTHENTICATION_DATA = new AuthenticationData();
        AUTHENTICATION_DATA.setId(TEST_ID);
        AUTHENTICATION_DATA.setPhoneNumber(TEST_PHONE_NUMBER);
        AUTHENTICATION_DATA.setToken(TEST_TOKEN);
        AUTHENTICATION_DATA.setUsername(TEST_USERNAME);

        CHECK_PHONE_NUMBER_REQUEST_DTO = new CheckPhoneNumberRequestDto();
        CHECK_PHONE_NUMBER_REQUEST_DTO.setPhoneNumber(TEST_PHONE_NUMBER);
        CHECK_USERNAME_REQUEST_DTO = new CheckUsernameRequestDto();
        CHECK_USERNAME_REQUEST_DTO.setUsername(TEST_USERNAME);
    }

}
