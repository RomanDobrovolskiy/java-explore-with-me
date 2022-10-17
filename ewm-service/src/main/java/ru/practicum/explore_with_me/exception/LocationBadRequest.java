package ru.practicum.explore_with_me.exception;

public class LocationBadRequest extends RuntimeException {
    public LocationBadRequest(String message) {
        super(message);
    }
}