package com.example.online_shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@Schema(name = "Register User", description = "Schema to hold User registration information")
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    @Schema(description = "Name of the user", example = "John Doe")
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Schema(description = "Email of the user", example = "john.doe@example.com")
    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Mobile number of the user", example = "+380973958378")
    @NotEmpty(message = "MobileNumber cannot be null or empty")
    @Pattern(regexp = "^\\+380\\d{9}$", message = "MobileNumber must start with +380 and contain 9 digits after the country code")
    private String mobileNumber;

    @Schema(description = "Password of the user", example = "password123")
    @NotEmpty(message = "Password cannot be null or empty")
    @Size(min = 5, message = "Password must be at least 5 symbols")
    private String password;
}
