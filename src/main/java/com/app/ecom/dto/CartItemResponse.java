package com.app.ecom.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private Long id;
    private UserResponse user;
    private ProductResponse product;
    private Integer quantity;
    private BigDecimal price;
}
