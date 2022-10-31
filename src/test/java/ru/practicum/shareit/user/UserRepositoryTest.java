package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserStorage userStorage;

    @Test
    @DirtiesContext
    void testSaveUser() {
        User user = new User();
        user.setName("user");
        user.setEmail("user@user.com");

        userStorage.save(user);
        assertThat(user.getId(), notNullValue());
    }
}
