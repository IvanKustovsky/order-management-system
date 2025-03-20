package com.example.online_shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserShortDto", description = "DTO for minimal user details including ID, name, and email")
public class UserShortDto {

    @Schema(description = "Unique ID of the user", example = "1")
    private Long id;

    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @Schema(description = "Email address of the user", example = "johndoe@example.com")
    private String email;
}
