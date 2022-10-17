package ru.practicum.explore_with_me.model.compilation;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.explore_with_me.model.event.ShortEventDto;

import java.util.Collection;

@Builder
@Getter
public class ReturnCompilationDto {
    private Long id;
    private Collection<ShortEventDto> events;
    private Boolean pinned;
    private String title;
}
