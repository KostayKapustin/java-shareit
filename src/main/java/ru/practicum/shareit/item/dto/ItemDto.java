package ru.practicum.shareit.item.dto;

import lombok.*;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long requestId;
}
