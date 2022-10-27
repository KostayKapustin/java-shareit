package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private ItemRequestStorage itemRequestStorage;

    private final User user1 = new User();
    private final User user2 = new User();
    private final Item item1 = new Item();
    private Item item2 = new Item();

    private final ItemRequest itemRequest = new ItemRequest();

    @BeforeEach
    public void reparationTest() {
        user1.setName("user1");
        user1.setEmail("user1@user.com");
        userStorage.save(user1);

        user2.setName("user2");
        user2.setEmail("user2@user.com");
        userStorage.save(user2);

        itemRequest.setDescription("Test");
        itemRequest.setRequest(user2);
        itemRequestStorage.save(itemRequest);

        item1.setOwner(user1);
        item1.setName("item1 name");
        item1.setDescription("item1 description");
        item1.setAvailable(true);
        item1.setRequest(itemRequest);
        itemStorage.save(item1);

        item2.setOwner(user2);
        item2.setName("item2 name");
        item2.setDescription("item1 description");
        item2.setAvailable(true);
        itemStorage.save(item2);
    }

    @Test
    @DirtiesContext
    void findAllByOwnerOrderByIdAscTest() {
        assertThat(itemStorage.findAllByOwnerOrderByIdAsc(user1).size(), equalTo(1));
    }

    @Test
    @DirtiesContext
    void findAllByRequestTest() {
        assertThat(itemStorage.findAllByRequest(itemRequest).size(), equalTo(1));
    }
}
