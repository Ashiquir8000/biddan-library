package com.example.sundorproject.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BookDto(
        String id,

        @NotBlank(message = "বইয়ের নাম দেওয়া আবশ্যক")
        @Size(min = 1, max = 150, message = "বইয়ের নাম ১ থেকে ১৫০ অক্ষরের মধ্যে হতে হবে")
        String title,

        @NotBlank(message = "লেখকের নাম দেওয়া আবশ্যক")
        @Size(min = 2, max = 100, message = "লেখকের নাম ২ থেকে ১০০ অক্ষরের মধ্যে হতে হবে")
        String author,

        @NotBlank(message = "ISBN নম্বর প্রদান করুন")
        @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "সঠিক ১০ বা ১৩ ডিজিটের ISBN নম্বর দিন")
        String isbn,

        @NotBlank(message = "একটি ক্যাটাগরি সিলেক্ট করুন")
        String categoryId,

        @NotNull(message = "মোট কপির সংখ্যা উল্লেখ করুন")
        @Min(value = 1, message = "কমপক্ষে ১টি কপি থাকতে হবে")
        Integer totalCopies,

        @Size(max = 1000, message = "বিবরণ ১০০০ অক্ষরের বেশি হতে পারবে না")
        String description
) {
}