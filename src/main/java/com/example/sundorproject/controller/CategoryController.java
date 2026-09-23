package com.example.sundorproject.controller;

import com.example.sundorproject.DTO.CategoryDto;
import com.example.sundorproject.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("categoryDto", new CategoryDto(null, "", ""));
        return "categories/list";
    }

    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute("categoryDto") CategoryDto dto,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "categories/list";
        }
        try {
            categoryService.save(dto);
            redirectAttributes.addFlashAttribute("successMessage", "ক্যাটাগরি সংরক্ষিত হয়েছে!");
            return "redirect:/categories";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categories", categoryService.getAll());
            return "categories/list";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable String id, RedirectAttributes redirectAttributes) {
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "ক্যাটাগরি মুছে ফেলা হয়েছে!");
        return "redirect:/categories";
    }
}