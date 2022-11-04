package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.ItemDto;

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

    public ItemRequestDto(Long id, String description, Long requestId, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestId = requestId;
        this.created = created;
    }
}
