package com.krenog.myf.controllers;

import com.krenog.myf.dto.authentication.SendSmsDto;
import com.krenog.myf.dto.authentication.SignInRequestDto;
import com.krenog.myf.dto.authentication.SignInResponseDto;
import com.krenog.myf.services.authentication.AuthenticationData;
import com.krenog.myf.services.authentication.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/send-sms-code")
    public void sendSmsCode(@RequestBody SendSmsDto dto) {
        authenticationService.sendSmsCode(dto.getPhoneNumber());
    }

    @PostMapping(value = "/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto dto) {
        AuthenticationData data = authenticationService.findUserAndAuthenticate(dto.getPhoneNumber(), dto.getCode());
        SignInResponseDto signInResponseDto = new SignInResponseDto(data);
        return new ResponseEntity<>(signInResponseDto, HttpStatus.OK);
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<SignInResponseDto> sendSmsCode(@RequestBody SignInRequestDto dto) {
        AuthenticationData data = authenticationService.findUserAndAuthenticate(dto.getPhoneNumber(), dto.getCode());
        SignInResponseDto signInResponseDto = new SignInResponseDto(data);
        return new ResponseEntity<>(signInResponseDto, HttpStatus.OK);
    }
}
