package com.krenog.myf.controllers;


import com.krenog.myf.dto.user.CheckExistResponseDto;
import com.krenog.myf.dto.user.CheckPhoneNumberRequestDto;
import com.krenog.myf.dto.user.CheckUsernameRequestDto;
import com.krenog.myf.services.user.UserService;
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
    public ResponseEntity checkPhoneNumber(@RequestBody CheckPhoneNumberRequestDto checkUsernameRequestDto) {
        Boolean isUsernameExist = userService.checkPhoneNumberIsExist(checkUsernameRequestDto.getPhoneNumber());
        return new ResponseEntity(new CheckExistResponseDto(isUsernameExist), HttpStatus.OK);
    }

    @PostMapping(value = "/check-username")
    public ResponseEntity checkUsername(@RequestBody CheckUsernameRequestDto checkUsernameRequestDto) {
        Boolean isUsernameExist = userService.checkUsernameIsExist(checkUsernameRequestDto.getUsername());
        return new ResponseEntity(new CheckExistResponseDto(isUsernameExist), HttpStatus.OK);
    }
}
