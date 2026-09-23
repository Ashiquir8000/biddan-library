package com.example.sundorproject.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BorrowRequestDto(
        @NotBlank(message = "ইউজার নির্বাচন করা আবশ্যক")
        String userId,

        @NotBlank(message = "বই নির্বাচন করা আবশ্যক")
        String bookId,

        @NotNull(message = "ফেরত দেওয়ার নির্ধারিত তারিখ দিতে হবে")
        @Future(message = "নির্ধারিত তারিখ অবশ্যই ভবিষ্যতের কোনো দিন হতে হবে")
        LocalDate dueDate
) {
}