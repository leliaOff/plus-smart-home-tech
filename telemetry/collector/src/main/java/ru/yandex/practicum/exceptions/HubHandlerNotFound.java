package ru.yandex.practicum.exceptions;

public class HubHandlerNotFound extends RuntimeException {
    public HubHandlerNotFound(String message) {
        super(message);
    }
}
