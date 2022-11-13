package ru.practicum.shareit.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.ItemDto;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    Long id;

    @NotEmpty(message = "Description не может быть равен null!")
    String description;
    Long requestId;
    LocalDateTime created;
    List<ItemDto> items;
}
