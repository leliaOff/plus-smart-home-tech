package ru.yandex.practicum.exceptions;

public class DeserializationException extends RuntimeException {
    public DeserializationException(String message) {
        super(message);
    }
}
