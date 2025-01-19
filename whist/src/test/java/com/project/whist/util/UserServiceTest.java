package com.project.whist.util;

import com.project.whist.dto.request.UserRequestDto;
import com.project.whist.dto.response.UserResponseDto;
import com.project.whist.model.User;
import com.project.whist.model.UserLogin;
import com.project.whist.repository.UserLoginRepository;
import com.project.whist.repository.UserRepository;
import com.project.whist.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserLoginRepository userLoginRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByUserId_shouldReturnUserResponseDto() {
        Long userId = 1L;
        User user = User.builder().id(userId).username("testUser").email("test@example.com").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.findByUserId(userId);

        assertNotNull(result);
        assertEquals("testUser", result.username());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        User user = User.builder().id(1L).username("testUser").email("test@example.com").build();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        var result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testUser", result.getFirst().username());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUsersNameContains_shouldReturnFilteredUsers() {
        User user = User.builder().id(1L).username("testUser").email("test@example.com").build();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        var result = userService.getUsersNameContains("test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testUser", result.getFirst().username());
    }

    @Test
    void registerNewUser_shouldSaveUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("newUser");
        userRequestDto.setPassword("password123");
        userRequestDto.setEmail("newuser@example.com");

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        userService.registerNewUser(userRequestDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void validateUserCredentialsAndGenerateToken_shouldReturnTokenResponseDto() {
        User user = User.builder().username("testUser").password("encodedPassword").build();
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("testUser");
        userRequestDto.setPassword("password123");

        when(userRepository.findUserByUsername("testUser")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        var result = userService.validateUserCredentialsAndGenerateToken(userRequestDto);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userLoginRepository, times(1)).save(any(UserLogin.class));
    }

    @Test
    void authorizeV1_shouldReturnAuthorizationResponse() throws Exception {
        UserLogin userLogin = UserLogin.builder()
                .token("token123")
                .tokenExpireTime("2099-12-31 23:59:59")
                .build();

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("testUser");
        userRequestDto.setToken("token123");

        when(userLoginRepository.findByUserAndToken("testUser", "token123")).thenReturn(userLogin);

        var result = userService.authorizeV1(userRequestDto);

        assertNotNull(result);
        assertTrue(result.isValid());
    }

    @Test
    void authorizeV1_shouldReturnFalseForExpiredToken() throws Exception {
        UserLogin userLogin = UserLogin.builder()
                .token("token123")
                .tokenExpireTime("2000-01-01 00:00:00")
                .build();

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("testUser");
        userRequestDto.setToken("token123");

        when(userLoginRepository.findByUserAndToken("testUser", "token123")).thenReturn(userLogin);

        var result = userService.authorizeV1(userRequestDto);

        assertNotNull(result);
        assertFalse(result.isValid());
    }
}
