package com.rsif.salestax.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsif.salestax.model.CartItem;
import com.rsif.salestax.model.Receipt;
import com.rsif.salestax.model.ReceiptItem;
import com.rsif.salestax.service.ItemService;
import com.rsif.salestax.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.rsif.salestax.TestToolsUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;

@WebMvcTest(controllers = SalesTaxController.class)
class SalesTaxControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    SalesTaxController salesTaxController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new SalesTaxController(new ItemServiceImpl())).build();
    }


    @Test
    void receiptCalculateTaxWhenValidInput_thenReturns200() throws Exception {
        //Creation des paramètres d'entrées et de sorties à partir de donnée au format Json
        List<CartItem> cartItems1 = fromJsonToTypeReference("input_" + 1 + ".json", new TypeReference<>() {});
        Receipt expectedReceipt1 = fromJsonToObject("output_" + 1 + ".json", Receipt.class);
        call(cartItems1, expectedReceipt1);

        List<CartItem> cartItems2 = fromJsonToTypeReference("input_" + 2 + ".json", new TypeReference<>() {});
        Receipt expectedReceipt2 = fromJsonToObject("output_" + 2 + ".json", Receipt.class);
        call(cartItems2, expectedReceipt2);

        List<CartItem> cartItems3 = fromJsonToTypeReference("input_" + 3 + ".json", new TypeReference<>() {});
        Receipt expectedReceipt3 = fromJsonToObject("output_" + 3 + ".json", Receipt.class);
        call(cartItems3, expectedReceipt3);

    }


    void call(List<CartItem> cartItems, Receipt expectedReceipt) throws Exception {
        String requestJson = asJsonString(cartItems);

        // Appeler la méthode à tester
        MvcResult result = mockMvc.perform(post("/calculate-taxes")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson)
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Receipt actualReceipt = new ObjectMapper().readValue(content, Receipt.class);


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

            assertThat(expectedItem.getName()).isEqualToIgnoringWhitespace(actualItem.getName());
            assertEquals(expectedItem.getTotal(), actualItem.getTotal());
        });
    }










}
