package ru.practicum.explore_with_me.controller.service.category;

import ru.practicum.explore_with_me.model.category.Category;
import ru.practicum.explore_with_me.model.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    CategoryDto patchCategory(CategoryDto categoryDto);

    CategoryDto getCategoryDto(Long categoryId);

    Category getCategory(Long categoryId);

    List<CategoryDto> getAll(Integer from, Integer size);
}
