package com.example.enrollment.dto.response;

import com.example.enrollment.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private Role role;
}
