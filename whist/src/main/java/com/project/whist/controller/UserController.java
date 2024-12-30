
package com.project.whist.controller;

import com.project.whist.service.UserService;
import com.project.whist.dto.UserAuthorizeResponseDto;
import com.project.whist.dto.UserRequestDto;
import com.project.whist.dto.UserTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserRequestDto getUserById(@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }

    @GetMapping("/user/all")
    @CrossOrigin
    public List<UserRequestDto> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user")
    @CrossOrigin
    public List<UserRequestDto> getUsersNameContains(@RequestParam String name) {
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