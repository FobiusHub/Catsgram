package ru.yandex.practicum.catsgram.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    // название ошибки
    String error;
}
