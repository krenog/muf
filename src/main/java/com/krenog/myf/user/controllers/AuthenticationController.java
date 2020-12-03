package com.krenog.myf.user.controllers;

import com.krenog.myf.user.dto.authentication.SendSmsDto;
import com.krenog.myf.user.dto.authentication.SignInRequestDto;
import com.krenog.myf.user.dto.authentication.SignInResponseDto;
import com.krenog.myf.user.dto.authentication.SignUpRequestDto;
import com.krenog.myf.user.services.authentication.AuthenticationData;
import com.krenog.myf.user.services.authentication.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Api(value = "Сервис аутентифкации")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/send-sms-code")
    @ApiOperation(value = "Запрос отправки смс")
    @ApiResponses(value = {
            @ApiResponse(code = 502, message = "sms_traffic_unavailable")
    })
    public void sendSmsCode(@RequestBody @Valid SendSmsDto dto) {
        authenticationService.sendSmsCode(dto.getPhoneNumber());
    }

    @PostMapping(value = "/sign-in")
    @ApiOperation(value = "Запрос входа")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "code_does_not_exist"),
            @ApiResponse(code = 401, message = "invalid_verification_code"),
            @ApiResponse(code = 429, message = "number_code_attempts"),
            @ApiResponse(code = 400, message = "user_not_found"),
    })
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody @Valid SignInRequestDto dto) {
        AuthenticationData authenticationData = authenticationService.signIn(dto);
        return new ResponseEntity<>(new SignInResponseDto(authenticationData), HttpStatus.OK);
    }

    @PostMapping(value = "/sign-up")
    @ApiOperation(value = "Запрос регистрации")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "code_does_not_exist"),
            @ApiResponse(code = 401, message = "invalid_verification_code"),
            @ApiResponse(code = 429, message = "number_code_attempts"),
            @ApiResponse(code = 400, message = "user_not_found"),
    })
    public ResponseEntity<SignInResponseDto> sendSmsCode(@RequestBody @Valid SignUpRequestDto dto) {
        AuthenticationData authenticationData = authenticationService.signUp(dto);
        return new ResponseEntity<>(new SignInResponseDto(authenticationData), HttpStatus.OK);
    }
}
