package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);
    UserDto findUserById(Long id);
    List<UserDto> findAll();
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUserById(Long id);

    void checkingUserById(Long id);
}
