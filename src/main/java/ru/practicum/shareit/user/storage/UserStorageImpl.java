package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long userId = 0L;

    @Override
    public User addUser(User user) {
        user.setId(++userId);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {} ", user);
        return user;
    }

    @Override
    public User findUserById(Long id) {
        User user = users.get(id);
        log.info("Найден пользователь: {}", user);
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>(users.values());
        log.info("Найдены пользователи: {} ", userList);
        return userList;
    }

    @Override
    public User updateUser(User user) {
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.info("Обновлена информация о пользователе: {}", user);
        return user;
    }

    @Override
    public void deleteUserById(Long id) {
        users.remove(id);
        log.info("Удален пользователь: {}", id);
    }
}

