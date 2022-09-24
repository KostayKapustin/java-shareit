package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotNull(message = "Имя не может быть пустым и равен null.")
    @NotBlank(message = "Имя не может содержать пробелы.")
    private String name;

    @NotNull(message = "Mail не может быть пустым и равен null.")
    @Email(message = "Некорректно указан Email.")
    private String email;

}

