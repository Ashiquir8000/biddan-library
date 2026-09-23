package com.example.sundorproject.service;

import com.example.sundorproject.DTO.CategoryDto;
import com.example.sundorproject.model.Category;
import com.example.sundorproject.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category save(CategoryDto dto) {
        if (dto.id() == null && categoryRepository.existsByNameIgnoreCase(dto.name())) {
            throw new IllegalArgumentException("এই নামের ক্যাটাগরি ইতিমধ্যে রয়েছে!");
        }
        Category category = Category.builder()
                .id(dto.id())
                .name(dto.name())
                .description(dto.description())
                .build();
        return categoryRepository.save(category);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ক্যাটাগরি পাওয়া যায়নি!"));
    }

    public void delete(String id) {
        categoryRepository.deleteById(id);
    }
}