package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.model.location.Location;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location as l " +
            "WHERE function('distance',:latitude,:longitude,l.latitude,l.longitude) <= l.radius " +
            "ORDER BY function('distance',:latitude,:longitude,l.latitude,l.longitude)")
    List<Location> getNearestLocations(double latitude, double longitude);

    @Query(nativeQuery = true, value = "SELECT distance(?1,?2,l.latitude,l.longitude) " +
            "FROM locations AS l")
    List<Double> getDistances(double latitude, double longitude);
}
