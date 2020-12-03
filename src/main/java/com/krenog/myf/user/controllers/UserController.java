package com.krenog.myf.user.controllers;


import com.krenog.myf.user.dto.user.*;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.services.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
@Api(value = "Сервис пользователя")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/check-phone")
    @ApiOperation(value = "Проверка существоания телефона")
    public ResponseEntity<CheckExistResponseDto> checkPhoneNumber(@RequestBody @Valid CheckPhoneNumberRequestDto checkPhoneNumberRequestDto) {
        Boolean isPhoneNumber = userService.checkPhoneNumberIsExist(checkPhoneNumberRequestDto.getPhoneNumber());
        return new ResponseEntity<>(new CheckExistResponseDto(isPhoneNumber), HttpStatus.OK);
    }

    @PostMapping(value = "/check-username")
    @ApiOperation(value = "Проверка существования никнейма")
    public ResponseEntity<CheckExistResponseDto> checkUsername(@RequestBody @Valid CheckUsernameRequestDto checkUsernameRequestDto) {
        Boolean isUsernameExist = userService.checkUsernameIsExist(checkUsernameRequestDto.getUsername());
        return new ResponseEntity<>(new CheckExistResponseDto(isUsernameExist), HttpStatus.OK);
    }

    @GetMapping(value = "/find")
    @ApiOperation(value = "Поиск пользователя")
    public ResponseEntity<List<CommonUserDataDto>> findUser(@Valid FindUsersByUsernameParameters findUsersByUsernameDto) {
        List<User> users = userService.findUsersByUsername(findUsersByUsernameDto);
        List<CommonUserDataDto> commonUsersData = users.stream().map(CommonUserDataDto::new).collect(Collectors.toList());
        return new ResponseEntity<>(commonUsersData, HttpStatus.OK);
    }
}
