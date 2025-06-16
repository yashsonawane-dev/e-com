package com.app.ecom.mapper;

import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setOrderStatus(order.getOrderStatus());
        response.setItems(order.getItems().stream().map(orderItemMapper::mapToOrderItemDTO).toList());
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }
}
