package jp.co.iris.global.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ログ出力クラスです
 */
public class LogUtils {
    private LogUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String SEPT_CAHR = " : ";
    private static final String IP_TITLE = "IP Address ";
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    /**
     * ログの出力処理を実行する.
     *
     * @param r ログの出力処理を記述したIF
     */
    private static void writeLog(Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Debugレベルのログを出力する
     */
    public static void debug() {
        writeLog(() -> logger.debug(getExecInfo()));
    }

    /**
     * Debugレベルのログを出力する(任意文字列の出力あり)
     *
     * @param msg ログ情報に付加して出力する任意の文字列
     */
    public static void debug(String msg) {
        writeLog(() -> logger.debug(getExecInfo().concat(SEPT_CAHR).concat(msg)));
    }

    /**
     * Infoレベルのログを出力する
     */
    public static void info() {
        writeLog(() -> logger.info(getExecInfo()));
    }

    /**
     * Infoレベルのログを出力する(任意文字列の出力あり)
     *
     * @param msg ログ情報に付加して出力する任意の文字列
     */
    public static void info(String msg) {
        writeLog(() -> logger.info(getExecInfo().concat(SEPT_CAHR).concat(msg)));
    }

    /**
     * Warnレベルのログを出力する
     */
    public static void warn() {
        writeLog(() -> logger.warn(getExecInfo()));
    }

    /**
     * Warnレベルのログを出力する(任意文字列の出力あり)
     *
     * @param msg ログ情報に付加して出力する任意の文字列
     */
    public static void warn(String msg) {
        writeLog(() -> logger.warn(getExecInfo().concat(SEPT_CAHR).concat(msg)));
    }

    /**
     * Errorレベルのログを出力する
     *
     * @param e エラー情報を保持する例外クラス
     */
    public static void error(Exception e) {
        writeLog(() -> logger.error(getIpAddress().concat(SEPT_CAHR).concat(ComUtils.getStackTraceStr(e))));
    }

    /**
     * Errorレベルのログを出力する
     *
     * @param e   エラー情報を保持する例外クラス
     * @param msg ログ情報に付加して出力する任意の文字列
     */
    public static void error(Exception e, String msg) {
        writeLog(() -> logger.error(getExecInfo().concat(SEPT_CAHR).concat(msg)));
        error(e);
    }

    /**
     * IPアドレスを取得する
     *
     * @return IPアドレス
     */
    private static String getIpAddress() {
        StringBuilder sb = new StringBuilder(IP_TITLE);
        try {
            sb.append(ComUtils.getIp());
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return sb.toString();
    }

    /**
     * 実行時情報を取得する
     *
     * @return IPアドレスと呼出し元の情報
     */
    private static String getExecInfo() {
        // 使用端末のIPアドレス
        StringBuilder sb = new StringBuilder(getIpAddress());

        // 呼出し元の情報
        final Integer STACK_NUM = 4;
        StackTraceElement[] ste = (new Throwable()).getStackTrace();
        sb.append(SEPT_CAHR + ste[STACK_NUM].getClassName());
        sb.append(SEPT_CAHR + ste[STACK_NUM].getMethodName());
        sb.append(SEPT_CAHR + ste[STACK_NUM].getLineNumber());
        return sb.toString();
    }
}
