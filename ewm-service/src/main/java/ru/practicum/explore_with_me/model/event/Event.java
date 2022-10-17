package ru.practicum.explore_with_me.model.event;

import lombok.*;
import ru.practicum.explore_with_me.model.category.Category;
import ru.practicum.explore_with_me.model.location.Location;
import ru.practicum.explore_with_me.model.request.ParticipationRequest;
import ru.practicum.explore_with_me.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "events", schema = "public")
public class Event {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    private String annotation;
    private String description;
    private String title;
    private boolean paid;
    private LocalDateTime created;
    private LocalDateTime published;
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    private Integer participantLimit;
    private boolean requestModeration;
    private Float latitude;
    private Float longitude;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ParticipationRequest> participationRequests = new ArrayList<>();
}
