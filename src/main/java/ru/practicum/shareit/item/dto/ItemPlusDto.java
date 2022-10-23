package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class ItemPlusDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
}
