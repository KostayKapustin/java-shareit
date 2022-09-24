package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        if (userService.findUserById(userId) != null) {
            Item item = ItemMapper.toItem(itemDto);
            item.setOwner(UserMapper.toUser(userService.findUserById(userId)));
            return itemStorage.addItem(userId, item);
        } else {
            return null;
        }
    }

    @Override
    public ItemDto getItem(long itemId) {
        return ItemMapper.toItemDto(itemStorage.getItem(itemId));
    }

    @Override
    public Collection<ItemDto> getAllItem(Long userId) {
        return itemStorage.getAllItem(userId);
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, long itemId) {
        if (userService.findUserById(userId) != null) {
            return itemStorage.updateItem(userId, itemDto, itemId);
        } else {
            return null;
        }
    }
    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (!text.isEmpty()) {
            return itemStorage.searchItems(text);
        } else {
            return new ArrayList<>();
        }
    }
}
