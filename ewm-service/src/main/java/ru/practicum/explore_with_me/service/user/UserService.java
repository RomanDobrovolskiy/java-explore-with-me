package ru.practicum.explore_with_me.service.user;

import ru.practicum.explore_with_me.model.user.ReturnUserDto;
import ru.practicum.explore_with_me.model.user.User;
import ru.practicum.explore_with_me.model.user.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    User getUser(Long userId);

    UserDto getUserDto(Long userId);

    ReturnUserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    UserDto patchUser(UserDto userDto);

    void deleteUser(Long userId);

    List<ReturnUserDto> getUsersByIdWithPagination(Collection<Long> userIds, Integer from, Integer size);
}
