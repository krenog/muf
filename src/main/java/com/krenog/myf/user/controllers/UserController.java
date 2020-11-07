package com.krenog.myf.user.controllers;


import com.krenog.myf.user.dto.user.CheckExistResponseDto;
import com.krenog.myf.user.dto.user.CheckPhoneNumberRequestDto;
import com.krenog.myf.user.dto.user.CheckUsernameRequestDto;
import com.krenog.myf.user.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/check-phone")
    public ResponseEntity<CheckExistResponseDto> checkPhoneNumber(@RequestBody CheckPhoneNumberRequestDto checkPhoneNumberRequestDto) {
        Boolean isPhoneNumber = userService.checkPhoneNumberIsExist(checkPhoneNumberRequestDto.getPhoneNumber());
        return new ResponseEntity<>(new CheckExistResponseDto(isPhoneNumber), HttpStatus.OK);
    }

    @PostMapping(value = "/check-username")
    public ResponseEntity<CheckExistResponseDto> checkUsername(@RequestBody CheckUsernameRequestDto checkUsernameRequestDto) {
        Boolean isUsernameExist = userService.checkUsernameIsExist(checkUsernameRequestDto.getUsername());
        return new ResponseEntity<>(new CheckExistResponseDto(isUsernameExist), HttpStatus.OK);
    }
}
