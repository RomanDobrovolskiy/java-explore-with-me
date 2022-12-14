package ru.practicum.explore_with_me.controller.service.event;

import ru.practicum.explore_with_me.exception.EventBadRequestException;
import ru.practicum.explore_with_me.exception.EventNotFoundException;
import ru.practicum.explore_with_me.model.event.Event;
import ru.practicum.explore_with_me.model.event.EventState;
import ru.practicum.explore_with_me.model.location.Location;
import ru.practicum.explore_with_me.utils.Coordinate;

import java.time.LocalDateTime;

class EventValidator {

    static boolean canEventBeCanceled(Event event) {
        if (event.getState() == EventState.CANCELED) {
            throw new EventBadRequestException("Event already cancelled");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new EventBadRequestException("Cannot cancel published event");
        }
        return true;
    }

    static boolean canEventBeCanceledByUser(Event event, Long userId) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EventNotFoundException(event.getId());
        }
        if (event.getState() == EventState.CANCELED) {
            throw new EventBadRequestException("Event already cancelled");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new EventBadRequestException("Cannot cancel published event");
        }
        return true;
    }

    static boolean canEventBePublished(Event event, LocalDateTime currentTime) {
        if (event.getEventDate().isBefore(currentTime.plusHours(1))) {
            throw new EventBadRequestException("Event starts in less than 1 hour");
        }
        if (event.getState() == EventState.CANCELED) {
            throw new EventBadRequestException("Event was rejected");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new EventBadRequestException("Event already published");
        }
        return true;
    }

    static boolean isEventInLocation(Event event, Location location) {
        if (Coordinate.distance(location.getLongitude(), location.getLatitude(),
                event.getLongitude(), event.getLatitude()) > location.getRadius()) {
            throw new EventBadRequestException("Event not in this location");
        }
        return true;
    }

}
