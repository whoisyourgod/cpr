package jp.co.iris.global.message;

import lombok.Data;

@Data
public class ErrorMessage {

    public ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;

    private String message;
}
