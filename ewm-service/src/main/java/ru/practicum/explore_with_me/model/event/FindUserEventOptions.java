package ru.practicum.explore_with_me.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;

@AllArgsConstructor
@Getter
public class FindUserEventOptions {
    Collection<Long> userIds;
    Collection<EventState> states;
    Collection<Long> categoryIds;
    LocalDateTime start;
    LocalDateTime end;
}
