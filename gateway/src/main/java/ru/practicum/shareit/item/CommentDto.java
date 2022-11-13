package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;

    @NotEmpty(message = "Text не может быть равен null!")
    String text;
    Long authorId;
    String authorName;
    LocalDateTime created;
}
