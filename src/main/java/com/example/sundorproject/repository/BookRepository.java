package com.example.sundorproject.repository;

import com.example.sundorproject.model.Book;
import com.example.sundorproject.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    // ISBN অনুযায়ী বই খুঁজতে
    Optional<Book> findByIsbn(String isbn);

    // নতুন বই যোগের সময় ISBN ডুপ্লিকেট কি না চেক করতে
    boolean existsByIsbn(String isbn);

    // বইয়ের নামের অংশবিশেষ দিয়ে সার্চ করতে (যেমন: "java" লিখলেই চলে আসবে)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // লেখকের নাম দিয়ে সার্চ করতে
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // নির্দিষ্ট ক্যাটাগরির অন্তর্ভুক্ত বইগুলো পেতে
    List<Book> findByCategory(Category category);

    // যে বইগুলো বর্তমানে স্টকে ধার দেওয়ার জন্য খালি আছে
    List<Book> findByAvailableCopiesGreaterThan(int count);
}