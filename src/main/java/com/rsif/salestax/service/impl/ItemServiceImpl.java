package com.rsif.salestax.service.impl;

import com.rsif.salestax.model.CartItem;
import com.rsif.salestax.model.Receipt;
import com.rsif.salestax.service.ItemService;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00", SYMBOLS);

    @Override
    public Receipt getItemSalesTaxCalc(List<CartItem> cartItems) {
        Receipt receipt = new Receipt();

        double totalSalesTax = cartItems.stream().mapToDouble(this::calculateSalesTax).sum();
        double totalCost = cartItems.stream().mapToDouble(item -> item.getPrice() + calculateSalesTax(item)).sum();

        cartItems.forEach(item -> receipt.addItem(item.getName(), roundToTwoDecimalPlaces(item.getPrice() + calculateSalesTax(item))));

        receipt.setTotalSalesTax(roundToTwoDecimalPlaces(totalSalesTax));
        receipt.setTotalCost(roundToTwoDecimalPlaces(totalCost));
        return receipt;
    }

    public double calculateSalesTax(CartItem item) {
        double salesTax = 0;

        if (!item.isExempt()) {
            salesTax += calculateBasicSalesTax(item.getPrice());
        }

        if (item.isImported()) {
            salesTax += calculateImportDutyTax(item.getPrice());
        }

        return roundToNearestFiveCents(salesTax);

    }

    private double calculateBasicSalesTax(double price) {
        double taxRate = 0.10; // 10% basic sales tax
        return price * taxRate;
    }

    private double calculateImportDutyTax(double price) {
        double taxRate = 0.05; // 5% import duty
        return price * taxRate;
    }

    private double roundToNearestFiveCents(double amount) {
        return Math.ceil(amount / 0.05) * 0.05;
    }

    private double roundToTwoDecimalPlaces(double value) {
        return Double.parseDouble(DECIMAL_FORMAT.format(value));
    }


}
