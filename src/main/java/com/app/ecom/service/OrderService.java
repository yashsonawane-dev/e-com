package com.app.ecom.service;

import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.mapper.OrderMapper;
import com.app.ecom.mapper.ProductMapper;
import com.app.ecom.model.*;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public Optional<OrderResponse> createOrder(String userId) {
        // Validate for cart items
        List<CartItem> cartItems = cartService.getCartItems(userId);

        if (CollectionUtils.isEmpty(cartItems)) {
            return Optional.empty();
        }
        // Validate userId
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);

        if (ObjectUtils.isEmpty(user)) {
            return Optional.empty();
        }

        // Calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item ->
                        new OrderItem(null,
                                item.getProduct(),
                                item.getQuantity(),
                                item.getPrice(),
                                order)
                ).toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(userId);
        return Optional.of(orderMapper.mapToOrderResponse(order));
    }
}
