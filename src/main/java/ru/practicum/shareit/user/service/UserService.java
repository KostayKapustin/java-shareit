package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User getUserById(Long id);

    List<User> getAllUsers();

    User addUser(User user);

    User updateUserById(Long id, User user);

    void deleteUserById(long id);
}
