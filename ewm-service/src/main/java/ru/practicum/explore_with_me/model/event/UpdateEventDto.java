package ru.practicum.explore_with_me.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.explore_with_me.utils.LocalDateTimeDeserializer;
import ru.practicum.explore_with_me.validation.Validation;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Builder
@Getter
public class UpdateEventDto {
    @NotNull(groups = Validation.OnPatch.class)
    private Long eventId;
    private String annotation;
    private Long category;
    private String description;
    @Future
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Boolean paid;
    @PositiveOrZero
    @Builder.Default
    private Integer participantLimit = 0;
    private String title;
    private Long locationId;
}