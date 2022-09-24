package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    User addUser(User user);
    User findUserById(Long id);
    List<User> findAll();
    User updateUser(User user);
    void deleteUserById(Long id);
}
