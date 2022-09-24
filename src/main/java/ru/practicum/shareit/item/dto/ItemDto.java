package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(message = "Имя не может содержать пробелы.")
    private String name;

    @NotBlank(message = "Описание не может содержать пробелы.")
    private String description;

    @NotNull(message = "Доступ не может быть пустым и равен null.")
    private Boolean available;
}
