package ru.practicum.explore_with_me.model.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReturnUserDto {
    private Long id;
    private String name;
    private String email;
}
