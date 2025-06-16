package com.app.ecom.controller;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.model.CartItem;
import com.app.ecom.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId,
                                            @RequestBody CartItemRequest request) {
        return cartService.addToCart(userId, request) ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.badRequest().body("Product out of stock or User not found or Product not found");
    }


    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeFromCart(@RequestHeader("X-User-ID") String userId, @PathVariable Long productId) {
        return cartService.removeFromCart(userId, productId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<List<CartItem>> getCartItems(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }
}
