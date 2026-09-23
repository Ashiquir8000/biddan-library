package com.example.sundorproject.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationDto(
        @NotBlank(message = "নাম খালি রাখা যাবে না")
        @Size(min = 2, max = 50, message = "নাম ২ থেকে ৫০ অক্ষরের মধ্যে হতে হবে")
        String name,

        @NotBlank(message = "ইমেইল প্রদান করা আবশ্যক")
        @Email(message = "সঠিক ইমেইল ফরম্যাট দিন")
        String email,

        @NotBlank(message = "পাসওয়ার্ড খালি রাখা যাবে না")
        @Size(min = 6, message = "পাসওয়ার্ড কমপক্ষে ৬ অক্ষরের হতে হবে")
        String password,

        @NotBlank(message = "ফোন নম্বর দেওয়া আবশ্যক")
        @Pattern(regexp = "^(?:\\+88|88)?01[3-9]\\d{8}$", message = "সঠিক ফোন নম্বর দিন")
        String phone
) {
}