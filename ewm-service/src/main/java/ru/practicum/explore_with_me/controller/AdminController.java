package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.model.category.CategoryDto;
import ru.practicum.explore_with_me.model.compilation.CompilationDto;
import ru.practicum.explore_with_me.model.compilation.ReturnCompilationDto;
import ru.practicum.explore_with_me.model.event.AdminUpdateEventDto;
import ru.practicum.explore_with_me.model.event.EventState;
import ru.practicum.explore_with_me.model.event.FindUserEventOptions;
import ru.practicum.explore_with_me.model.event.FullEventDto;
import ru.practicum.explore_with_me.model.location.LocationDto;
import ru.practicum.explore_with_me.model.user.ReturnUserDto;
import ru.practicum.explore_with_me.model.user.UserDto;
import ru.practicum.explore_with_me.controller.service.category.CategoryService;
import ru.practicum.explore_with_me.controller.service.compilation.CompilationService;
import ru.practicum.explore_with_me.controller.service.event.EventService;
import ru.practicum.explore_with_me.controller.service.location.LocationService;
import ru.practicum.explore_with_me.controller.service.user.UserService;
import ru.practicum.explore_with_me.validation.Validation;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;

    private final LocationService locationService;

    @PostMapping("/locations")
    @Validated(Validation.OnCreate.class)
    public LocationDto createLocation(@Valid @RequestBody LocationDto location) {
        return locationService.createLocation(location);
    }

    @PatchMapping("/locations")
    @Validated(Validation.OnPatch.class)
    public LocationDto updateLocation(@Valid @RequestBody LocationDto location) {
        return locationService.patchLocation(location);
    }

    @DeleteMapping("/locations")
    public void deleteLocation(long id) {
        locationService.deleteLocation(id);
    }

    @GetMapping("/events/{locId}")
    public List<FullEventDto> getEventsInLocation(@PathVariable("locId") Long locationId,
                                                  @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return eventService.getEventsInLocation(locationId, from, size);
    }

    @GetMapping("/users")
    public List<ReturnUserDto> getUsersById(@RequestParam(value = "ids") Set<Long> userIds,
                                            @RequestParam(value = "from", defaultValue = "0") Integer from,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return userService.getUsersByIdWithPagination(userIds, from, size);
    }

    @PostMapping("/users")
    @Validated(Validation.OnCreate.class)
    public ReturnUserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable(name = "userId") Long userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("/categories")
    @Validated(Validation.OnCreate.class)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping("/categories")
    @Validated(Validation.OnPatch.class)
    public CategoryDto patchCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.patchCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable(name = "catId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @PostMapping("/compilations")
    public ReturnCompilationDto createCompilation(@Valid @RequestBody CompilationDto compilationDto) {
        return compilationService.createCompilation(compilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable(name = "compId") Long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public CompilationDto deleteEventFromCompilation(@PathVariable(name = "compId") Long compilationId,
                                                     @PathVariable(name = "eventId") Long eventId) {
        return compilationService.deleteEventFromCompilation(compilationId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public CompilationDto addEventToCompilation(@PathVariable(name = "compId") Long compilationId,
                                                @PathVariable Long eventId) {
        return compilationService.addEventToCompilation(compilationId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public CompilationDto unpinCompilation(@PathVariable(name = "compId") Long compilationId) {
        return compilationService.unpinCompilation(compilationId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public CompilationDto pinCompilation(@PathVariable(name = "compId") Long compilationId) {
        return compilationService.pinCompilation(compilationId);
    }

    @GetMapping("/events")
    public List<FullEventDto> findEvents(@RequestParam(value = "users") Set<Long> userIds,
                                         @RequestParam(value = "states") Set<EventState> states,
                                         @RequestParam(value = "categories") Set<Long> categoryIds,
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                         @RequestParam(value = "rangeStart") String start,
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                         @RequestParam(value = "rangeEnd") String end,
                                         @RequestParam(value = "from", defaultValue = "0") Integer from,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        FindUserEventOptions options = new FindUserEventOptions(userIds, states, categoryIds, start, end);

        return eventService.findEvents(options, from, size);
    }

    @PutMapping("/events/{eventId}")
    public FullEventDto updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody AdminUpdateEventDto adminUpdateEventDto) {
        return eventService.adminUpdateEvent(eventId, adminUpdateEventDto);
    }

    @PatchMapping("/events/{eventId}/publish")
    public FullEventDto publishEvent(@PathVariable Long eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public FullEventDto rejectEvent(@PathVariable Long eventId) {
        return eventService.rejectEvent(eventId);
    }
}
