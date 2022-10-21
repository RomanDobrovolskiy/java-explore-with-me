package ru.practicum.explore_with_me.model.location;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explore_with_me.validation.Validation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class LocationDto {
    @NotNull(groups = Validation.OnPatch.class)
    private Long id;
    @NotNull(groups = Validation.OnCreate.class)
    @NotBlank(groups = Validation.OnCreate.class, message = "Location name can't be blank")
    private String name;
    @NotNull(groups = Validation.OnCreate.class)
    @Max(value = 90, message = "Latitude must be less than 90 degrees")
    @Min(value = -90, message = "Latitude must be greater than -90 degrees")
    private Double latitude;
    @NotNull(groups = Validation.OnCreate.class)
    @Max(value = 180, message = "Longitude must be less than 180 degrees")
    @Min(value = -180, message = "Longitude must be greater than -180 degrees")
    private Double longitude;
    @NotNull(groups = Validation.OnCreate.class)
    private Double radius;
}