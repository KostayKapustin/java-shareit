package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.user.dto.UserDtoBooking;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class BookingDtoItemOrUser {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDtoBooking item;
    private UserDtoBooking booker;
    private BookingStatus status;

    public BookingDtoItemOrUser(Long id, LocalDateTime start, LocalDateTime end, BookingStatus status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
    }
}
