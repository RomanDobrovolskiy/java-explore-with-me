package ru.practicum.statistic.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EndpointHitDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public static EndpointHitDto from(EndpointHit hit) {
        return EndpointHitDto.builder()
                .app(hit.getApp())
                .id(hit.getId())
                .uri(hit.getUri())
                .timestamp(hit.getTimestamp())
                .ip(hit.getIp())
                .build();
    }
}
