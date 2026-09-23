package com.example.sundorproject.config;

import com.example.sundorproject.model.Book;
import com.example.sundorproject.model.Category;
import com.example.sundorproject.model.Role;
import com.example.sundorproject.model.User;
import com.example.sundorproject.repository.BookRepository;
import com.example.sundorproject.repository.CategoryRepository;
import com.example.sundorproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        // ১. প্রাথমিক ইউজার তৈরি (যদি আগে থেকে না থাকে)
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .name("সিস্টেম অ্যাডমিন")
                    .email("admin@library.com")
                    .password("admin123")
                    .role(Role.ROLE_ADMIN)
                    .phone("01711000000")
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            User student = User.builder()
                    .name("রাকিবুল হাসান")
                    .email("rakib@student.com")
                    .password("user123")
                    .role(Role.ROLE_USER)
                    .phone("01811000000")
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.saveAll(List.of(admin, student));
            log.info("ডিফল্ট ইউজার এবং অ্যাডমিন সফলভাবে যুক্ত হয়েছে।");
        }

        // ২. ক্যাটাগরি তৈরি
        if (categoryRepository.count() == 0) {
            Category programming = Category.builder()
                    .name("Software Engineering & Programming")
                    .description("কম্পিউটার সায়েন্স ও প্রোগ্রামিং সম্পর্কিত বই")
                    .build();

            Category science = Category.builder()
                    .name("Science & Mathematics")
                    .description("বিজ্ঞান, গণিত ও গবেষণা সংক্রান্ত বই")
                    .build();

            Category literature = Category.builder()
                    .name("Literature & Fiction")
                    .description("গল্প, উপন্যাস ও ধ্রুপদী সাহিত্য")
                    .build();

            categoryRepository.saveAll(List.of(programming, science, literature));
            log.info("ডিফল্ট ক্যাটাগরি ডাটাবেজে যুক্ত হয়েছে।");
        }

        // ৩. বই তৈরি
        if (bookRepository.count() == 0) {
            Category programming = categoryRepository.findByNameIgnoreCase("Software Engineering & Programming")
                    .orElse(null);
            Category literature = categoryRepository.findByNameIgnoreCase("Literature & Fiction")
                    .orElse(null);

            Book book1 = Book.builder()
                    .title("Clean Architecture: A Craftsman's Guide")
                    .author("Robert C. Martin")
                    .isbn("9780134494166")
                    .category(programming)
                    .totalCopies(5)
                    .availableCopies(5)
                    .description("A comprehensive guide to software structure and design.")
                    .build();

            Book book2 = Book.builder()
                    .title("Designing Data-Intensive Applications")
                    .author("Martin Kleppmann")
                    .isbn("9781449373320")
                    .category(programming)
                    .totalCopies(3)
                    .availableCopies(3)
                    .description("The big ideas behind reliable, scalable, and maintainable systems.")
                    .build();

            Book book3 = Book.builder()
                    .title("হিমু এবং একটি নীল পদ্ম")
                    .author("হুমায়ূন আহমেদ")
                    .isbn("9789844120891")
                    .category(literature)
                    .totalCopies(4)
                    .availableCopies(4)
                    .description("জনপ্রিয় বাংলা সমকালীন ফিকশন উপন্যাস।")
                    .build();

            bookRepository.saveAll(List.of(book1, book2, book3));
            log.info("ডিফল্ট বইয়ের তালিকা ডাটাবেজে ইনসার্ট হয়েছে।");
        }
    }
}