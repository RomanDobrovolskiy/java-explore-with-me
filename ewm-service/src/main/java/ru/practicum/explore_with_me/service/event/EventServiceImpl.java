package ru.practicum.explore_with_me.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.exception.EventBadRequestException;
import ru.practicum.explore_with_me.exception.EventDateTooEarlyException;
import ru.practicum.explore_with_me.exception.EventNotFoundException;
import ru.practicum.explore_with_me.model.category.Category;
import ru.practicum.explore_with_me.model.event.*;
import ru.practicum.explore_with_me.model.user.User;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.service.category.CategoryService;
import ru.practicum.explore_with_me.service.statistic_client.StatisticService;
import ru.practicum.explore_with_me.service.user.UserService;
import ru.practicum.explore_with_me.utils.OffsetBasedPageRequest;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final StatisticService statisticService;

    @PostConstruct
    private void setStatisticServiceToEventMapper() {
        EventMapper.setStatisticService(statisticService);
    }

    @Override
    public List<ShortEventDto> findPublicEvent(FindPublicEventOptions options, Integer from, Integer size) {
        List<Event> events;

        if (options.getStart() == null || options.getEnd() == null) {
            events = eventRepository.findAfterDate(options.getText(), options.getCategoryIds(), options.isPaid(),
                    LocalDateTime.now());
        } else {
            events = eventRepository.findBetweenDates(options.getText(), options.getCategoryIds(), options.isPaid(),
                    options.getStart(), options.getEnd());
        }

        List<ShortEventDto> returnEvents = new ArrayList<>();

        for (Event event : events) {
            ShortEventDto dto = EventMapper.toShortDto(event);
            if (event.getParticipantLimit() > 0) {
                if (options.isOnlyAvailable()) {
                    if (dto.getConfirmedRequests() < event.getParticipantLimit()) {
                        returnEvents.add(dto);
                    }
                } else {
                    returnEvents.add(dto);
                }
            } else {
                returnEvents.add(dto);
            }
        }

        if (options.getEventSortType() == EventSortType.VIEWS) {
            return returnEvents.stream()
                    .sorted(Comparator.comparingLong(ShortEventDto::getViews))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        } else {
            return returnEvents.stream()
                    .sorted(Comparator.comparing(ShortEventDto::getEventDate))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public FullEventDto getById(Long eventId) {
        Event event = getEvent(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new EventNotFoundException(eventId);
        }
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<ShortEventDto> getUserEvents(Long userId, Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("id"));
        userService.getUser(userId);

        return eventRepository.findAllByInitiatorId(userId, page).stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FullEventDto createEvent(Long userId, CreateEventDto createEventDto) {
        LocalDateTime now = LocalDateTime.now();
        if (createEventDto.getEventDate().isBefore(now.plusHours(2))) {
            throw new EventDateTooEarlyException();
        }

        User user = userService.getUser(userId);

        Category category = categoryService.getCategory(createEventDto.getCategory());

        Event event = EventMapper.fromDto(createEventDto);
        event.setCreated(now);
        event.setInitiator(user);
        event.setState(EventState.PENDING);
        event.setCategory(category);

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public FullEventDto patchEvent(Long userId, UpdateEventDto updateEventDto) {
        LocalDateTime now = LocalDateTime.now();

        User user = userService.getUser(userId);
        Event eventToUpdate = getEvent(updateEventDto.getEventId()).toBuilder().build();

        if (!eventToUpdate.getInitiator().getId().equals(user.getId())) {
            throw new EventNotFoundException(eventToUpdate.getId());
        }

        if (eventToUpdate.getState() == EventState.PUBLISHED) {
            throw new EventBadRequestException("Cant modify published event");
        }

        if (updateEventDto.getEventDate() != null) {
            if (updateEventDto.getEventDate().isBefore(now.plusHours(2))) {
                throw new EventDateTooEarlyException();
            } else {
                eventToUpdate.setEventDate(updateEventDto.getEventDate());
            }
        }

        if (updateEventDto.getAnnotation() != null && !updateEventDto.getAnnotation().isBlank()) {
            eventToUpdate.setAnnotation(updateEventDto.getAnnotation());
        }

        if (updateEventDto.getDescription() != null && !updateEventDto.getDescription().isBlank()) {
            eventToUpdate.setDescription(updateEventDto.getDescription());
        }

        if (updateEventDto.getCategory() != null) {
            eventToUpdate.setCategory(categoryService.getCategory(updateEventDto.getCategory()));
        }

        if (updateEventDto.getPaid() != null) {
            eventToUpdate.setPaid(updateEventDto.getPaid());
        }

        if (updateEventDto.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(updateEventDto.getParticipantLimit());
        }

        if (updateEventDto.getTitle() != null && !updateEventDto.getTitle().isBlank()) {
            eventToUpdate.setTitle(updateEventDto.getTitle());
        }

        return EventMapper.toFullDto(eventRepository.save(eventToUpdate));
    }

    @Override
    public FullEventDto getUserEvent(Long userId, Long eventId) {
        User user = userService.getUser(userId);
        Event event = getEvent(eventId);

        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new EventNotFoundException(event.getId());
        }
        return EventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public FullEventDto cancelUserEvent(Long userId, Long eventId) {
        userService.getUser(userId);
        Event eventToCancel = getEvent(eventId).toBuilder().build();

        if (EventValidator.canEventBeCanceledByUser(eventToCancel, userId)) {
            eventToCancel.setState(EventState.CANCELED);
        }
        return EventMapper.toFullDto(eventRepository.save(eventToCancel));
    }

    @Override
    public List<FullEventDto> findEvents(FindUserEventOptions options, Integer from, Integer size) {

        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("id"));
        List<Event> events = eventRepository.findBetweenDatesByUsersStatesCategories(options.getUserIds(),
                options.getStates(), options.getCategoryIds(), options.getStart(), options.getEnd(), page);

        return events.stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public FullEventDto adminUpdateEvent(Long eventId, AdminUpdateEventDto adminUpdateEventDto) {
        Event eventToUpdate = getEvent(eventId).toBuilder().build();

        if (adminUpdateEventDto.getEventDate() != null) {
            eventToUpdate.setEventDate(adminUpdateEventDto.getEventDate());
        }

        if (adminUpdateEventDto.getAnnotation() != null && !adminUpdateEventDto.getAnnotation().isBlank()) {
            eventToUpdate.setAnnotation(adminUpdateEventDto.getAnnotation());
        }

        if (adminUpdateEventDto.getLocation() != null) {
            if (adminUpdateEventDto.getLocation().getLon() != null) {
                eventToUpdate.setLongitude(adminUpdateEventDto.getLocation().getLon());
            }
            if (adminUpdateEventDto.getLocation().getLat() != null) {
                eventToUpdate.setLatitude(adminUpdateEventDto.getLocation().getLat());
            }
        }

        if (adminUpdateEventDto.getDescription() != null && !adminUpdateEventDto.getDescription().isBlank()) {
            eventToUpdate.setDescription(adminUpdateEventDto.getDescription());
        }

        if (adminUpdateEventDto.getRequestModeration() != null) {
            eventToUpdate.setRequestModeration(adminUpdateEventDto.getRequestModeration());
        }

        if (adminUpdateEventDto.getCategory() != null) {
            eventToUpdate.setCategory(categoryService.getCategory(adminUpdateEventDto.getCategory()));
        }

        if (adminUpdateEventDto.getPaid() != null) {
            eventToUpdate.setPaid(adminUpdateEventDto.getPaid());
        }

        if (adminUpdateEventDto.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(adminUpdateEventDto.getParticipantLimit());
        }

        if (adminUpdateEventDto.getTitle() != null && !adminUpdateEventDto.getTitle().isBlank()) {
            eventToUpdate.setTitle(adminUpdateEventDto.getTitle());
        }

        return EventMapper.toFullDto(eventRepository.save(eventToUpdate));
    }

    @Override
    @Transactional
    public FullEventDto publishEvent(Long eventId) {
        LocalDateTime now = LocalDateTime.now();
        Event event = getEvent(eventId);
        if (EventValidator.canEventBePublished(event, now)) {
            event.setState(EventState.PUBLISHED);
            event.setPublished(now);
        }
        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public FullEventDto rejectEvent(Long eventId) {
        Event event = getEvent(eventId);
        if (EventValidator.canEventBeCanceled(event)) {
            event.setState(EventState.CANCELED);
        }
        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public Event getEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
    }
}
