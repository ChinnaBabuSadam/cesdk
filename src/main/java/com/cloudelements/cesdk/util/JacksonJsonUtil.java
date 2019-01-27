package com.cloudelements.cesdk.util;

import com.cloudelements.cesdk.service.Service;
import com.cloudelements.cesdk.service.exception.JsonParseException;
import com.cloudelements.cesdk.service.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JacksonJsonUtil {

    private static ObjectMapper objectMapper;
    private static ObjectMapper nonNullObjectMapper;
    private static ObjectMapper nonNullFormattedObjectMapper;
    private static ObjectMapper anyFieldVisibilityObjectMapper;

    private static final String UTF_8 = "UTF-8";


    static {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

        nonNullObjectMapper = new ObjectMapper();
        nonNullObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        nonNullObjectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        nonNullFormattedObjectMapper = new ObjectMapper();
        nonNullFormattedObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        nonNullFormattedObjectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        nonNullFormattedObjectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        nonNullFormattedObjectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        nonNullFormattedObjectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        anyFieldVisibilityObjectMapper = new ObjectMapper();
        anyFieldVisibilityObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        anyFieldVisibilityObjectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        anyFieldVisibilityObjectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        anyFieldVisibilityObjectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        anyFieldVisibilityObjectMapper.setVisibilityChecker(anyFieldVisibilityObjectMapper.getVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        anyFieldVisibilityObjectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

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

    protected static <T> T convertMapToObject(Map map, Class<T> objectClass, ObjectMapper mapper) {
        try {
            return mapper.readValue(mapper.writeValueAsString(map), objectClass);
        } catch (UnrecognizedPropertyException e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST ,String.format("No field exists on object named %s",
                    e.getPropertyName()));
        } catch (InvalidFormatException e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, String.format("The value '%s' is invalid",
                    e.getValue()));
        } catch (Exception e) {
            throw new JsonParseException("Failed to convert map to object", e);
        }
    }

    public static <T> T convertMapToObjectWithAnyField(Map map, Class<T> objectClass) {
        return convertMapToObject(map, objectClass, anyFieldVisibilityObjectMapper);
    }

    public static List convertObjectToList(Object object) {
        if (object == null) {
            return null;
        }
        List objectList = new ArrayList<>();
        if (object instanceof Map || object instanceof String) {
            objectList.add(object);
        } else if (object instanceof List) {
            objectList.addAll((List) object);
        }
        return objectList;
    }

}
