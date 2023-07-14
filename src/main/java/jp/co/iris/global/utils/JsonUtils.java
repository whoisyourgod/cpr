package jp.co.iris.global.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class JsonUtils {
    private final ObjectMapper objectMapper;
    private static JsonUtils instance;

    public JsonUtils() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static JsonUtils getInstance() {
        if (instance == null) {
            instance = new JsonUtils();
        }

        return instance;
    }

    public <T> T jsonToObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception ex) {
            log.error("Json to object error! {0}", ex);
            return null;
        }
    }

    public JavaType getJaveType(Class<?> parametrized, Class<?>... parameterClasses) {
        return objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    public <T> T jsonToObject(String json, JavaType javaType) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return objectMapper.readValue(json, javaType);
        } catch (Exception ex) {
            log.error("Json to object error! {0}", ex);
            return null;
        }
    }

    public String objectToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception ex) {
            log.error("Object to json error! {0}", ex);
            return "";
        }
    }
}
