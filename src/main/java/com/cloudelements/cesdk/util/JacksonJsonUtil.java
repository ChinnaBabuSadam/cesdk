package com.cloudelements.cesdk.util;

import com.cloudelements.cesdk.service.exception.JsonParseException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class JacksonJsonUtil {

    private static ObjectMapper objectMapper;
    private static ObjectMapper nonNullObjectMapper;
    private static ObjectMapper nonNullFormattedObjectMapper;

    private static final String UTF_8 = "UTF-8";


    static {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

        nonNullObjectMapper = new ObjectMapper();
        nonNullObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        nonNullFormattedObjectMapper = new ObjectMapper();
        nonNullFormattedObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        nonNullFormattedObjectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        nonNullFormattedObjectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        nonNullFormattedObjectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }


    public static Map<String, Object> convertStringToMap(String json) {
        try {
            return objectMapper.readValue(json.getBytes(UTF_8), new TypeReference<Map>() {});
        } catch (Exception e) {
            throw new JsonParseException("Failed to convert String to Map " + json, e);
        }
    }

    public static String convertObjectToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new JsonParseException("Failed to convert Map to String", e);
        }
    }

    public static String convertMapToString(Map map) {
        return convertObjectToString(map);
    }

    public static List<Map> convertStringToList(String json) {
        if (StringUtils.isBlank(json)) { return null; }
        try {
            return objectMapper.readValue(json.getBytes(UTF_8), new TypeReference<List<Map>>() {});
        } catch (Exception e) {
            throw new JsonParseException("Failed to convert String to List " + json, e);
        }
    }

    public static Map convertObjectToMap(Object o) {
        try {
            return objectMapper.convertValue(o, Map.class);
        } catch (Exception e) {
            throw new JsonParseException("Failed to convert object to map", e);
        }
    }

    public static List<Map> convertObjectToMapList(List<?> list) {
        try {
            TypeReference<List<Map>> typeRef = new TypeReference<List<Map>>() {};
            return objectMapper.convertValue(list, typeRef);
        } catch (Exception e) {
            throw new JsonParseException("Failed to convert object to map", e);
        }
    }

    public static <T> T convertStringToObject(String string, Class<T> objectClass) {
        try {
            return nonNullFormattedObjectMapper.readValue(string, objectClass);
        } catch (Exception e) {
            throw new JsonParseException("Failed to convert string to object", e);
        }
    }

}
