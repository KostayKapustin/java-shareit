package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAll();
    }

    @PostMapping
    public ResponseEntity<Object> addNewUser(@Valid @RequestBody UserDto user) {
        log.info("Добавлен новый объект: {}",user);
        return userClient.add(user);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> updateUserById(@Valid @RequestBody UserDto user,@PathVariable @Positive Long id) {
        log.info("Изменен объект с ID: {}", id);
        user.setId(id);
        return userClient.edit(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable @Positive Long id) {
        log.info("Запрошен объект с ID: {}",id);
        return userClient.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable @Positive Long id) {
        userClient.delete(id);
        log.info("Удален пользователь с ID {}", id);
    }
}
