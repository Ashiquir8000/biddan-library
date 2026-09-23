package com.example.sundorproject.service;

import com.example.sundorproject.DTO.UserRegistrationDto;
import com.example.sundorproject.DTO.UserResponseDto;
import com.example.sundorproject.model.Role;
import com.example.sundorproject.model.User;
import com.example.sundorproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("ইমেইলটি ইতোমধ্যে ব্যবহৃত হচ্ছে!");
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .phone(dto.phone())
                .role(Role.ROLE_USER)
                .active(true)
                .build();

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    public List<UserResponseDto> getAllActiveUsers() {
        return userRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public User getUserEntity(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ইউজার পাওয়া যায়নি!"));
    }

    private UserResponseDto mapToResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhone(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}