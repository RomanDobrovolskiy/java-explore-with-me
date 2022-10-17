package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.model.event.Event;
import ru.practicum.explore_with_me.model.event.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(long userId, Pageable page);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :categoryIds) AND " +
            "(LOWER(e.annotation) LIKE CONCAT('%',LOWER(:text),'%') OR " +
            "LOWER(e.description) LIKE CONCAT('%',LOWER(:text),'%')) AND " +
            "e.eventDate > :now AND " +
            "e.state = 'PUBLISHED' AND " +
            "e.paid = :paid")
    List<Event> findAfterDate(String text, Collection<Long> categoryIds, boolean paid, LocalDateTime now);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :categoryIds) AND " +
            "(LOWER(e.annotation) LIKE CONCAT('%',LOWER(:text),'%') OR " +
            "LOWER(e.description) LIKE CONCAT('%',LOWER(:text),'%')) AND " +
            "(e.eventDate >= :start AND e.eventDate <= :end) AND " +
            "e.state = 'PUBLISHED' AND " +
            "e.paid = :paid")
    List<Event> findBetweenDates(String text, Collection<Long> categoryIds, boolean paid, LocalDateTime start,
                                 LocalDateTime end);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :categoryIds) AND " +
            "(e.initiator.id IN :userIds) AND " +
            "(e.state IN :states) AND " +
            "(e.eventDate >= :start AND e.eventDate <= :end)")
    List<Event> findBetweenDatesByUsersStatesCategories(Collection<Long> userIds, Collection<EventState> states,
                                                        Collection<Long> categoryIds, LocalDateTime start,
                                                        LocalDateTime end, Pageable pageable);
    @Query("SELECT e FROM Event as e " +
            "WHERE function('distance',:latitude,:longitude,e.latitude,e.longitude) <= :radius " +
            "ORDER BY function('distance',:latitude,:longitude,e.latitude,e.longitude)")
    List<Event> getEventsNearPoint(double latitude, double longitude, double radius, Pageable pageable);

    @Query("SELECT e FROM Event as e " +
            "WHERE function('distance',:latitude,:longitude,e.latitude,e.longitude) <= :radius " +
            "AND e.state = 'PUBLISHED' " +
            "ORDER BY function('distance',:latitude,:longitude,e.latitude,e.longitude)")
    List<Event> getPublicEventsNearPoint(double latitude, double longitude, double radius, Pageable pageable);
}