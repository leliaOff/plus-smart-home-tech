package ru.yandex.practicum.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.exceptions.NoProductsInShoppingCartException;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.models.ShoppingCart;
import ru.yandex.practicum.repositories.ShoppingCartRepository;
import ru.yandex.practicum.requests.ChangeProductQuantityRequest;
import ru.yandex.practicum.services.mappers.ShoppingCartMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository repository;
    private final ShoppingCartMapper mapper;
    private final ShoppingCartValidator validator;
    private final ShoppingStoreClient shoppingStoreClient;

    public ShoppingCartDto getOrCreate(String username) {
        log.info("Получить корзину для авторизованного пользователя или создать новую: {}", username);
        validator.validateUsername(username);

        ShoppingCart shoppingCart = repository.findByUsername(username)
                .orElseGet(() -> create(username));
        return mapper.toShoppingCartDto(shoppingCart);
    }

    @Transactional
    public ShoppingCart create(String username) {
        log.info("Создать новую корзину для пользователя: {}", username);
        ShoppingCart cart = new ShoppingCart();
        cart.setUsername(username);
        cart.setActive(true);
        cart.setProducts(new HashMap<>());
        return repository.save(cart);
    }

    @Transactional
    public ShoppingCartDto add(String username, Map<UUID, Integer> products) {
        log.info("Добавить товар в корзину пользователя: {}", username);
        validator.validateUsername(username);

        ShoppingCart shoppingCart = getActive(username);

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            ProductDto productDto = shoppingStoreClient.getProduct(entry.getKey());
            int quantity = entry.getValue();
            validator.validateProductQuantity(productDto, quantity);
            shoppingCart.getProducts().merge(productDto.getProductId(), quantity, (a, b) -> (a + b) > 0 ? (a + b) : null);
        }
        repository.save(shoppingCart);
        return mapper.toShoppingCartDto(shoppingCart);
    }

    public ShoppingCart getActive(String username) {
        log.info("Получить активную корзину для авторизованного пользователя: {}", username);
        return repository.findByUsernameAndActive(username, true)
                .orElseThrow(() -> new NoProductsInShoppingCartException("У пользователя нет актуальной корзины: " + username));
    }

    @Transactional
    public void deactivate(String username) {
        log.info("Деактивация корзины товаров пользователя: {}", username);
        validator.validateUsername(username);
        ShoppingCart shoppingCart = getActive(username);
        shoppingCart.setActive(false);
        repository.save(shoppingCart);
    }

    @Transactional
    public ShoppingCartDto remove(String username, Map<UUID, Integer> products) {
        log.info("Изменить состав товаров в корзине пользователя: {}", username);
        validator.validateUsername(username);

        ShoppingCart shoppingCart = getActive(username);

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            shoppingCart.getProducts().merge(entry.getKey(), entry.getValue(), (a, b) -> (a + b) > 0 ? (a + b) : null);
        }

        repository.save(shoppingCart);
        return mapper.toShoppingCartDto(shoppingCart);
    }

    @Transactional
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        log.info("Изменить количество товаров в корзине пользователя: {}", username);
        validator.validateUsername(username);

        ShoppingCart shoppingCart = getActive(username);

        UUID productId = request.getProductId();
        int quantity = request.getNewQuantity();

        if (!shoppingCart.getProducts().containsKey(productId)) {
            throw new NoProductsInShoppingCartException("Корзина пуста: " + productId);
        }

        ProductDto productDto = shoppingStoreClient.getProduct(productId);
        validator.validateProductQuantity(productDto, quantity);

        shoppingCart.getProducts().put(productId, quantity);
        repository.save(shoppingCart);

        return mapper.toShoppingCartDto(shoppingCart);
    }
}
