package com.rsif.salestax.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receipt {
    private List<ReceiptItem> items = new ArrayList<>();
    private double totalSalesTax;
    private double totalCost;

    public void addItem(String name, double x) {
        ReceiptItem item = new ReceiptItem(name, x);
        items.add(item);
    }
}
