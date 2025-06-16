package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.mapper.ProductMapper;
import com.app.ecom.mapper.UserMapper;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    public boolean addToCart(String userId, CartItemRequest request) {
        // look for product
        Product product = productRepository.findById(request.getProductId())
                .orElse(null);

        if (ObjectUtils.isEmpty(product)) {
            return false;
        }

        if (product.getStockQuantity() < request.getQuantity()) {
            return false;
        }

        // look for user
        User user = userRepository.findById(Long.valueOf(userId))
                .orElse(null);

        if (ObjectUtils.isEmpty(user)) {
            return false;
        }

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);
        if (!ObjectUtils.isEmpty(existingCartItem)) {
            // update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            // create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setUser(user);
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItem.setQuantity(request.getQuantity());
            cartItemRepository.save(cartItem);
        }

        product.setStockQuantity(product.getStockQuantity() - request.getQuantity());
        productRepository.save(product);
        return true;
    }

    public boolean removeFromCart(String userId, Long productId) {
        // look for user
        User user = userRepository.findById(Long.valueOf(userId))
                .orElse(null);

        if (ObjectUtils.isEmpty(user)) {
            return false;
        }

        // look for product
        Product product = productRepository.findById(productId)
                .orElse(null);

        if (ObjectUtils.isEmpty(product)) {
            return false;
        }

        cartItemRepository.deleteByUserAndProduct(user, product);
        return true;
    }

    public List<CartItemResponse> getCartItems(String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElse(null);

        if (ObjectUtils.isEmpty(user)) {
            return List.of();
        }

        List<CartItem> cartItemList = cartItemRepository.findByUser(user);
        List<CartItemResponse> responseList = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            CartItemResponse response = new CartItemResponse();
            response.setId(cartItem.getId());
            response.setUser(userMapper.mapToUserResponse(cartItem.getUser()));
            response.setProduct(productMapper.mapToProductResponse(cartItem.getProduct()));
            response.setPrice(cartItem.getPrice());
            response.setQuantity(cartItem.getQuantity());
            responseList.add(response);
        }

        return CollectionUtils.isEmpty(responseList) ? List.of() : responseList;
    }
}
