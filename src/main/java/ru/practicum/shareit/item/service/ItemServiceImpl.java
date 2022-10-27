package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final CommentStorage commentStorage;

    @Override
    public Item addItem(Item item) {
        checkIncomingItem(item);
        return itemStorage.save(item);
    }

    @Override
    public List<Item> getAllItemsByOwner(User owner) {
        return itemStorage.findAllByOwnerOrderByIdAsc(owner);
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> outgoingList = new ArrayList<>();
        List<Item> incomingList = itemStorage.findAll();
        for (Item item: incomingList) {
            if (item.getDescription().toLowerCase().contains(text.toLowerCase()) && item.getAvailable()) {
                outgoingList.add(item);
            }
        }
        return outgoingList;
    }

    @Override
    public Item updateItem(Item item) {
        Item currentItem = getItemById(item.getId());
        if (!item.getOwner().equals(currentItem.getOwner())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
        Boolean availableItem = item.getAvailable();
        String nameItem = item.getName();
        String descriptionItem = item.getDescription();
        if (availableItem != null) {
            currentItem.setAvailable(availableItem);
        }
        if (nameItem != null) {
            currentItem.setName(nameItem);
        }
        if (descriptionItem != null) {
            currentItem.setDescription(descriptionItem);
        }
        itemStorage.save(currentItem);
        return currentItem;
    }

    @Override
    public Item getItemById(Long id) {
        try {
            return itemStorage.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public CommentDto addComment(Item item, User booker, Comment comment, Boolean checkFlag) {
        if (!checkFlag) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Comment commentTmp =  commentStorage.save(comment);
        return CommentMapper.toCommentDto(commentTmp);
    }

    @Override
    public List<CommentDto> getCommentsByItem(Item item) {
        List<Comment> comments = commentStorage.findAllByItemOrderByIdDesc(item);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment: comments) {
            commentDtoList.add(CommentMapper.toCommentDto(comment));
        }
        return commentDtoList;
    }

    @Override
    public List<ItemDto> getAllItemsByRequest(ItemRequest itemRequest) {
        List<Item> items = itemStorage.findAllByRequest(itemRequest);
        List<ItemDto> itemDto = new ArrayList<>();
        for (Item item: items) {
            itemDto.add(ItemMapper.toItemDto(item));
        }
        return itemDto;
    }

    private void checkIncomingItem(Item item) {
        try {
            getUserById(item.getOwner().getId());
        } catch (NullPointerException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
        if (item.getAvailable() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST);
        }
        String nameItem = item.getName();
        if (nameItem == null || nameItem.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST);
        }
        String descriptionItem = item.getDescription();
        if (descriptionItem == null || descriptionItem.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST);
        }
    }

    public User getUserById(Long id) {
        try {
            return userStorage.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


}
