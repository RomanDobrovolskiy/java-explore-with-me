package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.model.category.CategoryDto;
import ru.practicum.explore_with_me.controller.service.category.CategoryService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories(@RequestParam(value = "from", defaultValue = "0") Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable(name = "catId") Long categoryId) {
        return categoryService.getCategoryDto(categoryId);
    }
}
