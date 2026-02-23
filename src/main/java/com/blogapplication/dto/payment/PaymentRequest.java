package com.blogapplication.dto.payment;

import lombok.Data;

@Data
public class PaymentRequest {
    private int amount;
    private String currency;
    private String orderId;
    private String receipt;
}