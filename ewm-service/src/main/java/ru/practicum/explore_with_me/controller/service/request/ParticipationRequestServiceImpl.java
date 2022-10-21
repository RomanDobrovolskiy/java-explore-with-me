package ru.practicum.explore_with_me.controller.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.controller.service.event.EventService;
import ru.practicum.explore_with_me.controller.service.user.UserService;
import ru.practicum.explore_with_me.exception.EventBadRequestException;
import ru.practicum.explore_with_me.exception.EventNotFoundException;
import ru.practicum.explore_with_me.exception.RequestNotFoundException;
import ru.practicum.explore_with_me.model.event.Event;
import ru.practicum.explore_with_me.model.event.EventState;
import ru.practicum.explore_with_me.model.request.ParticipationRequest;
import ru.practicum.explore_with_me.model.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.model.request.ParticipationRequestStatus;
import ru.practicum.explore_with_me.model.request.RequestMapper;
import ru.practicum.explore_with_me.model.user.User;
import ru.practicum.explore_with_me.repository.ParticipationRequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventService eventService;
    private final UserService userService;

    @Override
    public List<ParticipationRequestDto> getRequestsForUserEvents(Long userId, Long eventId) {
        Event event = eventService.getEvent(eventId);
        User user = userService.getUser(userId);
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new EventNotFoundException(eventId);
        }

        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto approveRequest(Long userId, Long eventId, Long requestId) {
        userService.getUser(userId);
        ParticipationRequest request = getRequest(requestId);
        if (!request.getEvent().getId().equals(eventId) || !request.getEvent().getInitiator().getId().equals(userId)) {
            throw new RequestNotFoundException(requestId);
        }

        if (request.getEvent().getParticipantLimit() > 0) {
            long confirmedRequests = request.getEvent().getParticipationRequests().stream()
                    .filter(r -> r.getStatus() == ParticipationRequestStatus.CONFIRMED)
                    .count();
            if (confirmedRequests >= request.getEvent().getParticipantLimit()) {
                throw new EventBadRequestException("Event participant limit reached");
            } else if (confirmedRequests == request.getEvent().getParticipantLimit() - 1) {
                request.getEvent().getParticipationRequests().stream()
                        .filter(r -> !r.getId().equals(requestId) &&
                                r.getStatus() == ParticipationRequestStatus.PENDING)
                        .peek(r -> r.setStatus(ParticipationRequestStatus.CANCELED))
                        .peek(requestRepository::save);
            }
        }
        request.setStatus(ParticipationRequestStatus.CONFIRMED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long requestId) {
        userService.getUser(userId);
        ParticipationRequest request = getRequest(requestId);
        if (!request.getEvent().getId().equals(eventId) || !request.getEvent().getInitiator().getId().equals(userId)) {
            throw new RequestNotFoundException(requestId);
        }
        request.setStatus(ParticipationRequestStatus.REJECTED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        userService.getUser(userId);

        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        LocalDateTime now = LocalDateTime.now();
        User user = userService.getUser(userId);
        Event event = eventService.getEvent(eventId);

        if (event.getInitiator().getId().equals(userId) || event.getState() != EventState.PUBLISHED) {
            throw new EventNotFoundException(event.getId());
        }

        if (event.getParticipantLimit() > 0 && event.getParticipationRequests().stream()
                .filter(r -> r.getStatus() == ParticipationRequestStatus.CONFIRMED)
                .count() >= event.getParticipantLimit()) {
            throw new EventBadRequestException("Event participant limit reached");
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .requester(user)
                .created(now)
                .event(event)
                .build();

        if (event.isRequestModeration()) {
            request.setStatus(ParticipationRequestStatus.PENDING);
        } else {
            request.setStatus(ParticipationRequestStatus.CONFIRMED);
        }

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userService.getUser(userId);
        ParticipationRequest request = getRequest(requestId);
        if (!request.getRequester().getId().equals(userId)) {
            throw new RequestNotFoundException(requestId);
        }
        request.setStatus(ParticipationRequestStatus.CANCELED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    private ParticipationRequest getRequest(Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new RequestNotFoundException(id));
    }
}
