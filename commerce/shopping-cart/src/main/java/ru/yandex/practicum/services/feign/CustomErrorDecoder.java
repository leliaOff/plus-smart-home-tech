package ru.yandex.practicum.services.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.ProductNotFoundException;

@Slf4j
@Component
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus httpStatus = HttpStatus.resolve(response.status());
        log.error("FEIGN: method {}, status {}, cause {}", methodKey, response.status(), response.reason());
        assert httpStatus != null;
        return switch (httpStatus) {
            case NOT_FOUND -> new ProductNotFoundException("Продукт не найден");
            case BAD_REQUEST -> new BadRequestException("Bad Request");
            case INTERNAL_SERVER_ERROR -> new InternalServerErrorException("Internal Server Error");
            default -> defaultErrorDecoder.decode(methodKey, response);
        };
    }
}