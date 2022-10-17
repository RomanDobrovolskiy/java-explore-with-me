package ru.practicum.explore_with_me.exception;

public class EventDateTooEarlyException extends RuntimeException {
    public EventDateTooEarlyException() {
        super("Event starts in less than 2 hours");
    }
}
