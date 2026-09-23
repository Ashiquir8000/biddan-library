package com.example.sundorproject.DTO;

import com.example.sundorproject.model.BorrowStatus;
import java.time.LocalDate;

public record BorrowRecordResponseDto(
        String id,
        String userName,
        String userEmail,
        String bookTitle,
        String bookIsbn,
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate,
        BorrowStatus status
) {
}