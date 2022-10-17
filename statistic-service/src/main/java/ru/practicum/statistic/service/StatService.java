package ru.practicum.statistic.service;

import ru.practicum.statistic.model.EndpointHitDto;
import ru.practicum.statistic.model.StatDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatService {

    EndpointHitDto create(EndpointHitDto dto);

    List<StatDto> findStat(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique);
}
