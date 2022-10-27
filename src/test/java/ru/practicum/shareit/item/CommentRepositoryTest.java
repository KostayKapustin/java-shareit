package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private ItemStorage itemStorage;

    @Autowired
    private CommentStorage commentStorage;

    @Test
    @DirtiesContext
    void findAllByBookerOrderByStartDescTest() {
        Item item = new Item();
        item.setName("item");
        item.setId(1L);
        item.setDescription("item");
        itemStorage.save(item);
        commentStorage.findAllByItemOrderByIdDesc(item);
    }
}
