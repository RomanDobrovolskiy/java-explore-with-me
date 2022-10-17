package ru.practicum.explore_with_me.model.event;

import ru.practicum.explore_with_me.model.request.ParticipationRequestStatus;
import ru.practicum.explore_with_me.service.statistic_client.StatisticService;

public class EventMapper {
    private static StatisticService statisticService;

    public static void setStatisticService(StatisticService service) {
        if (statisticService == null) {
            statisticService = service;
        }
    }

    public static FullEventDto toFullDto(Event event) {
        if (event == null) {
            return null;
        }
        return FullEventDto.builder()
                .id(event.getId())
                .category(new FullEventDto.Category(event.getCategory().getId(), event.getCategory().getName()))
                .eventDate(event.getEventDate())
                .annotation(event.getAnnotation())
                .createdOn(event.getCreated())
                .publishedOn(event.getPublished())
                .description(event.getDescription())
                .title(event.getTitle())
                .paid(event.isPaid())
                .requestModeration(event.isRequestModeration())
                .initiator(new FullEventDto.User(event.getInitiator().getId(), event.getInitiator().getName()))
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .confirmedRequests(event.getParticipationRequests().stream()
                        .filter(r -> r.getStatus() == ParticipationRequestStatus.CONFIRMED).count())
                .location(new FullEventDto.Location(event.getLatitude(), event.getLongitude()))
                .views(statisticService != null ?
                        statisticService.getStatistic("/events/" + event.getId()) : null)
                .build();
    }

    public static ShortEventDto toShortDto(Event event) {
        if (event == null) {
            return null;
        }
        return ShortEventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new ShortEventDto.Category(event.getCategory().getId(), event.getCategory().getName()))
                .eventDate(event.getEventDate())
                .initiator(new ShortEventDto.User(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .confirmedRequests(event.getParticipationRequests().stream()
                        .filter(r -> r.getStatus() == ParticipationRequestStatus.CONFIRMED).count())
                .views(statisticService != null ?
                        statisticService.getStatistic("/events/" + event.getId()) : null)
                .build();
    }

    public static Event fromDto(CreateEventDto createEventDto) {
        if (createEventDto == null) {
            return null;
        }
        return Event.builder()
                .annotation(createEventDto.getAnnotation())
                .description(createEventDto.getDescription())
                .eventDate(createEventDto.getEventDate())
                .paid(createEventDto.getPaid())
                .participantLimit(createEventDto.getParticipantLimit())
                .requestModeration(createEventDto.getRequestModeration())
                .latitude(createEventDto.getLocation().getLat())
                .longitude(createEventDto.getLocation().getLon())
                .title(createEventDto.getTitle())
                .build();
    }
}
