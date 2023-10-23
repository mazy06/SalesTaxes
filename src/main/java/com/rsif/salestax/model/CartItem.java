package com.rsif.salestax.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String name;
    private double price;
    private boolean isExempt;
    private boolean isImported;
}
