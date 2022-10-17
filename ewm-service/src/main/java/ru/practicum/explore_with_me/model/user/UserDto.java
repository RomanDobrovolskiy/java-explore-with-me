package ru.practicum.explore_with_me.model.user;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explore_with_me.validation.Validation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
    @NotNull(groups = Validation.OnUpdate.class)
    private Long id;
    @NotNull(groups = {Validation.OnCreate.class, Validation.OnUpdate.class})
    @Email(message = "Email should be valid")
    private String email;
    @NotNull(groups = {Validation.OnCreate.class, Validation.OnUpdate.class})
    @NotBlank(groups = {Validation.OnCreate.class, Validation.OnUpdate.class}, message = "User name can't be blank")
    private String name;
}
