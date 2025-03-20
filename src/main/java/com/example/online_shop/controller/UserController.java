package com.example.online_shop.controller;

import com.example.online_shop.constants.UserConstants;
import com.example.online_shop.dto.*;
import com.example.online_shop.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "User Management",
        description = "APIs for managing users in Order Management System"
)
@RestController
@RequestMapping(path = "/users/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "${cross-origin.allowed-origin:http://localhost:3000}")
public class UserController {

    private final IUserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account in the Order Management System"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "User already registered with the credentials"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@Valid @RequestBody RegisterUserDto userDto) {
        userService.registerUser(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UserConstants.STATUS_201, UserConstants.MESSAGE_201));
    }

    @Operation(
            summary = "User login",
            description = "Authenticates a user and starts a session"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully logged in"),
            @ApiResponse(responseCode = "401", description = "User Unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> loginUser(@Valid @RequestBody LoginDto loginDto) {
        userService.login(loginDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
    }

    @Operation(
            summary = "User logout",
            description = "Ends the user session"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully logged out"),
            @ApiResponse(responseCode = "401", description = "User Unauthorized")
    })
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logoutUser() {
        userService.logout();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
    }

    @Operation(
            summary = "Fetch user details",
            description = "Retrieves user details by email"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<UserDto> fetchUser(@RequestParam(name = "email") @Email String email) {
        var userDto = userService.fetchUser(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

    @Operation(
            summary = "Delete user account",
            description = "Deletes a user account by email"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto> deleteUserDetails(@RequestParam(name = "email") @Email String email) {
        userService.deleteUser(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
    }
}
