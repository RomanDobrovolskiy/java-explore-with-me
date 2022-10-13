package ru.practicum.explore_with_me.exception;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(long categoryId) {
        super(String.format("Category with id %d not found", categoryId));
    }
}
