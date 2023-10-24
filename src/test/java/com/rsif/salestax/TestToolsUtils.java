package com.rsif.salestax;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsif.salestax.model.CartItem;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

public class TestToolsUtils {

    public static <T> T fromJsonToObject (String json, Class<T> clazz) {
        try {
            Resource resource = new ClassPathResource("json/" + json);
            String jsonContentUTF8 = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8); //encoding UTF-8
            return new ObjectMapper().readValue(jsonContentUTF8, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T fromJsonToTypeReference (String json, TypeReference<T> valueTypeRef) {
        try {
            Resource resource = new ClassPathResource("json/" + json);
            String jsonContentUTF8 = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8); //encoding UTF-8
            return new ObjectMapper().readValue(jsonContentUTF8, valueTypeRef);
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
