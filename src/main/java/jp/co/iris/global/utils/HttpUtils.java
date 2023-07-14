package jp.co.iris.global.utils;

import jp.co.iris.global.constant.Const;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class HttpUtils {
    private HttpUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(
            Arrays.asList(new MappingJackson2HttpMessageConverter(), new Html2HttpMessageConvert())
    ).build();

    public static <T> T get(String url, Class<T> clazz, Object... params) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, clazz, params);
        return responseEntity.getBody();
    }

    public static <T> T post(String url, Object requestBody, Class<T> clazz) {
        LogUtils.info("HttpUtils post url: " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, clazz);
        Assert.isTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()) || HttpStatus.NO_CONTENT.equals(responseEntity.getStatusCode()), "HttpStatus is not OK!");
        return responseEntity.getBody();
    }

    private static class Html2HttpMessageConvert extends AbstractHttpMessageConverter<Object> {
        public Html2HttpMessageConvert() {
            super(StandardCharsets.UTF_8, MediaType.TEXT_HTML);
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return true;
        }

        @Override
        protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
                throws IOException, HttpMessageNotReadableException {
            String body = Const.DEFAULT_BLANK;
            try (InputStream is = inputMessage.getBody()) {
                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    byte[] bytes = new byte[10240];
                    int n;
                    while ((n = is.read(bytes)) != -1) {
                        os.write(bytes, 0, n);
                    }
                    body = os.toString();
                }
            }
            return JsonUtils.getInstance().jsonToObject(body, clazz);
        }

        @Override
        protected void writeInternal(Object t, HttpOutputMessage outputMessage)
                throws HttpMessageNotWritableException {
            // Do nothing because unneed to handle
        }
    }
}