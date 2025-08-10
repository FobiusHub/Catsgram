package ru.yandex.practicum.catsgram.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = {"id"})
public class Post {
    private Long id;
    private long authorId;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    private Instant postDate;
}
