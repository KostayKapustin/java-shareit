package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User getUserById(Long id) {
        try {
            return userStorage.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    @Override
    public User addUser(User user) {
        checkNewUser(user);
        return userStorage.save(user);
    }

    @Override
    public User updateUserById(Long id, User user) {
        User currentUser = getUserById(id);
        user.setId(id);
        if (user.getName() != null) {
            currentUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            currentUser.setEmail(user.getEmail());
        }
        userStorage.save(currentUser);
        return currentUser;
    }

    @Override
    public void deleteUserById(long id) {
        userStorage.findById(id).get().getEmail();
        userStorage.deleteById(id);
    }

    private void checkNewUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            log.error("Ошибка добавления пользователя. Пустой email");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


}
