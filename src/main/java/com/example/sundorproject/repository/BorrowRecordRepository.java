package com.example.sundorproject.repository;

import com.example.sundorproject.model.Book;
import com.example.sundorproject.model.BorrowRecord;
import com.example.sundorproject.model.BorrowStatus;
import com.example.sundorproject.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends MongoRepository<BorrowRecord, String> {

    // নির্দিষ্ট ইউজারের সব হিস্ট্রি দেখতে
    List<BorrowRecord> findByUser(User user);

    // নির্দিষ্ট ইউজারের বর্তমান স্ট্যাটাস অনুযায়ী রেকর্ড (যেমন: এখনও কোন বইগুলো সে ফেরত দেয়নি)
    List<BorrowRecord> findByUserAndStatus(User user, BorrowStatus status);

    // কোনো বই বর্তমানে ধার দেওয়া অবস্থায় আছে কি না চেক করতে
    List<BorrowRecord> findByBookAndStatus(Book book, BorrowStatus status);

    // একজন ইউজার একই বই বর্তমানে ধার নিয়ে রেখেছে কি না (ডুপ্লিকেট একই বই নেওয়া আটকাতে)
    Optional<BorrowRecord> findByUserAndBookAndStatus(User user, Book book, BorrowStatus status);

    // নির্ধারিত তারিখ পেরিয়ে গেছে কিন্তু ফেরত দেয়নি (Overdue বই ট্র্যাক করতে)
    List<BorrowRecord> findByStatusAndDueDateBefore(BorrowStatus status, LocalDate currentDate);
}