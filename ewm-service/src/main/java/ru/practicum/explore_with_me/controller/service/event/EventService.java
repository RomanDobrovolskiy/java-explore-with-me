package ru.practicum.explore_with_me.controller.service.event;

import ru.practicum.explore_with_me.model.event.*;

import java.util.List;

public interface EventService {
    List<ShortEventDto> findPublicEvent(FindPublicEventOptions options, Integer from, Integer size);

    FullEventDto getById(Long eventId);

    List<ShortEventDto> getUserEvents(Long userId, Integer from, Integer size);

    FullEventDto createEvent(Long userId, CreateEventDto createEventDto);

    FullEventDto patchEvent(Long userId, UpdateEventDto updateEventDto);

    FullEventDto getUserEvent(Long userId, Long eventId);

    FullEventDto cancelUserEvent(Long userId, Long eventId);

    Event getEvent(long eventId);

    List<FullEventDto> findEvents(FindUserEventOptions options, Integer from, Integer size);

    FullEventDto adminUpdateEvent(Long eventId, AdminUpdateEventDto adminUpdateEventDto);

    FullEventDto publishEvent(Long eventId);

    FullEventDto rejectEvent(Long eventId);

    List<ShortEventDto> getPublishedEventsInLocation(Long locationId, Integer from, Integer size);

    List<FullEventDto> getEventsInLocation(Long locationId, Integer from, Integer size);
}
