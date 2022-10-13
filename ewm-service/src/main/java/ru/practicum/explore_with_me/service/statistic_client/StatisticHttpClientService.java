package ru.practicum.explore_with_me.service.statistic_client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.model.statistic_client.StatisticHitEndpointDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatisticHttpClientService implements StatisticService {

    private final StatisticClient client;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final String SERVICE_NAME = "Explore-With-Me";

    @Override
    public void hitEndpoint(String endpoint, String ipAddress) {
        StatisticHitEndpointDto dto = StatisticHitEndpointDto.builder()
                .app(SERVICE_NAME)
                .uri(endpoint)
                .ip(ipAddress)
                .build();

        try {
            client.post("hit", dto);
        } catch (Exception ex) {
            log.error("Statistic service GET error: " + ex.getMessage());
        }
    }

    @Override
    public Long getStatistic(String endpoint) {
        String startRange = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).format(formatter);
        String endRange = LocalDateTime.now().plusYears(20).withNano(0).format(formatter);

        Map<String, Object> parameters = Map.of(
                "start", startRange,
                "end", endRange,
                "uris", endpoint
        );

        try {
            ResponseEntity<Object> response = client.get("stats?start={start}&end={end}&uris={uris}", parameters);
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Map<String, Object>> stats = (List<Map<String, Object>>) response.getBody();
                if (stats != null && stats.size() > 0) {
                    return ((Number) stats.get(0).get("hits")).longValue();
                }
            }
        } catch (Exception ex) {
            log.error("Statistic service GET error: " + ex.getMessage());
            return null;
        }
        return null;
    }
}
