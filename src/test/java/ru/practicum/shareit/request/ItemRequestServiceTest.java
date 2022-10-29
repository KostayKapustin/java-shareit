package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {

    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private UserService userService;

    private User user = new User();
    private final Item item1 = new Item();
    private ItemRequest itemRequest = new ItemRequest();


    @BeforeEach
    public void reparationTest() {
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@user.com");
        userService.addUser(user);

        item1.setOwner(user);
        item1.setName("item1 item1");
        item1.setDescription("item1 description");
        item1.setAvailable(true);

        itemRequest.setId(1L);
        itemRequest.setDescription("Test");
        itemRequest.setRequest(user);
        itemRequestService.addItemRequest(ItemRequestMapper.toItemRequestDto(itemRequest));
    }

    @Test
    @DirtiesContext
    @Transactional
    void addAndGetItemRequest() {
        ItemRequest itemRequestForCheck = itemRequestService.getItemRequestById(itemRequest.getId());
        List<ItemRequestDto> itemRequestList = itemRequestService.getAllItemRequest(user.getId());

        assertThat(itemRequestList.size(), equalTo(1));
        assertThat(itemRequestForCheck.getDescription(),equalTo("Test"));

        assertThat(itemRequestForCheck.getId(),equalTo(1L));

        assertThrows(NoSuchElementException.class, () -> itemRequestService.getItemRequestById(100L));

        Item item = ItemMapper.toItemRequest(ItemMapper.toItemDto(item1), user, itemRequest);
    }

    @Test
    @DirtiesContext
    @Transactional
    void getItemRequestById() {
        ItemRequestDto itemRequestDto = itemRequestService.getItemRequestById(itemRequest.getId(), user.getId());
        assertThat(itemRequestDto.getId(),equalTo(1L));
        assertThat(itemRequestDto.getDescription(),equalTo("Test"));
        assertThrows(Exception.class, () -> itemRequestService.getItemRequestById(100L, user.getId()));
    }

    @Test
    @DirtiesContext
    @Transactional
    void getAllItemRequest() {
        List<ItemRequestDto> itemRequestDtoList = itemRequestService.getAllItemRequest(1L,1,5);
        assertThat(itemRequestDtoList.size(),equalTo(0));
        assertThrows(ResponseStatusException.class, () ->
                itemRequestService.getAllItemRequest(1L,-1,1));
    }
}
