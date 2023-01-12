package com.example.security.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
    private boolean isAdmin;
}
