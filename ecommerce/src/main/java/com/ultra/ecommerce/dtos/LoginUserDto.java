package com.ultra.ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginUserDto {
    @NotBlank(message = "Please provide a email")
    private String email;
    @NotBlank(message = "Please provide a password")
    private String password;
}
