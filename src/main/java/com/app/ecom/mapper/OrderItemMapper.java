package com.app.ecom.mapper;

import com.app.ecom.dto.OrderItemDTO;
import com.app.ecom.model.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderItemMapper {

    public OrderItemDTO mapToOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO response = new OrderItemDTO();
        response.setId(orderItem.getId());
        response.setProductId(orderItem.getProduct().getId());
        response.setQuantity(orderItem.getQuantity());
        response.setPrice(orderItem.getPrice());
        response.setSubTotal(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        return response;
    }
}
