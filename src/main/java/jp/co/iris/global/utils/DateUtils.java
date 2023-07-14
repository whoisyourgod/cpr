package jp.co.iris.global.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <pre>
 * 日付ユーティリティクラス.
 * </pre>
 */
public class DateUtils {
    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * yyyyMMddHHmmssSSS
     */
    public static final String FRM_YMDHMSSSS = "yyyyMMddHHmmssSSS";
    /**
     * yyyyMMddHHmmssSS
     */
    public static final String FRM_YMDHMSSS = "yyyyMMddHHmmssSS";
    /**
     * yyyyMMddHHmmssS
     */
    public static final String FRM_YMDHMSS = "yyyyMMddHHmmssS";
    /**
     * yyyyMMddHHmmss
     */
    public static final String FRM_YMDHMS = "yyyyMMddHHmmss";
    /**
     * yyyyMMddHHmm
     */
    public static final String FRM_YMDHM = "yyyyMMddHHmm";
    /**
     * yyyyMMddHH
     */
    public static final String FRM_YMDH = "yyyyMMddHH";
    /**
     * yyyyMMdd
     */
    public static final String FRM_YMD = "yyyyMMdd";
    /**
     * yyyyMM
     */
    public static final String FRM_YM = "yyyyMM";
    /**
     * yyyy/MM/dd
     */
    public static final String FRM_YMD_SL = "yyyy/MM/dd";
    /**
     * yy-MM-dd
     */
    public static final String FRM_Y2MD_W2 = "yy-MM-dd";

    /**
     * <pre>
     * システム日付を取得し、指定された形式（文字列型）で返す。
     * </pre>
     *
     * @param format 日付形式
     * @return システム日付
     */
    public static String getSysDate(String format) {
        return toDateText(getSysDate(), format);
    }

    /**
     * <pre>
     * システム日付を取得する。
     * </pre>
     *
     * @return システム日付
     */
    public static Date getSysDate() {
        return new Date();
    }

    /**
     * <pre>
     * 月初日を取得する。
     * しかも、加減算用の月数も指定することができる。
     * 要するに、対象日付に対して○月後／○月前の月初日を返してくれる。
     * </pre>
     *
     * @param targetDate  対象日付（日付型）
     * @param monthAmount 加減算する月数
     * @return 月初日
     */
    public static Date getMonthStartDate(Date targetDate, Integer monthAmount) {
        //当月の初日
        Date date = targetDate;
        if (monthAmount != 0) {
            date = addMonth(targetDate, monthAmount);
        }
        return toDate(toDateText(date, FRM_YM) + "01", FRM_YMD);
    }

    /**
     * <pre>
     * 月初日を取得する。
     * しかも、加減算用の月数も指定することができる。
     * 要するに、対象日付に対して○月後／○月前の月初日を返してくれる。
     * </pre>
     *
     * @param targetDate  対象日付（文字列型）
     * @param format      日付フォーマット
     * @param monthAmount 加減算する月数
     * @return 月初日
     */
    public static Date getMonthStartDate(String targetDate, String format, Integer monthAmount) {
        return getMonthStartDate(toDate(targetDate, format), monthAmount);
    }

    /**
     * <pre>
     * 月末日を取得する。
     * しかも、加減算用の月数も指定することができる。
     * 要するに、対象日付に対して○月後／○月前の月末日を返してくれる。
     * </pre>
     *
     * @param targetDate  対象日付
     * @param monthAmount 加減算する月数
     * @return 月末日
     */
    public static Date getMonthEndDate(Date targetDate, Integer monthAmount) {
        //翌月の初日-1日
        Date date = targetDate;
        if (monthAmount + 1 != 0) {
            date = addMonth(targetDate, monthAmount + 1);
        }
        return toDate(addDay(toDateText(date, FRM_YM) + "01", FRM_YMD, -1), FRM_YMD);
    }

    /**
     * <pre>
     * 月末日を取得する。
     * しかも、加減算用の月数も指定することができる。
     * 要するに、対象日付に対して○月後／○月前の月末日を返してくれる。
     * </pre>
     *
     * @param targetDate  対象日付（文字列型）
     * @param format      日付フォーマット
     * @param monthAmount 加減算する月数
     * @return 月末日
     */
    public static Date getMonthEndDate(String targetDate, String format, Integer monthAmount) {
        return getMonthEndDate(toDate(targetDate, format), monthAmount);
    }

    /**
     * String型の日付をDate型に変換
     *
     * @param targetDate 変換対象日付（文字列）
     * @param fromFormat 第一引数の日付の形式
     * @return 変換後日付
     */
    public static Date toDate(String targetDate, String fromFormat) {
        if (ComUtils.isEmpty(targetDate)) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(fromFormat);
            formatter.setLenient(false);
            return formatter.parse(targetDate);
        } catch (@SuppressWarnings("unused") ParseException e) {
            return null;
        }
    }

    /**
     * 日付を文字列に変換して返却する。※nullのときはブランクを返す
     * 変換形式： 引数にて指定
     *
     * @param date     対象日付
     * @param toFormat 変換後の日付形式
     * @return 変換文字列
     */
    public static String toDateText(Date date, String toFormat) {
        String str = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(toFormat);
            str = df.format(date);
        }
        return str;
    }

    /**
     * 日付を文字列に変換して返却する。※nullのときはブランクを返す
     * 変換形式： 引数にて指定
     *
     * @param targetDate 対象日付
     * @param fromFormat 元々の形式
     * @param toFormat   新しい形式
     * @return 変換文字列
     */
    public static String toDateText(String targetDate, String fromFormat, String toFormat) {
        return toDateText(toDate(targetDate, fromFormat), toFormat);
    }

    /**
     * 日付を文字列に変換して返却する。※nullのときはブランクを返す
     * 変換形式： 引数にて指定
     * ※年から始まる形式であること（YYYY/MM、YYYY/MM/DD、YYYY/MM/DD HH:MM:SS など）
     *
     * @param targetDate_ 対象日付
     * @param toFormat    新しい形式
     * @return 変換文字列
     */
    public static String toDateText(String sourceDate, String toFormat) {
        String fromFormat = "";
        if (ComUtils.isEmpty(sourceDate)) {
            return "";
        }
        String targetDate = sourceDate;
        targetDate = targetDate.replace("-", "");
        targetDate = targetDate.replace("/", "");
        targetDate = targetDate.replace(":", "");
        targetDate = targetDate.replace(".", "");
        targetDate = targetDate.replace(" ", "");

        if (targetDate.length() == FRM_YMDHMSSSS.length()) {
            fromFormat = FRM_YMDHMSSSS;
        } else if (targetDate.length() == FRM_YMDHMSSS.length()) {
            fromFormat = FRM_YMDHMSSS;
        } else if (targetDate.length() == FRM_YMDHMSS.length()) {
            fromFormat = FRM_YMDHMSS;
        } else if (targetDate.length() == FRM_YMDHMS.length()) {
            fromFormat = FRM_YMDHMS;
        } else if (targetDate.length() == FRM_YMDHM.length()) {
            fromFormat = FRM_YMDHM;
        } else if (targetDate.length() == FRM_YMDH.length()) {
            fromFormat = FRM_YMDH;
        } else if (targetDate.length() == FRM_YMD.length()) {
            fromFormat = FRM_YMD;
        } else if (targetDate.length() == FRM_YM.length()) {
            fromFormat = FRM_YM;
        } else {
            return "";
        }

        return toDateText(toDate(targetDate, fromFormat), toFormat);
    }
    //----------------------------------------------------------------
    //日時データの加減算
    //----------------------------------------------------------------

    /**
     * 日時データ加減算（年）
     *
     * @param date   対象日付(String)
     * @param amount 加算数
     * @param format 形式
     * @return 加算後日時データ
     */
    public static String addYear(String date, String format, Integer amount) {
        return toDateText(addYear(toDate(date, format), amount), format);
    }

    /**
     * 日時データ加減算（年）
     *
     * @param date   対象日付(Date)
     * @param amount 加算数
     * @return 加算後日時データ
     */
    public static Date addYear(Date date, Integer amount) {
        return addDateTime(date, amount, Calendar.YEAR);
    }

    /**
     * 日時データ加減算（月）
     *
     * @param date   対象日付(String)
     * @param format 形式
     * @param amount 加算数
     * @return 加算後日時データ
     */
    public static String addMonth(String date, String format, Integer amount) {
        return toDateText(addMonth(toDate(date, format), amount), format);
    }

    /**
     * 日時データ加減算（月）
     *
     * @param date   対象日付(Date)
     * @param amount 加算数
     * @return 加算後日時データ
     */
    public static Date addMonth(Date date, Integer amount) {
        return addDateTime(date, amount, Calendar.MONTH);
    }

    /**
     * 日時データ加減算（日）
     *
     * @param date   対象日付(String)
     * @param format 形式
     * @param amount 加算数
     * @return 加算後日時データ
     */
    public static String addDay(String date, String format, Integer amount) {
        return toDateText(addDay(toDate(date, format), amount), format);
    }

    /**
     * 日時データ加減算（日）
     *
     * @param date   対象日付(Date)
     * @param amount 加算数
     * @return 加算後日時データ
     */
    public static Date addDay(Date date, Integer amount) {
        return addDateTime(date, amount, Calendar.DATE);
    }

    /**
     * 日時データ加減算（時）
     *
     * @param date   対象日付(String)
     * @param format 形式
     * @param amount 加算数
     * @return 加算後日時データ
     */
    public static String addHour(String date, String format, Integer amount) {
        return toDateText(addHour(toDate(date, format), amount), format);
    }

    /**
     * 日時データ加減算（時）
     *
     * @param date   対象日付(Date)
     * @param amount 加算数
     * @return 加算後日時データ
     */
    public static Date addHour(Date date, Integer amount) {
        return addDateTime(date, amount, Calendar.HOUR);
    }

    /**
     * 日時データ加減算（分）
     *
     * @param date   対象日付(String)
     * @param format 形式
     * @param amount 加算数
     * @return 加算後日時データ
     */
    public static String addMinute(String date, String format, Integer amount) {
        return toDateText(addMinute(toDate(date, format), amount), format);
    }

    /**
     * 日時データ加減算（分）
     *
     * @param date   対象日付(Date)
     * @param amount 加算数
     * @return 加算後日時データ
     */
    public static Date addMinute(Date date, Integer amount) {
        return addDateTime(date, amount, Calendar.MINUTE);
    }

    /**
     * 日時データ加減算（秒）
     *
     * @param date   対象日付(String)
     * @param format 形式
     * @param amount 加算数
     * @return 加算後日時データ
     */
    public static String addSecond(String date, String format, Integer amount) {
        return toDateText(addSecond(toDate(date, format), amount), format);
    }

    /**
     * 日時データ加減算（秒）
     *
     * @param date   対象日付(Date)
     * @param amount 加算数
     * @return 加算後日時データ
     */
    public static Date addSecond(Date date, Integer amount) {
        return addDateTime(date, amount, Calendar.SECOND);
    }

    /**
     * 日時データ加減算
     *
     * @param date   対象日付(Date)
     * @param amount 加算数
     * @param kind   加算レベル(年、月、日、時、分、秒)
     * @return 加算後日時データ
     */
    private static Date addDateTime(Date date, Integer amount, Integer kind) {
        if (ComUtils.isEmpty(date)) {
            return null;
        }
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(kind, amount);
        return now.getTime();
    }
}
