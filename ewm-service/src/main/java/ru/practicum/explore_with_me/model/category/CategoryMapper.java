package ru.practicum.explore_with_me.model.category;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category fromDto(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        return new Category(categoryDto.getId(), categoryDto.getName());
    }
}
