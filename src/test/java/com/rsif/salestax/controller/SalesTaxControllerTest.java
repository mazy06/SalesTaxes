package com.rsif.salestax.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsif.salestax.model.CartItem;
import com.rsif.salestax.model.Receipt;
import com.rsif.salestax.model.ReceiptItem;
import com.rsif.salestax.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(SalesTaxController.class)
class SalesTaxControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;




    @Test
    void receiptCalculateTaxWhenValidInput_thenReturns200() throws Exception {

        // Créer des exemples de paramètres d'entrée
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem("book", 12.49, true, false));
        cartItems.add(new CartItem("music CD", 14.99, false, false));
        cartItems.add(new CartItem("chocolatebar", 0.85, true, false));

        // Créer un exemple de données de sortie attendues
        Receipt expectedReceipt = new Receipt();
        expectedReceipt.addItem("book", 12.99);
        expectedReceipt.addItem("music CD", 16.49);
        expectedReceipt.addItem("chocolatebar", 0.85);
        expectedReceipt.setTotalSalesTax(1.50);
        expectedReceipt.setTotalCost(29.83);

        // Définir le comportement du mock du calculateur de taxes
        /*when(itemService.calculateSalesTax(cartItems.get(0))).thenReturn(0.0);
        when(itemService.calculateSalesTax(cartItems.get(1))).thenReturn(6.00);
        when(itemService.calculateSalesTax(cartItems.get(2))).thenReturn(0.60);*/

        String requestJson = asJsonString(cartItems);

        // Appeler la méthode à tester
        MvcResult result = mockMvc.perform(post("/calculate-taxes")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson)
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Receipt actualReceipt = fromJson(content, Receipt.class);


        // Vérifier que la réponse correspond aux données attendues
        List<ReceiptItem> expectedItems = expectedReceipt.getItems();
        List<ReceiptItem> actualItems = actualReceipt.getItems();

        assertEquals(expectedItems.size(), actualItems.size());

        expectedItems.forEach( expectedItem -> {
            ReceiptItem actualItem = actualItems.stream()
                    .filter(item -> item.getName().equals(expectedItem.getName()))
                    .findFirst()
                    .orElse(null);

            assertNotNull(actualItem);

            assertThat(expectedItem.getName()).isEqualToIgnoringCase(actualItem.getName());
            assertEquals(expectedItem.getTotal(), actualItem.getTotal());
        });

    }

    public static <T> T fromJson (String json, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
