package ru.practicum.explore_with_me.controller.service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.exception.LocationBadRequest;
import ru.practicum.explore_with_me.model.location.LocationDto;
import ru.practicum.explore_with_me.model.location.LocationMapper;
import ru.practicum.explore_with_me.exception.LocationNotFoundException;
import ru.practicum.explore_with_me.model.location.Location;
import ru.practicum.explore_with_me.repository.LocationRepository;
import ru.practicum.explore_with_me.utils.OffsetBasedPageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository repository;

    @Override
    public Optional<Location> getNearestToPointLocation(float latitude, float longitude) {
        List<Location> locations = repository.getNearestLocations(latitude, longitude);

        if (locations.size() > 0) {
            return Optional.of(locations.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<LocationDto> getAllLocations(Integer from, Integer size, Double longitude, Double latitude) {
        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("name"));
        if (longitude != null && latitude != null) {
            if (longitude > 180 || longitude < -180) {
                throw new LocationBadRequest("Longitude must be in range [-180; 180]");
            }
            if (latitude > 90 || latitude < -90) {
                throw new LocationBadRequest("Latitude must be in range [-90; 90]");
            }

            return repository.getNearestLocations(latitude, longitude).stream()
                    .skip(from)
                    .limit(size)
                    .map(LocationMapper::toDto)
                    .collect(Collectors.toList());
        }
        return repository.findAll(page).stream().map(LocationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public LocationDto createLocation(LocationDto locationDto) {
        return LocationMapper.toDto(repository.save(LocationMapper.fromDto(locationDto)));
    }

    @Override
    public LocationDto patchLocation(LocationDto locationDto) {
        Location location = getLocation(locationDto.getId()).toBuilder().build();
        if (locationDto.getName() != null) {
            location.setName(locationDto.getName());
        }
        if (locationDto.getRadius() != null) {
            location.setRadius(locationDto.getRadius());
        }
        if (locationDto.getLongitude() != null) {
            location.setLongitude(locationDto.getLongitude());
        }
        if (locationDto.getLatitude() != null) {
            location.setLatitude(locationDto.getLatitude());
        }
        return LocationMapper.toDto(repository.save(location));
    }

    @Override
    public void deleteLocation(long id) {
        repository.deleteById(id);
    }

    @Override
    public Location getLocation(long id) {
        return repository.findById(id).orElseThrow(() -> new LocationNotFoundException(id));
    }
}