package com.example.sundorproject.controller;

import com.example.sundorproject.DTO.BorrowRequestDto;
import com.example.sundorproject.service.BookService;
import com.example.sundorproject.service.BorrowService;
import com.example.sundorproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/borrows")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;
    private final UserService userService;
    private final BookService bookService;

    @GetMapping
    public String listRecords(Model model) {
        model.addAttribute("records", borrowService.getAllRecords());
        return "borrows/list";
    }

    @GetMapping("/issue")
    public String showIssueForm(Model model) {
        model.addAttribute("borrowDto", new BorrowRequestDto("", "", LocalDate.now().plusDays(14)));
        model.addAttribute("users", userService.getAllActiveUsers());
        model.addAttribute("books", bookService.getAll());
        return "borrows/form";
    }

    @PostMapping("/issue")
    public String processIssue(@Valid @ModelAttribute("borrowDto") BorrowRequestDto dto,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllActiveUsers());
            model.addAttribute("books", bookService.getAll());
            return "borrows/form";
        }
        try {
            borrowService.issueBook(dto);
            redirectAttributes.addFlashAttribute("successMessage", "বই সফলভাবে ইস্যু করা হয়েছে!");
            return "redirect:/borrows";
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("users", userService.getAllActiveUsers());
            model.addAttribute("books", bookService.getAll());
            return "borrows/form";
        }
    }

    @PostMapping("/return/{id}")
    public String processReturn(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            borrowService.returnBook(id);
            redirectAttributes.addFlashAttribute("successMessage", "বই সফলভাবে ফেরত নেওয়া হয়েছে!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/borrows";
    }
}