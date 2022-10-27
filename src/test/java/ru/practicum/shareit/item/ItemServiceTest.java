package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRequestService itemRequestService;

    private final User user = new User();
    private final User user2 = new User();
    private final User anotherUser = new User();
    private final Item item1 = new Item();
    private final Item item2 = new Item();
    private final Item item3 = new Item();
    private final Booking booking1 = new Booking();
    private final Booking booking2 = new Booking();
    private final Booking booking3 = new Booking();
    private final Booking booking4 = new Booking();

    @BeforeEach
    public void reparationTest() {
        user.setName("user");
        user.setEmail("user@user.com");
        userService.addUser(user);

        user2.setName("user2");
        user2.setEmail("user2@user.com");
        userService.addUser(user2);

        anotherUser.setName("another");
        anotherUser.setEmail("another@user.com");

        item1.setOwner(user);
        item1.setName("item1 item1");
        item1.setDescription("item1 description");
        item1.setAvailable(true);

        item2.setOwner(user);
        item2.setName("item2 item2");
        item2.setDescription("item2 description");
        item2.setAvailable(true);

        item3.setOwner(anotherUser);
        item3.setName("item3 item3");
        item3.setDescription("item3 description");
        item3.setAvailable(true);

        booking1.setItem(item1);
        booking1.setBooker(user2);
        booking1.setStart(LocalDateTime.of(2023, 1, 1, 10, 0));
        booking1.setEnd(LocalDateTime.of(2023, 1, 10, 10, 0));
        booking1.setStatus(WAITING);

        booking2.setItem(item2);
        booking2.setBooker(user2);
        booking2.setStart(LocalDateTime.of(2022, 1, 1, 10, 0));
        booking2.setEnd(LocalDateTime.of(2023, 1, 10, 10, 0));
        booking2.setStatus(WAITING);

        booking3.setItem(item2);
        booking3.setBooker(user2);
        booking3.setStart(LocalDateTime.of(2022, 1, 1, 10, 0));
        booking3.setEnd(LocalDateTime.of(2022, 1, 10, 10, 0));
        booking3.setStatus(APPROVED);

        booking4.setItem(item2);
        booking4.setBooker(user2);
        booking4.setStart(LocalDateTime.of(2023, 2, 1, 10, 0));
        booking4.setEnd(LocalDateTime.of(2023, 2, 10, 10, 0));
        booking4.setStatus(WAITING);
    }

    @Test
    @DirtiesContext
    @Transactional
    void addItemTest() {
        assertThat(itemService.getAllItemsByOwner(user)
                        .size(),
                equalTo(0));
        assertThat(itemService.addItem(item1)
                        .getId(),
                equalTo(1L));
        assertThat(itemService.getAllItemsByOwner(user)
                        .size(),
                equalTo(1));
        assertThrows(InvalidDataAccessApiUsageException.class, () -> itemService.addItem(item3));

        Item item4 = new Item();
        item4.setOwner(anotherUser);
        item4.setName("item3 item3");
        item4.setDescription("item3 description");
        assertThrows(InvalidDataAccessApiUsageException.class, () -> itemService.addItem(item4));

        Item item5 = new Item();
        item5.setOwner(anotherUser);
        item5.setName("item3 item3");
        item5.setDescription("item3 description");
        userService.addUser(anotherUser);
        assertThrows(ResponseStatusException.class, () -> itemService.addItem(item5));

        Item item6 = new Item();
        item6.setOwner(anotherUser);
        item6.setName("");
        item6.setDescription("item3 description");
        item6.setAvailable(true);
        assertThrows(ResponseStatusException.class, () -> itemService.addItem(item6));

        Item item7 = new Item();
        item7.setOwner(anotherUser);
        item7.setName("userName");
        item7.setAvailable(true);
        assertThrows(ResponseStatusException.class, () -> itemService.addItem(item7));
    }

    @Test
    @DirtiesContext
    @Transactional
    void searchItemTest() {
        itemService.addItem(item1);
        assertThat(itemService.searchItem("description")
                        .size(),
                equalTo(1));
        assertThat(itemService.searchItem("")
                        .size(),
                equalTo(1));
    }

    @Test
    @DirtiesContext
    @Transactional
    void updateItemTest() {
        itemService.addItem(item1);
        Item item4 = new Item();
        item4.setDescription("item4 description");
        assertThrows(InvalidDataAccessApiUsageException.class, () -> itemService.updateItem(item4));
        item4.setId(1L);
        item4.setOwner(user);
        assertThat(itemService.updateItem(item4)
                        .getDescription(),
                equalTo("item4 description"));
        item4.setOwner(user2);
        assertThrows(ResponseStatusException.class, () -> itemService.updateItem(item4));
    }

    @Test
    @DirtiesContext
    @Transactional
    void getItemByIdTest() {
        itemService.addItem(item1);
        assertThat(itemService.getItemById(1L)
                        .getName(),
                equalTo("item1 item1"));
        assertThrows(ResponseStatusException.class, () -> itemService.getItemById(2L));
    }

    @Test
    @DirtiesContext
    @Transactional
    void addCommentTest() {
        Comment comment = new Comment();
        comment.setText("");
        comment.setItem(item1);
        comment.setUser(user2);
        comment.setId(1L);
        assertThrows(ConstraintViolationException.class,
                () -> itemService.addComment(item1, user2, comment, true));
        comment.setText("TextNew");
        itemService.addComment(item1, user2, comment, true);
        assertThrows(InvalidDataAccessApiUsageException.class, () -> itemService.getCommentsByItem(item1));
        item1.setId(1L);
        userService.addUser(user);
        userService.addUser(user2);
        itemService.addItem(item1);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequest(user2);
        itemRequest.setDescription("test");
        itemRequestService.addItemRequest(ItemRequestMapper.toItemRequestDto(itemRequest));
        itemService.addComment(item1, user2, comment, true);
        assertThrows(InvalidDataAccessApiUsageException.class, () -> itemService.getAllItemsByRequest(itemRequest));
    }
}

