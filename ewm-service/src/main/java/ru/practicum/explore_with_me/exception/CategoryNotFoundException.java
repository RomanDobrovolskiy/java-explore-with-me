package ru.practicum.explore_with_me.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long categoryId) {
        super(String.format("Category with id %d not found", categoryId));
    }
}
