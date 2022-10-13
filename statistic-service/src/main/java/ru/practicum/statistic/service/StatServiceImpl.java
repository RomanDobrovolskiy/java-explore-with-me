package ru.practicum.statistic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statistic.model.EndpointHit;
import ru.practicum.statistic.model.EndpointHitDto;
import ru.practicum.statistic.model.StatDto;
import ru.practicum.statistic.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatServiceImpl implements StatService {
    private final HitRepository repository;

    @Override
    public EndpointHitDto create(EndpointHitDto dto) {
        EndpointHit hit = EndpointHit.builder()
                .timestamp(LocalDateTime.now())
                .ip(dto.getIp())
                .app(dto.getApp())
                .uri(dto.getUri())
                .build();

        return EndpointHitDto.from(repository.save(hit));
    }

    @Override
    public List<StatDto> findStat(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique) {
        return repository.findAllByUriInAndTimestampBetween(uris, start, end).stream()
                .collect(Collectors.groupingBy(EndpointHit::getUri)).values().stream()
                .map(hits -> StatDto.builder()
                        .uri(hits.get(0).getUri())
                        .app(hits.get(0).getApp())
                        .hits(unique ? hits.stream().filter(distinctByKey(EndpointHit::getIp)).count() : hits.size())
                        .build())
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
