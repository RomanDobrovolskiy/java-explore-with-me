package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.model.event.EventSortType;
import ru.practicum.explore_with_me.model.event.FindPublicEventOptions;
import ru.practicum.explore_with_me.model.event.FullEventDto;
import ru.practicum.explore_with_me.model.event.ShortEventDto;
import ru.practicum.explore_with_me.service.event.EventService;
import ru.practicum.explore_with_me.service.statistic_client.StatisticService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
@Slf4j
public class EventController {
    private final EventService eventService;
    private final StatisticService statisticService;

    @GetMapping
    public List<ShortEventDto> getEvents(@RequestParam(value = "text", defaultValue = "") String text,
                                         @RequestParam(value = "categories") Set<Long> categoryIds,
                                         @RequestParam(value = "paid") boolean paid,
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                         @RequestParam(value = "rangeStart", required = false) LocalDateTime start,
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                         @RequestParam(value = "rangeEnd", required = false) LocalDateTime end,
                                         @RequestParam(value = "onlyAvailable") boolean onlyAvailable,
                                         @RequestParam(value = "sort") EventSortType eventSortType,
                                         @RequestParam(value = "from", defaultValue = "0") Integer from,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                                         HttpServletRequest request) {

        FindPublicEventOptions options = new FindPublicEventOptions(text, categoryIds, paid, start, end,
                onlyAvailable, eventSortType);

        List<ShortEventDto> response = eventService.findPublicEvent(options, from, size);
        try {
            statisticService.hitEndpoint(request.getRequestURI(), request.getRemoteAddr());
        } catch (Exception ex) {
            log.error("Statistic service POST error: " + ex.getMessage());
        }

        return response;
    }

    @GetMapping("/{catId}")
    public FullEventDto getEventById(@PathVariable(name = "catId") Long categoryId, HttpServletRequest request) {
        FullEventDto response = eventService.getById(categoryId);
        try {
            statisticService.hitEndpoint(request.getRequestURI(), request.getRemoteAddr());
        } catch (Exception ex) {
            log.error("Statistic service POST error: " + ex.getMessage());
        }
        return response;
    }

}
