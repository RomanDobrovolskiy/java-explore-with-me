package ru.practicum.explore_with_me.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@AllArgsConstructor
@Getter
public class FindUserEventOptions {
    Collection<Long> userIds;
    Collection<EventState> states;
    Collection<Long> categoryIds;
    LocalDateTime start;
    LocalDateTime end;

    public FindUserEventOptions(Collection<Long> userIds, Collection<EventState> states, Collection<Long> categoryIds, String start, String end) {
        this.userIds = userIds;
        this.states = states;
        this.categoryIds = categoryIds;
        this.start = dateRange(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.end = dateRange(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public LocalDateTime dateRange(String date, DateTimeFormatter formatter) {
        if (date != null && !date.isBlank()) {
            try {
                return LocalDateTime.parse(date, formatter);
            } catch (Exception e) {
                throw new IllegalArgumentException("Cannot parse date range: " + start + " - " + end);
            }
        }
        return null;
    }
}
