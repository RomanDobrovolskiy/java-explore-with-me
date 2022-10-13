package ru.practicum.statistic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistic.model.EndpointHitDto;
import ru.practicum.statistic.model.StatDto;
import ru.practicum.statistic.service.StatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatService statService;

    @PostMapping("/hit")
    public EndpointHitDto createHit(@RequestBody EndpointHitDto dto) {
        return statService.create(dto);
    }

    @GetMapping("/stats")
    public List<StatDto> getStats(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                  @RequestParam(value = "start")
                                  LocalDateTime start,
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                  @RequestParam(value = "end")
                                  LocalDateTime end,
                                  @RequestParam(value = "uris") Set<String> uris,
                                  @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        return statService.findStat(start, end, uris, unique);
    }
}
