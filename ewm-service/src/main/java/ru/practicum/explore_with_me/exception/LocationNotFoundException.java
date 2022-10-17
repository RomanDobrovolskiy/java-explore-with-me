package ru.practicum.explore_with_me.exception;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Long locationId) {
        super(String.format("Location with id %d not found", locationId));
    }
}
