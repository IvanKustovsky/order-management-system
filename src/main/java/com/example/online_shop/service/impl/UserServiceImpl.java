package com.example.online_shop.service.impl;

import com.example.online_shop.constants.RoleConstants;
import com.example.online_shop.dto.LoginDto;
import com.example.online_shop.dto.RegisterUserDto;
import com.example.online_shop.dto.UserDto;
import com.example.online_shop.exception.ResourceNotFoundException;
import com.example.online_shop.exception.UserAlreadyExistsException;
import com.example.online_shop.mapper.UserMapper;
import com.example.online_shop.repository.UserRepository;
import com.example.online_shop.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public void registerUser(RegisterUserDto registerUserDto) {
        log.info("Registering user with email: {}", registerUserDto.getEmail());
        checkIfUserExists(registerUserDto.getEmail(), registerUserDto.getMobileNumber());

        var user = UserMapper.INSTANCE.toEntity(registerUserDto);
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setRole(RoleConstants.USER_ROLE);

        userRepository.save(user);
        log.info("User registered successfully with email: {}", registerUserDto.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto fetchUser(String email) {
        log.info("Fetching user by email: {}", email);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        log.info("User found: {}", user.getEmail());
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        log.info("Deleting user with email: {}", email);
        var existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        userRepository.deleteById(existingUser.getId());
        log.info("User deleted successfully with email: {}", email);
    }

    @Override
    public void login(LoginDto loginDto) {
        log.info("User attempting to log in: {}", loginDto.getUsername());
        var token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        var authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User logged in successfully: {}", loginDto.getUsername());
    }

    @Override
    public void logout() {
        log.info("User logging out");
        SecurityContextHolder.getContext().setAuthentication(null);
        log.info("User logged out successfully");
    }

    private void checkIfUserExists(String email, String mobileNumber) {
        log.debug("Checking if user exists with email: {} or mobile number: {}", email, mobileNumber);
        checkIfUserExistsByEmail(email);
        checkIfUserExistsByMobileNumber(mobileNumber);
    }

    private void checkIfUserExistsByEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    log.warn("User already exists with email: {}", email);
                    throw new UserAlreadyExistsException("User already registered with provided email");
                });
    }

    private void checkIfUserExistsByMobileNumber(String mobileNumber) {
        userRepository.findByMobileNumber(mobileNumber)
                .ifPresent(user -> {
                    log.warn("User already exists with mobile number: {}", mobileNumber);
                    throw new UserAlreadyExistsException("User already registered with provided mobile number");
                });
    }
}