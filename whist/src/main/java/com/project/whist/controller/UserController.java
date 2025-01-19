
package com.project.whist.controller;

import com.project.whist.dto.response.UserResponseDto;
import com.project.whist.model.User;
import com.project.whist.service.UserService;
import com.project.whist.dto.response.UserAuthorizeResponseDto;
import com.project.whist.dto.request.UserRequestDto;
import com.project.whist.dto.response.UserTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{userId}")
    @CrossOrigin
    public UserResponseDto getUserById(@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }

    @GetMapping("/user/all")
    @CrossOrigin
    public List<UserResponseDto> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user")
    @CrossOrigin
    public List<UserResponseDto> getUsersNameContains(@RequestParam String name) {
        return userService.getUsersNameContains(name);
    }

    @PostMapping("/user/register")
    @CrossOrigin
    public void registerNewUser(@RequestBody UserRequestDto userRequestDto) {
        userService.registerNewUser(userRequestDto);
    }

    @PostMapping("/user/authenticate")
    @CrossOrigin
    public UserTokenResponseDto login(@RequestBody UserRequestDto userRequestDto) {
        return userService.validateUserCredentialsAndGenerateToken(userRequestDto);
    }

    @PostMapping("/user/authorize")
    @CrossOrigin
    public UserAuthorizeResponseDto authorize(@RequestBody UserRequestDto userRequestDto) throws ParseException {
        return userService.authorizeV1(userRequestDto);
    }
}