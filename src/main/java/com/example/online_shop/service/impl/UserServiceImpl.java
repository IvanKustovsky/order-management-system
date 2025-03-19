package com.example.online_shop.service.impl;

import com.example.online_shop.constants.RoleConstants;
import com.example.online_shop.dto.RegisterUserDto;
import com.example.online_shop.dto.UserDto;
import com.example.online_shop.exception.ResourceNotFoundException;
import com.example.online_shop.exception.UserAlreadyExistsException;
import com.example.online_shop.mapper.UserMapper;
import com.example.online_shop.repository.UserRepository;
import com.example.online_shop.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerUser(RegisterUserDto registerUserDto) {
        checkIfUserExists(registerUserDto.getEmail(), registerUserDto.getMobileNumber());

        var user = UserMapper.INSTANCE.toEntity(registerUserDto);
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setRole(RoleConstants.USER_ROLE);

        userRepository.save(user);
    }

    @Override
    public UserDto fetchUser(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        var existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        userRepository.deleteById(existingUser.getId());
    }

    private void checkIfUserExists(String email, String mobileNumber) {
        checkIfUserExistsByEmail(email);
        checkIfUserExistsByMobileNumber(mobileNumber);
    }

    private void checkIfUserExistsByEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User already registered with provided email");
                });
    }

    private void checkIfUserExistsByMobileNumber(String mobileNumber) {
        userRepository.findByMobileNumber(mobileNumber)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User already registered with provided mobile number");
                });
    }
}
