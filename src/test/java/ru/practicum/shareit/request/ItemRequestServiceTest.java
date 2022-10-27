package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
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

    @Test
    @DirtiesContext
    @Transactional
    void addAndGetItemRequest() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@user.com");
        userService.addUser(user);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Test");
        itemRequest.setRequest(user);
        itemRequestService.addItemRequest(ItemRequestMapper.toItemRequestDto(itemRequest));

        ItemRequest itemRequestForCheck = itemRequestService.getItemRequestById(itemRequest.getId());
        List<ItemRequestDto> itemRequestList = itemRequestService.getAllItemRequest(user.getId());

        assertThat(itemRequestList.size(), equalTo(1));
        assertThat(itemRequestForCheck.getDescription(),equalTo("Test"));

        assertThrows(NoSuchElementException.class, () -> {
            itemRequestService.getItemRequestById(99L);
        });

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        ItemRequest itemRequest2 = ItemRequestMapper.toItemRequest(itemRequestDto,user);
    }
}
