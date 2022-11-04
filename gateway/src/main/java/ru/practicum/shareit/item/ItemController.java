package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addNewItem(@RequestBody ItemDto itemDto,
                              @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemDto.setOwner(userId);
        return itemClient.addItem(itemDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getAllByOwner(userId);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> updateItemById(@Valid @RequestBody ItemDto itemDto,
                                                 @Positive @PathVariable Long id,
                                                 @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemDto.setOwner(userId);
        itemDto.setId(id);

        return itemClient.editItem(userId,itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@Positive @PathVariable Long itemId,
                                              @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {

        return itemClient.getItem(itemId,userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam(defaultValue = "") String text,
                                             @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Long from,
                                             @Positive @RequestParam(defaultValue = "10") Long size) {
        return itemClient.getItemByText(userId,text,from,size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Positive @PathVariable Long itemId,
                                             @Valid @RequestBody CommentDto commentDto,
                                             @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
