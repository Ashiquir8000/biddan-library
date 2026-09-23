package com.example.sundorproject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDto(
        String id,

        @NotBlank(message = "ক্যাটাগরির নাম খালি রাখা যাবে না")
        @Size(min = 2, max = 50, message = "ক্যাটাগরির নাম ২ থেকে ৫০ অক্ষরের মধ্যে হতে হবে")
        String name,

        @Size(max = 255, message = "বিবরণ সর্বোচ্চ ২৫৫ অক্ষরের হতে পারে")
        String description
) {
}