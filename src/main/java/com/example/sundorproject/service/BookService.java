package com.example.sundorproject.service;

import com.example.sundorproject.DTO.BookDto;
import com.example.sundorproject.model.Book;
import com.example.sundorproject.model.Category;
import com.example.sundorproject.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    public Book save(BookDto dto) {
        Category category = categoryService.getById(dto.categoryId());

        if (dto.id() == null) {
            if (bookRepository.existsByIsbn(dto.isbn())) {
                throw new IllegalArgumentException("এই ISBN নম্বরের বই আগেই যোগ করা হয়েছে!");
            }
            Book book = Book.builder()
                    .title(dto.title())
                    .author(dto.author())
                    .isbn(dto.isbn())
                    .category(category)
                    .totalCopies(dto.totalCopies())
                    .availableCopies(dto.totalCopies())
                    .description(dto.description())
                    .build();
            return bookRepository.save(book);
        }

        Book existing = getById(dto.id());
        int diff = dto.totalCopies() - existing.getTotalCopies();
        int updatedAvailable = existing.getAvailableCopies() + diff;
        if (updatedAvailable < 0) {
            throw new IllegalArgumentException("মোট কপি সংখ্যা ইস্যুকৃত কপির চেয়ে কম হতে পারে না!");
        }

        existing.setTitle(dto.title());
        existing.setAuthor(dto.author());
        existing.setIsbn(dto.isbn());
        existing.setCategory(category);
        existing.setTotalCopies(dto.totalCopies());
        existing.setAvailableCopies(updatedAvailable);
        existing.setDescription(dto.description());

        return bookRepository.save(existing);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book getById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("বই পাওয়া যায়নি!"));
    }

    public void delete(String id) {
        bookRepository.deleteById(id);
    }
}