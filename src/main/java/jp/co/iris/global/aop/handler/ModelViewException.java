package jp.co.iris.global.aop.handler;

public class ModelViewException extends RuntimeException {

    private static final long serialVersionUID = -8860890753002411913L;

    private int code;

    private String message;

    public static ModelViewException transfer(Exception e) {
        return new ModelViewException(1, e.getMessage());
    }

    private ModelViewException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
