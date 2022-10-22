package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class CommentDto {
    private Long id;

    @NotEmpty(message = "Text не может быть равен null!")
    private String text;
    private Long authorId;
    private String authorName;
    private LocalDateTime created;

    public CommentDto(Long id, String text, Long authorId, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.authorId = authorId;
        this.authorName = authorName;
        this.created = created;
    }
}
