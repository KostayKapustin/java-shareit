package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPlusDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @PostMapping
    public ItemDto addItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemDto.setOwner(userId);
        User owner = userService.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto,owner);
        return ItemMapper.toItemDto(itemService.addItem(item));
    }

    @GetMapping("")
    public List<ItemPlusDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        User owner = userService.getUserById(userId);
        List<Item> incomingList = itemService.getAllItemsByOwner(owner);
        List<ItemPlusDto> outgoingList = new ArrayList<>();

        log.info("Запрошен список всех объектов. Текущее количество объектов: {}",incomingList.size());
        for (Item item: incomingList) {
            ItemPlusDto itemDto = ItemMapper.toItemPlusDto(item);
            itemDto.setLastBooking(bookingService.getLastBookingForOwner(item,owner,userId));
            itemDto.setNextBooking(bookingService.getNextBookingForOwner(item,owner,userId));
            outgoingList.add(itemDto);
        }
        return outgoingList;
    }

    @PatchMapping(value = "/{id}")
    public ItemDto updateItemById(@Valid @RequestBody ItemDto itemDto,
                                  @PathVariable Long id,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemDto.setOwner(userId);
        itemDto.setId(id);
        User owner = userService.getUserById(userId);
        log.info("Изменен объект: {}",itemDto);

        Item updateItem = itemService.updateItem(ItemMapper.toItem(itemDto,owner));
        return ItemMapper.toItemDto(updateItem);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ItemPlusDto getItemById(@PathVariable Long id,
                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = itemService.getItemById(id);
        User owner = item.getOwner();
        log.info("Запрошен объект: {}",item);
        ItemPlusDto itemDto = ItemMapper.toItemPlusDto(item);
        itemDto.setLastBooking(bookingService.getLastBookingForOwner(item,owner,userId));
        itemDto.setNextBooking(bookingService.getNextBookingForOwner(item,owner,userId));
        itemDto.setComments(itemService.getCommentsByItem(item));
        return itemDto;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        List<ItemDto> outgoingList = new ArrayList<>();
        if (!text.isBlank()) {
            List<Item> incomingList = itemService.searchItem(text);
            for (Item item : incomingList) {
                outgoingList.add(ItemMapper.toItemDto(item));
            }
        }
        return outgoingList;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {

        User author = userService.getUserById(userId);
        Item item = itemService.getItemById(itemId);
        Boolean checkFlag = bookingService.checkBookingByBooker(item,author);
        Comment comment = CommentMapper.toComment(commentDto,item,author);
        return itemService.addComment(item,author,comment,checkFlag);
    }
}
