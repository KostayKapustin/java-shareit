package ru.practicum.shareit.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
public class ItemRequestDto {
    private Long id;

    @NotEmpty(message = "Description не может быть равен null!")
    private String description;
    private Long requestId;
    private LocalDateTime created;
    private List<ItemDto> items;
}
