package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore_with_me.model.location.LocationDto;
import ru.practicum.explore_with_me.controller.service.location.LocationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class LocationController {
    private final LocationService service;

    @GetMapping("/locations")
    public List<LocationDto> getAllLocations(@RequestParam(value = "from", defaultValue = "0") Integer from,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                                             @RequestParam(value = "longitude", required = false) Double lon,
                                             @RequestParam(value = "latitude", required = false) Double lat) {
        return service.getAllLocations(from, size, lon, lat);
    }
}
