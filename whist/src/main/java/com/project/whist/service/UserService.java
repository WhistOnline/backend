
package com.project.whist.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.whist.model.User;
import com.project.whist.model.UserLogin;
import com.project.whist.repository.UserLoginRepository;
import com.project.whist.repository.UserRepository;
import com.project.whist.dto.response.UserAuthorizeResponseDto;
import com.project.whist.dto.request.UserRequestDto;
import com.project.whist.dto.response.UserTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRequestDto findByUserId(Long userId) {

        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow();

        return UserRequestDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public List<UserRequestDto> getAllUsers() {

        return userRepository.findAll().stream()
                .map(user -> UserRequestDto.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .collect(Collectors.toList());

    }

    public List<UserRequestDto> getUsersNameContains(final String name) {

        return userRepository.findAll().stream()
                .filter(user -> user.getUsername().contains(name))
                .map(user -> UserRequestDto.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .collect(Collectors.toList());

    }


    public void registerNewUser(UserRequestDto userRequestDto) {
        User user = User.builder()
                .username(userRequestDto.getUsername())
                .password(passwordEncoder.encode(userRequestDto.getPassword())) // Hash the password
                .email(userRequestDto.getEmail())
                .createdAt(new Date().toInstant())
                .build();

        userRepository.save(user);
    }


    public String getCurrentTimeStamp() {
        Date newDate = DateUtils.addHours(new Date(), 3);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
    }

    public static String createJsonWebToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer("auth0")
                .withExpiresAt(DateUtils.addHours(new Date(), 3))
                .sign(Algorithm.HMAC256("secret"));
    }


    public UserTokenResponseDto validateUserCredentialsAndGenerateToken(UserRequestDto userRequestDto) {

        User user = userRepository.findUserByUsername(userRequestDto.getUsername());

        if (user != null && passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) { // Verify the hashed password

            String token = createJsonWebToken(userRequestDto.getUsername());

            UserLogin userLogin = UserLogin.builder()
                    .user(user)
                    .token(token)
                    .tokenExpireTime(getCurrentTimeStamp())
                    .build();
            userLoginRepository.save(userLogin);

            UserTokenResponseDto userTokenResponseDto = new UserTokenResponseDto();
            userTokenResponseDto.setToken(token);
            userTokenResponseDto.setUsername(userRequestDto.getUsername());

            return userTokenResponseDto;
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    public UserAuthorizeResponseDto authorizeV1(UserRequestDto userRequestDto) throws ParseException {
        UserLogin userLogin = userLoginRepository.findByUserAndToken(userRequestDto.getUsername(), userRequestDto.getToken());

        // check user-login in database
        if (userLogin != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(userLogin.getTokenExpireTime());

            // check if token expired
            if (new Date().compareTo(date) <1) {
                return new UserAuthorizeResponseDto(userRequestDto.getUsername(), true);
            } else {
                return new UserAuthorizeResponseDto(userRequestDto.getUsername(), false);
            }
        }
        return new UserAuthorizeResponseDto(userRequestDto.getUsername(), false);
    }
}
 