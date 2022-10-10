package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.RecurException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto addUser(UserDto userDto) {
        validateEmail(userDto);
        User user = UserMapper.toUser(userDto);
        User saveUser = userStorage.addUser(user);
        return UserMapper.toUserDto(saveUser);
    }

    @Override
    public UserDto findUserById(Long id) {
        checkingUserById(id);
        User user = userStorage.findUserById(id);
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<UserDto> usersDto;
        List<User> users = userStorage.findAll();
        usersDto = users.stream()
                .map(user -> UserMapper.toUserDto(user))
                .collect(Collectors.toList());
        return usersDto;
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        checkingUserById(id);
        User user = userStorage.findUserById(id);
        if (userDto.getName() == null || userDto.getName().isEmpty() || userDto.getName().isBlank()) {
            userDto.setName(user.getName());
        }
        user.setName(userDto.getName());
        validateEmail(userDto);
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty() || userDto.getEmail().isBlank()) {
            userDto.setEmail(user.getEmail());
        }
        user.setEmail(userDto.getEmail());
        UserDto userDtoNew = UserMapper.toUserDto(user);
        userStorage.updateUser(user);
        return userDtoNew;
    }

    @Override
    public void deleteUserById(Long id) {
        checkingUserById(id);
        userStorage.deleteUserById(id);
    }

    public void validateEmail(UserDto userDto) {
        List<UserDto> users = findAll();
        for (UserDto newUserDto : users) {
            if (newUserDto.getEmail().equals(userDto.getEmail())) {
                throw new RecurException(String.format("Пользователь с e-mail %s уже существует",
                        userDto.getEmail()));
            }
        }
    }

    @Override
    public void checkingUserById(Long id) {
        List<User> usersList = userStorage.findAll();
        Map<Long, User> usersMap = new HashMap<>();
        for (User user : usersList) {
            usersMap.put(user.getId(), user);
        }
        if (!usersMap.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", id));
        }
    }
}
