package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Long owner;
    Long requestId;
}
