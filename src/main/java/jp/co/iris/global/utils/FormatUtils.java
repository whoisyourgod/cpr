package jp.co.iris.global.utils;

import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

/**
 * <pre>
 * フォーマッター共通ユーティリティクラス.
 * </pre>
 *
 * @author
 * @version 1.0
 */
@SuppressWarnings("all")
public class FormatUtils {
    private FormatUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Integer型への変換を行う.
     *
     * @param text パラメータ
     * @return 変換結果（変換できない場合はnull）
     */
    public static Integer toInteger(Object text) {

        if (ObjectUtils.isEmpty(text)) {
            return null;
        }

        Integer result = null;
        try {
            String value = String.valueOf(text);
            value = ComUtils.zerosuppress(value);
            if (!value.matches("^-?([1-9]\\d*|0)$")) {
                // 数字以外が入っている場合は変換しない
                return null;
            }

            result = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            // 何もしない
        }

        return result;
    }

    /**
     * Long型への変換を行う.
     *
     * @param text パラメータ
     * @return 変換結果（変換できない場合はnull）
     */
    public static Long toLong(Object text) {

        if (ObjectUtils.isEmpty(text)) {
            return null;
        }

        Long result = null;
        try {
            String value = String.valueOf(text);
            if (!value.matches("^-?([1-9]\\d*|0)$")) {
                // 数字以外が入っている場合は変換しない
                return null;
            }

            result = Long.valueOf(value);
        } catch (NumberFormatException e) {
            // 何もしない
        }

        return result;
    }

    /**
     * BigDecimal型への変換を行う.
     *
     * @param text パラメータ
     * @return 変換結果（変換できない場合はnull）
     */
    public static BigDecimal toBigDecimal(Object text) {

        if (ObjectUtils.isEmpty(text)) {
            return null;
        }

        BigDecimal result = null;
        try {
            String value = String.valueOf(text);
            if (value.matches("^-?([1-9]\\d*|0)(\\.\\d+|)$")) {
                // 数値なのでここでOK
                result = new BigDecimal(value);
            } else if (value.matches("-{0,1}[0-9]+\\.{0,1}[0-9]*[eE]-{0,1}[0-9]+")) {
                // 指数表現を許容する
                result = new BigDecimal(value);
            } else {
                result = null;
            }

        } catch (NumberFormatException e) {
            // 何もしない
        }

        return result;
    }

    /**
     * DB登録値に型変換を行う（BigDecimal型）.
     *
     * @param value 画面入力値
     * @return 変換結果
     */
    public static BigDecimal toDbDecimal(String value) {

        // 画面入力値が未入力の場合にDB項目型に合わせて0を返す.
        if (ObjectUtils.isEmpty(value)) {
            return BigDecimal.ZERO;
        }

        return FormatUtils.toBigDecimal(value);
    }

    /**
     * String型への変換を行う.
     *
     * @param text パラメータ
     * @return 変換結果（変換できない場合はnull）
     */
    public static String toString(Object text) {
        return cast(text, String.class);
    }

    /**
     * 型変換を行う.
     *
     * @param text パラメータ
     * @param type 項目型
     * @return 変換結果（変換できない場合は、変換なしの値）
     */
    private static <T> T cast(Object text, Class<?> type) {

        if (ObjectUtils.isEmpty(text)) {
            return null;
        }

        Object value = null;

        if (type.isAssignableFrom(Integer.class)) {
            value = Integer.valueOf(String.valueOf(text));
        } else if (type.isAssignableFrom(Long.class)) {
            value = Long.valueOf(String.valueOf(text));
        } else if (type.isAssignableFrom(BigDecimal.class)) {
            value = new BigDecimal(String.valueOf(text));
        } else if (type.isAssignableFrom(String.class)) {
            if (text.getClass().isAssignableFrom(BigDecimal.class)) {
                value = ((BigDecimal) text).toPlainString();
            } else {
                value = String.valueOf(text);
            }
        } else {
            value = text;
        }

        // 呼び出し元の型に合わせる
        return (T) value;
    }

    /**
     * 型変換を行う.
     *
     * @param text パラメータ
     * @param type 項目型
     * @return 変換結果（変換できない場合は、変換なしの値）
     */
    public static <T> T cast(Object text) {
        return text == null ? null : cast(text, text.getClass());
    }
}
