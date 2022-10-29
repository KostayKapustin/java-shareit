package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestStorage itemRequestStorage;

    @BeforeEach
    public void reparationTest() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@user.com");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Test");
        itemRequest.setRequest(user);
        itemRequestStorage.save(itemRequest);
    }

    @Test
    @DirtiesContext
    void findAllByRequestIdOrderById() {
        List<ItemRequest> requestList = itemRequestStorage
                .findAllByRequestIdOrderById(1L);
        assertThat(requestList, notNullValue());
    }

    @Test
    @DirtiesContext
    void findAllByRequestIdNotOrderById() {
        Page<ItemRequest> requestList = itemRequestStorage
                .findAllByRequestIdNotOrderById(2L, PageRequest.of(1, 10));
        assertThat(requestList, notNullValue());
    }
}
