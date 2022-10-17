package ru.practicum.explore_with_me.model.compilation;

import ru.practicum.explore_with_me.model.event.Event;
import ru.practicum.explore_with_me.model.event.EventMapper;

import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList()))
                .build();
    }

    public static ReturnCompilationDto toReturnDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }
        return ReturnCompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream().map(EventMapper::toShortDto).collect(Collectors.toList()))
                .build();
    }
}
