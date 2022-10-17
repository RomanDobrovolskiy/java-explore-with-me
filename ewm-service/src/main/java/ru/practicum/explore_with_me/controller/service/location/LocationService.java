package ru.practicum.explore_with_me.controller.service.location;

import ru.practicum.explore_with_me.model.location.LocationDto;
import ru.practicum.explore_with_me.model.location.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    Optional<Location> getNearestToPointLocation(float latitude, float longitude);

    List<LocationDto> getAllLocations(Integer from, Integer size, Double longitude, Double latitude);

    LocationDto createLocation(LocationDto location);

    void deleteLocation(long id);

    LocationDto patchLocation(LocationDto location);

    Location getLocation(long id);
}