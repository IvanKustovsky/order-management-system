package com.example.online_shop.service.impl;

import com.example.online_shop.dto.RegisterUserDto;
import com.example.online_shop.dto.UserDto;
import com.example.online_shop.entity.UserEntity;
import com.example.online_shop.exception.ResourceNotFoundException;
import com.example.online_shop.exception.UserAlreadyExistsException;
import com.example.online_shop.repository.UserRepository;
import com.example.online_shop.service.impl.UserServiceImpl;
import com.example.online_shop.mapper.UserMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User Service Test Class")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @Order(1)
    @DisplayName("Should register user successfully when user does not exist")
    void shouldRegisterUserSuccessfully() {
        // given
        var registerUserDto = RegisterUserDto.builder()
                .email("test@example.com")
                .mobileNumber("123456789")
                .password("password")
                .build();

        when(userRepository.findByEmail(registerUserDto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByMobileNumber(registerUserDto.getMobileNumber())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("encodedPassword");

        // when
        userService.registerUser(registerUserDto);

        // then
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @Order(2)
    @DisplayName("Should throw UserAlreadyExistsException when registering user with existing email")
    void shouldThrowExceptionWhenRegisterUserWithExistingEmail() {
        // given
        var registerUserDto = RegisterUserDto.builder()
                .email("existing@example.com")
                .mobileNumber("123456789")
                .password("password")
                .build();

        when(userRepository.findByEmail(registerUserDto.getEmail()))
                .thenReturn(Optional.of(new UserEntity()));

        // when & then
        assertThatThrownBy(() -> userService.registerUser(registerUserDto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already registered with provided email");

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @Order(3)
    @DisplayName("Should throw UserAlreadyExistsException when registering user with existing mobile number")
    void shouldThrowExceptionWhenRegisterUserWithExistingMobileNumber() {
        // given
        var registerUserDto = RegisterUserDto.builder()
                .email("test@example.com")
                .mobileNumber("123456789")
                .password("password")
                .build();

        when(userRepository.findByEmail(registerUserDto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByMobileNumber(registerUserDto.getMobileNumber()))
                .thenReturn(Optional.of(new UserEntity()));

        // when & then
        assertThatThrownBy(() -> userService.registerUser(registerUserDto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already registered with provided mobile number");

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @Order(4)
    @DisplayName("Should fetch user successfully by email")
    void shouldFetchUserSuccessfully() {
        // given
        var email = "test@example.com";
        var userEntity = UserEntity.builder()
                .id(1L)
                .email(email)
                .name("Test User")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // when
        var result = userService.fetchUser(email);

        // then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("Test User", result.getName());
    }

    @Test
    @Order(5)
    @DisplayName("Should throw ResourceNotFoundException when fetching non-existent user")
    void shouldThrowExceptionWhenFetchingNonExistentUser() {
        // given
        var email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.fetchUser(email))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");
    }

    @Test
    @Order(6)
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // given
        var email = "test@example.com";
        var userEntity = UserEntity.builder()
                .id(1L)
                .email(email)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // when
        userService.deleteUser(email);

        // then
        verify(userRepository, times(1)).deleteById(userEntity.getId());
    }

    @Test
    @Order(7)
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        // given
        var email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.deleteUser(email))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");
    }
}