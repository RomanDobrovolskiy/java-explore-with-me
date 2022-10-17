package ru.practicum.explore_with_me.exception;

public class EventBadRequestException extends RuntimeException {
    public EventBadRequestException(String message) {
        super(message);
    }
}
