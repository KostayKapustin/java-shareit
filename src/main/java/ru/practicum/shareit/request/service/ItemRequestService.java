package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
     ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto);

     ItemRequest getItemRequestById(Long requestId);

     ItemRequestDto getItemRequestById(Long requestId, Long requesterId);

     List<ItemRequestDto> getAllItemRequest(Long requestId);

     List<ItemRequestDto> getAllItemRequest(Long requestId, int from, int size);
}