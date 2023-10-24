package com.rsif.salestax.controller;

import com.rsif.salestax.model.CartItem;
import com.rsif.salestax.model.Receipt;
import com.rsif.salestax.service.ItemService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class SalesTaxController {

    private final ItemService itemService;

    public SalesTaxController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping( value = "/calculate-taxes",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Receipt ReceiptCalculateTax(@RequestBody List<CartItem> cartItems){
        return  itemService.getItemSalesTaxCalc(cartItems);
    }



}
