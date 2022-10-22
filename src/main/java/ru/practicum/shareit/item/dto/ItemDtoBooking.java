package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class ItemDtoBooking {
    private Long id;
    private String name;

    public ItemDtoBooking(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
