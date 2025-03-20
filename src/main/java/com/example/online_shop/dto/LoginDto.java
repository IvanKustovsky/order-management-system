package com.example.online_shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "LoginDto", description = "DTO for login credentials including username and password")
public class LoginDto {

    @Schema(description = "Username for login. Email or mobile number", example = "johndoe@gmail.com")
    private String username;

    @Schema(description = "Password for login", example = "password123")
    private String password;
}
