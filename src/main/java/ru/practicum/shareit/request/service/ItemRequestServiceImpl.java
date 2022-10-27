package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    @Transactional
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto) {
        User request = userService.getUserById(itemRequestDto.getRequestId());
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, request);
        return ItemRequestMapper.toItemRequestDto(itemRequestStorage.save(itemRequest));
    }

    @Override
    public ItemRequest getItemRequestById(Long requestId) {
        return itemRequestStorage.findById(requestId).get();
    }

    @Override
    public ItemRequestDto getItemRequestById(Long requestId, Long requesterId) {
        ItemRequest itemRequest;
        userService.getUserById(requesterId);
        try {
            itemRequest = itemRequestStorage
                    .findById(requestId).get();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemService.getAllItemsByRequest(itemRequest));
        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequest(Long requestId) {
        userService.getUserById(requestId);
        List<ItemRequest> itemRequestList = itemRequestStorage
                .findAllByRequestIdOrderById(requestId);
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequestList) {
            ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
            itemRequestDto.setItems(itemService.getAllItemsByRequest(itemRequest));
            itemRequestDtoList.add(itemRequestDto);
        }
        return itemRequestDtoList;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequest(Long requestId, int from, int size) {
        if (from < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST);
        }
        userService.getUserById(requestId);
        Page<ItemRequest> itemRequestPage = itemRequestStorage
                .findAllByRequestIdNotOrderById(requestId, PageRequest.of(from / size, size));
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequestPage) {
            ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
            itemRequestDto.setItems(itemService.getAllItemsByRequest(itemRequest));
            itemRequestDtoList.add(itemRequestDto);
        }
        return itemRequestDtoList;
    }
}
