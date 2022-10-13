package ru.practicum.explore_with_me.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@AllArgsConstructor
public class FindPublicEventOptions {
    String text;
    Collection<Long> categoryIds;
    boolean paid;
    LocalDateTime start;
    LocalDateTime end;
    boolean onlyAvailable;
    EventSortType eventSortType;
}