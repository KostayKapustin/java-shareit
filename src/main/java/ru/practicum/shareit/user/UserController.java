package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public List<UserDto> getAllUsers() {
        List<User> incomingList = userService.getAllUsers();
        List<UserDto> outgoingList = new ArrayList<>();
        log.info("Запрошен список всех объектов. Текущее количество объектов: {}",incomingList.size());

        for (User user: incomingList) {
            outgoingList.add(UserMapper.toUserDto(user));
        }
        return outgoingList;
    }

    @PostMapping(value = "")
    public UserDto addUser(@Valid @RequestBody User user) {
        User newUser = userService.addUser(user);
        log.info("Добавлен новый объект: {}",newUser);

        return UserMapper.toUserDto(newUser);
    }

    @PatchMapping(value = "/{id}")
    public UserDto updateUserById(@Valid @RequestBody User user,@PathVariable Long id) {
        User updateUser = userService.updateUserById(id, user);
        log.info("Изменен объект: {}",updateUser);

        return UserMapper.toUserDto(updateUser);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        log.info("Запрошен объект: {}",user);
        return UserMapper.toUserDto(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable @Valid @RequestBody Long id) {
        userService.deleteUserById(id);
        log.info("Удален пользователь с ID {}", id);
    }

}
