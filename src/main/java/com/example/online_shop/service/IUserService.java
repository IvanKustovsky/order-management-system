package com.example.online_shop.service;

import com.example.online_shop.dto.RegisterUserDto;
import com.example.online_shop.dto.UserDto;

public interface IUserService {

    /**
     * @param registerUserDto - RegisterUserDto Object
     */
    void registerUser(RegisterUserDto registerUserDto);

    /**
     *
     * @param email - Input Email
     * @return User details based on a given email
     */
    UserDto fetchUser(String email);

    /**
     *
     * @param email - Input Email
     */
    void deleteUser(String email);
}
