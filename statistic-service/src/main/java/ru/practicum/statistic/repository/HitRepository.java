package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.statistic.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {
    List<EndpointHit> findAllByUriInAndTimestampBetween(Collection<String> uris, LocalDateTime start,
                                                        LocalDateTime end);
}
