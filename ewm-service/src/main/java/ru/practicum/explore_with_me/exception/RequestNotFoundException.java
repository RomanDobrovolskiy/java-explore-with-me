package ru.practicum.explore_with_me.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(Long requestId) {
        super(String.format("Request with id %d not found", requestId));
    }
}
