package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    ItemDto addItem(Long userId, Item item);

    Item getItem(long itemId);

    Collection<ItemDto> getAllItem(Long userId);

    ItemDto updateItem(Long userId, ItemDto itemDto, long itemId);

    Collection<ItemDto> searchItems(String text);
}
