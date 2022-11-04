package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Validated
public class CommentDto {
    private Long id;

    @NotEmpty(message = "Text не может быть равен null!")
    private String text;
    private Long authorId;
    private String authorName;
    private LocalDateTime created;
}
