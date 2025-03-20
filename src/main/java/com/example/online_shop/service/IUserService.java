package com.example.online_shop.service;

import com.example.online_shop.dto.LoginDto;
import com.example.online_shop.dto.RegisterUserDto;
import com.example.online_shop.dto.UserDto;

public interface IUserService {

    /**
     * Registers a new user based on the provided details.
     *
     * @param registerUserDto - Object containing the user registration information
     */
    void registerUser(RegisterUserDto registerUserDto);

    /**
     * Fetches the details of a user by their email.
     *
     * @param email - The email of the user to fetch
     * @return UserDto - User details including name, email, etc.
     */
    UserDto fetchUser(String email);

    /**
     * Deletes the user associated with the provided email.
     *
     * @param email - The email of the user to be deleted
     */
    void deleteUser(String email);

    /**
     * Logs in the user using the provided login credentials.
     *
     * @param loginDto - Object containing the user's login credentials (email(mobileNumber), password)
     */
    void login(LoginDto loginDto);

    /**
     * Logs out the currently authenticated user.
     */
    void logout();
}
