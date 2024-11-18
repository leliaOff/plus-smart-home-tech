package ru.yandex.practicum.exceptions;

public class SensorHandlerNotFound extends RuntimeException {
    public SensorHandlerNotFound(String message) {
        super(message);
    }
}
