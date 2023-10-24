package com.rsif.salestax.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String name;
    private int quantity;
    private double price;
    private boolean isExempt;
    private boolean isImported;
}
