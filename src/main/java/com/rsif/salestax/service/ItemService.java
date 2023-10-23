package com.rsif.salestax.service;

import com.rsif.salestax.model.CartItem;
import com.rsif.salestax.model.Receipt;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ItemService {
    Receipt getItemSalesTaxCalc(List<CartItem> cartItems);
    double calculateSalesTax(CartItem cartItem);
}
