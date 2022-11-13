package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(
            @Valid @RequestBody ItemRequestDto itemRequestDto,
            @Positive @RequestHeader("X-Sharer-User-Id") Long requestorId) {

        return itemRequestClient.addRequest(requestorId,itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestByRequestor(
            @Positive @RequestHeader("X-Sharer-User-Id") Long requestorId) {

        return  itemRequestClient.getAllItemRequest(requestorId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getPageItemRequestByRequestor(
            @Positive @RequestHeader("X-Sharer-User-Id") Long requestorId,
            @RequestParam(defaultValue = "1") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {

        return itemRequestClient.getAllItemRequest(requestorId, from, size);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @Positive @RequestHeader("X-Sharer-User-Id") Long requestorId,
            @PathVariable Long requestId) {

        return itemRequestClient.getItemRequestById(requestorId,requestId);
    }

}
