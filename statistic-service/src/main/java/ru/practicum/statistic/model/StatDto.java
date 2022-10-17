package ru.practicum.statistic.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatDto {
    private String app;
    private String uri;
    private long hits;

    public static StatDto from(EndpointHit hit) {
        return StatDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .build();
    }
}
