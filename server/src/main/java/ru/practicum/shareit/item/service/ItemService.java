package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemService {

     Item addItem(Item item);

     List<Item> getAllItemsByOwner(User owner);

     List<Item> searchItem(String text);

     Item updateItem(Item item);

     Item getItemById(Long id);

     CommentDto addComment(Item item, User author, Comment comment, Boolean checkFlag);

     List<CommentDto> getCommentsByItem(Item item);

     List<ItemDto> getAllItemsByRequest(ItemRequest itemRequest);
}
