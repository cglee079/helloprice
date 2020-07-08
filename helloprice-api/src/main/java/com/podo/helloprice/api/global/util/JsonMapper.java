package com.podo.helloprice.api.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.podo.helloprice.api.global.util.exception.JsonParseException;
import com.podo.helloprice.api.global.util.exception.ObjectToJsonException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T toObject(String jsonString, Class<T> clazz){
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonParseException(clazz, e);
        }
    }

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonException(e, object.getClass());
        }
    }

}
