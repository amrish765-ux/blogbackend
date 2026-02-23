package com.blogapplication.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private int amount;
    private String currency;
    private String receipt;
    private String notes;
}

