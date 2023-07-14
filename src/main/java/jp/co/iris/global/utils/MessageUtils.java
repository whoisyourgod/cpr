package jp.co.iris.global.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.iris.global.constant.Const;
import jp.co.iris.global.constant.MessageEnum;
import org.springframework.util.ResourceUtils;

import java.util.HashMap;
import java.util.Map;

public class MessageUtils {
    private MessageUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getMessage(MessageEnum messageEnum) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> messageMap = objectMapper.readValue(ResourceUtils.getFile("classpath:static/js/Message.json"), new TypeReference<HashMap<String, String>>() {
            });
            return messageMap.getOrDefault(messageEnum.name(), Const.COMMEN_ERROR_EXCEPTION);
        } catch (Exception ex) {
            LogUtils.error(ex);
            return Const.COMMEN_ERROR_EXCEPTION;
        }
    }
}
