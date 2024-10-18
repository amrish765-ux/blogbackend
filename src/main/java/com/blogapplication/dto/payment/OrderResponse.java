package com.blogapplication.dto.payment;

import lombok.Data;

@Data
public class OrderResponse {
    private String orderId;
    private int amount;
    private String currency;
    private String receipt;
}