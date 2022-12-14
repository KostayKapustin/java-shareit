package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequest().getId(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User request) {
        return new ItemRequest(
                itemRequestDto.getDescription(),
                request,
                LocalDateTime.now()
        );
    }
}
