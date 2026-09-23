package com.example.sundorproject.repository;

import com.example.sundorproject.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    // ক্যাটাগরির নাম অনুযায়ী খুঁজতে (কেস-ইনসেনসিটিভ)
    Optional<Category> findByNameIgnoreCase(String name);

    // নতুন ক্যাটাগরি তৈরির সময় নাম অলরেডি আছে কি না দেখতে
    boolean existsByNameIgnoreCase(String name);
}