package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DirtiesContext
    @Transactional
    void addAndDeleteUserTest() {
        User user = new User();
        user.setName("user");
        user.setEmail("user@user.com");
        userService.addUser(user);

        List<User> userList = userService.getAllUsers();
        User user1 = userService.getUserById(1L);
        assertThat(userList.size(), equalTo(1));
        assertThat(user1.getName(), equalTo("user"));
        assertThrows(ResponseStatusException.class, () -> userService.getUserById(2L));

        User user2 = new User();
        user2.setName("user");
        assertThrows(ResponseStatusException.class, () -> userService.addUser(user2));
        userService.deleteUserById(1L);
    }

    @Test
    @DirtiesContext
    @Transactional
    void addUserDuplicateEmailTest() {
        User user1 = new User();
        user1.setName("user1");
        user1.setEmail("user@user.com");
        userService.addUser(user1);

        User user2 = new User();
        user2.setName("user2");
        user2.setEmail("user@user.com");
        assertThrows(DataIntegrityViolationException.class, () -> userService.addUser(user2));
    }

    @Test
    @DirtiesContext
    @Transactional
    void updateUserByIdTest() {
        User user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@user.com");
        userService.addUser(user1);
        assertThat(userService.getUserById(1L).getEmail(), equalTo("user1@user.com"));

        User user2 = new User();
        user2.setName("user1");
        user2.setEmail("user2@user.com");
        userService.updateUserById(1L, user2);
        assertThat(userService.getUserById(1L).getEmail(), equalTo("user2@user.com"));

        User user3 = new User();
        user3.setEmail("user3@user.com");
        userService.updateUserById(1L, user3);
        assertThat(userService.getUserById(1L).getEmail(), equalTo("user3@user.com"));

        User user4 = new User();
        user4.setEmail("user4@user.com");
        assertThrows(ResponseStatusException.class, () -> userService.updateUserById(10L, user4));
    }
}
