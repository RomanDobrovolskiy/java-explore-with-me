package ru.practicum.explore_with_me.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long eventId) {
        super(String.format("Event with id %d not found", eventId));
    }
}
