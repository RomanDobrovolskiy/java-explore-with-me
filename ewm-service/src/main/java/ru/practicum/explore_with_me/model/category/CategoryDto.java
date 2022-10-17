package ru.practicum.explore_with_me.model.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore_with_me.validation.Validation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @NotNull(groups = Validation.OnPatch.class)
    private Long id;
    @NotNull
    @NotBlank(groups = {Validation.OnCreate.class, Validation.OnPatch.class}, message = "Category name cannot be blank")
    private String name;
}
