package com.example.online_shop.controller;

import com.example.online_shop.constants.UserConstants;
import com.example.online_shop.dto.RegisterUserDto;
import com.example.online_shop.dto.ResponseDto;
import com.example.online_shop.dto.UserDto;
import com.example.online_shop.service.IUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@Valid @RequestBody RegisterUserDto userDto) {
        userService.registerUser(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UserConstants.STATUS_201, UserConstants.MESSAGE_201));
    }

    @GetMapping
    public ResponseEntity<UserDto> fetchUser(@RequestParam(name = "email") @Email String email) {
        var userDto = userService.fetchUser(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

    //@PreAuthorize("hasRole('ADMIN')") TODO
    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteUserDetails(@RequestParam(name = "email") @Email String email) {
        userService.deleteUser(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
    }
}
