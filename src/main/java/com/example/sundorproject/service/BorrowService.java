package com.example.sundorproject.service;

import com.example.sundorproject.DTO.BorrowRecordResponseDto;
import com.example.sundorproject.DTO.BorrowRequestDto;
import com.example.sundorproject.model.Book;
import com.example.sundorproject.model.BorrowRecord;
import com.example.sundorproject.model.BorrowStatus;
import com.example.sundorproject.model.User;
import com.example.sundorproject.repository.BorrowRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    public BorrowRecordResponseDto issueBook(BorrowRequestDto dto) {
        User user = userService.getUserEntity(dto.userId());

        // ১. ডাবল ইস্যু চেক: ইউজার এই বইটি বর্তমানে ধার নিয়ে রেখেছে কি না
        boolean alreadyBorrowed = borrowRecordRepository.findByUserAndStatus(user, BorrowStatus.BORROWED)
                .stream()
                .anyMatch(record -> record.getBook().getId().equals(dto.bookId()));
        if (alreadyBorrowed) {
            throw new IllegalStateException("ইউজার ইতিমধ্যে এই বইটি ধার নিয়ে রেখেছেন এবং এখনও ফেরত দেননি!");
        }

        // ২. কনকারেন্ট চেকআউট হ্যান্ডলিং: অ্যাটমিক শর্তযুক্ত আপডেট (availableCopies > 0 হতে হবে)
        Query query = new Query(Criteria.where("id").is(dto.bookId()).and("availableCopies").gt(0));
        Update update = new Update().inc("availableCopies", -1);
        Book updatedBook = mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                Book.class
        );

        if (updatedBook == null) {
            throw new IllegalStateException("দুঃখিত, এই বইটির কোনো কপি বর্তমানে অবশিষ্ট নেই!");
        }

        BorrowRecord record = BorrowRecord.builder()
                .user(user)
                .book(updatedBook)
                .borrowDate(LocalDate.now())
                .dueDate(dto.dueDate())
                .status(BorrowStatus.BORROWED)
                .build();

        return mapToDto(borrowRecordRepository.save(record));
    }

    public BorrowRecordResponseDto returnBook(String recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("ধারের রেকর্ড পাওয়া যায়নি!"));

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new IllegalStateException("বইটি ইতিমধ্যে ফেরত দেওয়া হয়েছে!");
        }

        // বইয়ের স্টকে অ্যাটমিকালি ১ যোগ
        Query query = new Query(Criteria.where("id").is(record.getBook().getId()));
        Update update = new Update().inc("availableCopies", 1);
        mongoTemplate.findAndModify(query, update, Book.class);

        record.setReturnDate(LocalDate.now());
        record.setStatus(BorrowStatus.RETURNED);

        return mapToDto(borrowRecordRepository.save(record));
    }

    public List<BorrowRecordResponseDto> getAllRecords() {
        return borrowRecordRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private BorrowRecordResponseDto mapToDto(BorrowRecord record) {
        return new BorrowRecordResponseDto(
                record.getId(),
                record.getUser().getName(),
                record.getUser().getEmail(),
                record.getBook().getTitle(),
                record.getBook().getIsbn(),
                record.getBorrowDate(),
                record.getDueDate(),
                record.getReturnDate(),
                record.getStatus()
        );
    }
}