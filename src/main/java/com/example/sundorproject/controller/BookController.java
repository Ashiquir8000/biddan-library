package com.example.sundorproject.controller;

import com.example.sundorproject.DTO.BookDto;
import com.example.sundorproject.service.BookService;
import com.example.sundorproject.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "books/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("bookDto", new BookDto(null, "", "", "", "", 1, ""));
        model.addAttribute("categories", categoryService.getAll());
        return "books/form";
    }

    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute("bookDto") BookDto dto,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "books/form";
        }
        try {
            bookService.save(dto);
            redirectAttributes.addFlashAttribute("successMessage", "বই সফলভাবে সংরক্ষিত হয়েছে!");
            return "redirect:/books";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categories", categoryService.getAll());
            return "books/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable String id, RedirectAttributes redirectAttributes) {
        bookService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "বই মুছে ফেলা হয়েছে!");
        return "redirect:/books";
    }
}