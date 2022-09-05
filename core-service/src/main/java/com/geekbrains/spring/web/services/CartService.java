package com.geekbrains.spring.web.services;

import com.geekbrains.spring.web.dto.Cart;
import com.geekbrains.spring.web.entities.Product;
import com.geekbrains.spring.web.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductsService productsService;
    @Qualifier("test")
    private final CacheManager cacheManager;
    @Value("${spring.cache.user.name}")
    private String CACHE_CART;
    private Cart cart;

    @Cacheable(value = "Cart", key = "#cartName")
    public Cart getCurrentCart(String cartName){
        cart = cacheManager.getCache(CACHE_CART).get(cartName, Cart.class);
        if(!Optional.ofNullable(cart).isPresent()){
            cart = new Cart(cartName, cacheManager);
            cacheManager.getCache(CACHE_CART).put(cartName, cart);
        }
        return cart;
    }

    @CachePut(value = "Cart", key = "#cartName")
    public Cart addProductByIdToCart(Long id, String cartName){
        Cart cart = getCurrentCart(cartName);
        if(!cart.addProductCount(id)) {
            Product product = productsService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Не удалось найти продукт"));
            cart.addProduct(product);
        }
            return cart;
    }

    @CachePut(value = "Cart", key = "#cartName")
    public Cart clear(String cartName){
        Cart cart = getCurrentCart(cartName);
        cart.clear();
        return cart;
    }

//    public Cart getCurrentCart(String cartKey) {
//        if (!redisTemplate.hasKey(cartKey)) {
//            redisTemplate.opsForValue().set(cartKey, new Cart());
//        }
//        return (Cart) redisTemplate.opsForValue().get(cartKey);
//    }
//
//    public void addProductByIdToCart(Long id, String cartKey) {
//        Product product = productsService.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Невозможно добавить продукт в корзину. Продукт не найдет, id: " + id));
//        execute(cartKey, c -> {
//            c.addProduct((product));
//        });
//    }
//
//    public void clear(String cartKey) {
//        execute(cartKey, Cart::clear);
//    }
//
//    public void removeItemFromCart(String cartKey, Long productId) {
//        execute(cartKey, c -> c.removeProduct(productId));
//    }
//
//    public void decrementItem(String cartKey, Long productId) {
//        execute(cartKey, c -> c.removeProduct(productId));
//    }
//
//    public void merge(String userCartKey, String guestCartKey) {
//        Cart guestCart = getCurrentCart(guestCartKey);
//        Cart userCart = getCurrentCart(userCartKey);
//        userCart.merge(guestCart);
//        updateCart(guestCartKey, guestCart);
//        updateCart(userCartKey, userCart);
//    }
//
//    private void execute(String cartKey, Consumer<Cart> action) {
//        Cart cart = getCurrentCart(cartKey);
//        action.accept(cart);
//        redisTemplate.opsForValue().set(cartKey, cart);
//    }
//
//    public void updateCart(String cartKey, Cart cart) {
//        redisTemplate.opsForValue().set(cartKey, cart);
//    }
}
