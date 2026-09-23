package com.example.sundorproject.DTO;

import com.example.sundorproject.model.Role;
import java.time.LocalDateTime;

public record UserResponseDto(
        String id,
        String name,
        String email,
        Role role,
        String phone,
        boolean active,
        LocalDateTime createdAt
) {
}