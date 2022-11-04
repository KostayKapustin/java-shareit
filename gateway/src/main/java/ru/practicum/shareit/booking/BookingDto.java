package ru.practicum.shareit.booking;

import lombok.*;
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
}
