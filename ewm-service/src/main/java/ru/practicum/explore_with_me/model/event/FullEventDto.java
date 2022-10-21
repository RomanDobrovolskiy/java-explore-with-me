package ru.practicum.explore_with_me.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explore_with_me.model.location.LocationDto;
import ru.practicum.explore_with_me.validation.Validation;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder
public class FullEventDto {
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private User initiator;
    private Boolean paid;
    private Integer participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Location location;
    private LocationDto assignedLocation;
    @Setter
    private Long views;

    @AllArgsConstructor
    @Getter
    public static class Category {
        private Long id;
        private String name;
    }

    @AllArgsConstructor
    @Getter
    public static class User {
        private Long id;
        private String name;
    }

    @Builder
    @Data
    public static class Location {
        @NotNull(groups = Validation.OnCreate.class)
        private Float lat;
        @NotNull(groups = Validation.OnCreate.class)
        private Float lon;
    }
}
