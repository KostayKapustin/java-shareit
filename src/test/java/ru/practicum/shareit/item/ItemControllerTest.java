package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;


@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;

    @MockBean
    private BookingService bookingService;

    @MockBean
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

        user2.setName("user1");
        user2.setEmail("user1@user.com");
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
    void addItemTest() throws Exception {
        when(itemService.addItem(any()))
                .thenReturn(item1);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(ItemMapper.toItemDto(item1)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getAllItemsTest() throws Exception {
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        when(itemService.getAllItemsByOwner(user))
                .thenReturn(items);
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void updateItemByIdTest() throws Exception {
        when(itemService.updateItem(any()))
                .thenReturn(item2);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(ItemMapper.toItemDto(item1)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getItemByIdTest() throws Exception {
        when(itemService.getItemById(any()))
                .thenReturn(item1);
        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void searchItemTest() throws Exception {
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        when(itemService.searchItem(any()))
                .thenReturn(items);
        mvc.perform(get("/items/search?text=аккУМУляторная")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void addCommentTest() throws Exception {
        CommentDto comment = new CommentDto();
        comment.setText("text");
        when(itemService.addComment(any(), any(), any(), any()))
                .thenReturn(comment);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }
}
