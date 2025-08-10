package ru.yandex.practicum.catsgram.dto;

import lombok.Data;

@Data
public class UpdatePostRequest {
    private long authorId;
    private String description;

    public boolean hasAuthorId() {
        return authorId != 0;
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }
}
