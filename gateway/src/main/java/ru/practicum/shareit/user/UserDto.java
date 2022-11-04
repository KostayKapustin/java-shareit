package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;


@AllArgsConstructor
@Getter
@Setter
@ToString
@Validated
public class UserDto {
    private Long id;
    private String name;
    @Email(message = "Не правильно указан mail!")
    private String email;
}
