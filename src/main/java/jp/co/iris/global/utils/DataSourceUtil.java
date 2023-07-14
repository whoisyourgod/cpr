package jp.co.iris.global.utils;

/**
 * データベース切り替えユーティリティクラス
 * 
 */
public class DataSourceUtil {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * データベース名設定
     */
    public static void setDB(String dbType) {
        contextHolder.set(dbType);
    }

    /**
     * データベース名取得
     */
    public static String getDB() {
        return (contextHolder.get());
    }

    /**
     * クリアデータベース名
     */
    public static void clearDB() {
        contextHolder.remove();
    }
}
