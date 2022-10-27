package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addNewItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") Long requestId) {
        itemRequestDto.setRequestId(requestId);
        ItemRequestDto newItemRequest = itemRequestService.addItemRequest(itemRequestDto);
        log.info("Добавлен новый объект: {}",newItemRequest);
        return newItemRequest;
    }

    @GetMapping
    public List<ItemRequestDto> getAllItemRequestByRequest(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Запрошен список ItemRequest для пользователя с ID: {}",requestorId);

        return  itemRequestService.getAllItemRequest(requestorId);
    }

    @GetMapping(value = "/all")
    public List<ItemRequestDto> getPageItemRequestByRequest(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                                              @RequestParam(defaultValue = "1") @PositiveOrZero int from,
                                                              @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрошен список ItemRequest для пользователя с ID: {}", requestorId);
        return itemRequestService.getAllItemRequest(requestorId, from, size);
    }

    @GetMapping(value = "/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                             @PathVariable Long requestId) {
        log.info("Запрошен ItemRequest с ID: {}",requestId);
        return  itemRequestService.getItemRequestById(requestId,requestorId);
    }
}
