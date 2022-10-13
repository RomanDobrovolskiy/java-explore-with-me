package ru.practicum.explore_with_me.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explore_with_me.utils.LocalDateTimeDeserializer;
import ru.practicum.explore_with_me.validation.Validation;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateEventDto {
    @NotNull(groups = Validation.OnPatch.class)
    private Long id;
    @NotNull(groups = Validation.OnCreate.class)
    @NotBlank(groups = Validation.OnCreate.class)
    private String annotation;
    @NotNull(groups = Validation.OnCreate.class)
    private Long category;
    @NotNull(groups = Validation.OnCreate.class)
    @NotBlank(groups = Validation.OnCreate.class)
    private String description;
    @NotNull(groups = Validation.OnCreate.class)
    @Future
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull(groups = Validation.OnCreate.class)
    private Location location;
    @NotNull(groups = Validation.OnCreate.class)
    private Boolean paid;
    @PositiveOrZero
    @Builder.Default
    private Integer participantLimit = 0;
    @Builder.Default
    private Boolean requestModeration = true;
    @NotNull(groups = Validation.OnCreate.class)
    private String title;

    @Builder
    @Data
    public static class Location {
        @NotNull(groups = Validation.OnCreate.class)
        private Float lat;
        @NotNull(groups = Validation.OnCreate.class)
        private Float lon;
    }
}
