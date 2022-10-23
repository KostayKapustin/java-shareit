package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;

    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, Long itemId, Long bookerId, BookingStatus status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.bookerId = bookerId;
        this.status = status;
    }
}
