package com.example.sundorproject.repository;

import com.example.sundorproject.model.Role;
import com.example.sundorproject.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // লগইনের সময় বা ইউজার ডিটেইলস পাওয়ার জন্য
    Optional<User> findByEmail(String email);

    // রেজিস্ট্রেশনের সময় ইমেইল ডুপ্লিকেট কি না চেক করতে
    boolean existsByEmail(String email);

    // রোল অনুযায়ী (যেমন: সব সাধারণ ইউজার বা সব অ্যাডমিন) তালিকা দেখতে
    List<User> findByRole(Role role);

    // শুধুমাত্র অ্যাক্টিভ ইউজারদের খুঁজতে
    List<User> findByActiveTrue();
}