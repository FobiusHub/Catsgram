package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;

@RestControllerAdvice
public class ErrorHandler {
    //Для исключения NotFoundException должен возвращаться код ответа 404 (Not found), в поле error — сообщение из исключения.
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandle(final NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    //Для исключения DuplicatedDataException — код ответа 409 (Conflict), в поле error — сообщение из исключения.
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse duplicatedDataHandle(final DuplicatedDataException e) {
        return new ErrorResponse(e.getMessage());
    }

    //Для исключения ConditionsNotMetException — код ответа 422 (Unprocessable Entity), в поле error — сообщение из исключения.
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse conditionsNotMetHandle(final ConditionsNotMetException e) {
        return new ErrorResponse(e.getMessage());
    }

    //Для исключения ParameterNotValidException — код ответа 400 (Bad request), в поле error — сообщение вида
    //Некорректное значение параметра <parameter>: <reason>, где вместо <parameter> и <reason> подставлены соответствующие значения.
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse parameterNotValidHandle(final ParameterNotValidException e) {
        return new ErrorResponse("Некорректное значение параметра " + e.getParameter() + ": " + e.getReason());
    }

    //Для любого другого исключения (Throwable) — код ответа 500 (Internal server error) и сообщение "Произошла непредвиденная ошибка.".
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse parameterNotValidHandle(final Throwable e) {
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}
