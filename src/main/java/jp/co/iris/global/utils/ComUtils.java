package jp.co.iris.global.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.iris.global.aop.handler.CPException;
import jp.co.iris.global.bean.CP00001Bean;
import jp.co.iris.global.constant.Const;

/**
 * <pre>
 * 汎用ユーティリティクラス.
 * </pre>
 *
 * @author
 * @version 1.0
 */
@Component
public class ComUtils {
    private static HttpServletRequest req;

    @Autowired
    private void setRequest(HttpServletRequest request) {
        ComUtils.req = request;
    }

    private static HttpSession httpSession;

    @Autowired
    private void setHttpSession(HttpSession httpSession) {
        ComUtils.httpSession = httpSession;
    }

    public static CP00001Bean getHeadData() {
        return (CP00001Bean) httpSession.getAttribute(Const.HEAD_DATA);
    }

    public static String getSystemId(String pageId) {
        try {
            return InetAddress.getLocalHost().getHostName() + Const.UN_BAR + pageId;
        } catch (Exception e) {
            Log.info(e.getMessage());
            return Const.DEFAULT_BLANK;
        }
    }

    /**
     * クライアント端末のIPアドレスを取得する.
     *
     * @return IPアドレス
     */
    public static String getIp() {
        String remoteAddr = "";
        if (req != null) {
            remoteAddr = req.getHeader("X-FORWARDED-FOR");
            if (isBlank(remoteAddr)) {
                remoteAddr = req.getRemoteAddr();
            }
        }
        return remoteAddr;
    }


    /**
     * 例外のスタックトレースを文字列にして返す
     *
     * @param e スタックトレースを取得する例外
     * @return 文字列化されたスタックトレース
     */
    public static String getStackTraceStr(Exception e) {
        StringWriter sw = new StringWriter();
        try {
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
        } catch (Exception exp) {
            LogUtils.error(exp);
        }
        return sw.toString();
    }

    /**
     * フォワード用の文字列を返す
     *
     * @param url 遷移先URL
     * @return フォワード用文字列
     */
    public static String getForwardStr(String url) {
        return Const.FORWARD + url;
    }

    /**
     * リダイレクト用の文字列を返す
     *
     * @param url 遷移先URL
     * @return リダイレクト用文字列
     */
    public static String getRedirectStr(String url) {
        return Const.REDIRECT + url;
    }

    /**
     * null、空文字、空白文字の判定 null、空文字、空白文字ならtrue それ以外false
     *
     * @param cs 文字列
     * @return true、false
     */
    public static boolean isBlank(final CharSequence cs) {
        Integer strLen = 0;
        if (cs == null || (strLen = cs.length()) == 0) {
            // 引数がnullまたは空文字の場合
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                // 引数に1つでも空白でない文字が含まれる場合
                return false;
            }
        }
        return true;
    }

    /**
     * 配列が要素を持つか判定する. 配列がnullまたは要素数がゼロの場合はtrueを, それ以外の場合はfalseを返す.
     *
     * @param target 判定対象の配列
     * @return 判定結果
     */
    public static boolean isEmptyArray(Object[] target) {
        return target == null || target.length == 0;
    }

    /**
     * Listが要素を持つか判定する. Listがnullまたは要素数がゼロの場合はtrueを, それ以外の場合はfalseを返す.
     *
     * @param target 判定対象のリスト
     * @return 判定結果
     */
    public static boolean isEmptyList(List<?> target) {
        return target == null || target.isEmpty();
    }

    /**
     * ドロップダウンリストに表示する値を生成する.
     *
     * @param cd コード
     * @param nm 名称
     * @return DDLの表示値
     */
    public static String getDdlItem(Integer cd, String nm) {
        return makeDdlItem(String.valueOf(cd), nm);
    }

    /**
     * ドロップダウンリストに表示する値を生成する.
     *
     * @param cd コード
     * @param nm 名称
     * @return DDLの表示値
     */
    public static String getDdlItem(String cd, String nm) {
        return makeDdlItem(cd, nm);
    }

    /**
     * ドロップダウンリストに表示する値を生成する.
     *
     * @param cd コード
     * @param nm 名称
     * @return DDLの表示値
     */
    private static String makeDdlItem(String cd, String nm) {
        return cd + " " + nm;
    }

    /**
     * 渡された値を指定された日付フォーマットに変換する 変換できなかった場合は引数の値をそのまま返す
     *
     * @param target 変換対象の値
     * @param fmtFrom 変換前のフォーマット
     * @param fmtTo 変換後のフォーマット
     * @return 変換された値
     */
    public String tranDateFormat(String target, String fmtFrom, String fmtTo) {
        if (isBlank(target) || "0".equals(target)) {
            // 1 空文字或いは０の場合は処理しない
            return "";
        }
        String ret = "";
        try {
            // 1日付変換処理
            SimpleDateFormat sdfFrom = new SimpleDateFormat(fmtFrom);
            SimpleDateFormat sdfTo = new SimpleDateFormat(fmtTo);
            Date dt = sdfFrom.parse(target);
            ret = sdfTo.format(dt);

        } catch (ParseException e) {
            LogUtils.warn(String.format(
                            "Date format failed. Target value : %s   fmtFrom : %s   fmtTo : %s   Error message : %s",
                            target, fmtFrom, fmtTo, e.getMessage()));
            // フォーマットに失敗した場合は引数をそのまま返す
            ret = target;
        }
        return ret;
    }

    /**
     * 渡された値を指定された日付フォーマットに変換する 変換できなかった、有効日付ではなかった場合はNULLを返却
     *
     * @param target 変換対象の値
     * @param fmtFrom 変換前のフォーマット
     * @param fmtTo 変換後のフォーマット
     * @param isLenient 有効日付チェック可否
     * @return 変換された値
     */
    public String tranDateFormat(String target, String fmtFrom, String fmtTo, boolean isLenient) {
        if (isBlank(target) || "0".equals(target)) {
            // 1 空文字或いは０の場合は処理しない
            return "";
        }
        String ret = "";
        try {
            // 1日付変換処理
            SimpleDateFormat sdfFrom = new SimpleDateFormat(fmtFrom);
            SimpleDateFormat sdfTo = new SimpleDateFormat(fmtTo);

            // 有効日付チェックON(Falseでチェック有効になる)
            sdfFrom.setLenient(!isLenient);
            sdfTo.setLenient(!isLenient);

            Date dt = sdfFrom.parse(target);
            ret = sdfTo.format(dt);

        } catch (ParseException e) {
            LogUtils.warn(String.format(
                            "Date format failed. Target value : %s   fmtFrom : %s   fmtTo : %s   Error message : %s",
                            target, fmtFrom, fmtTo, e.getMessage()));
            // フォーマットに失敗した場合はNULLを返す
            ret = null;
        }
        return ret;
    }

    /**
     * 渡された日付値を指定された日付フォーマット（文字列）に変換する
     *
     * @param target 変換対象の値
     * @param fmtTo 変換後のフォーマット
     * @return 変換された値
     */
    public String tranDateFormat(Date target, String fmtTo) {
        if (target == null) {
            // nullの場合は処理しない
            return null;
        }

        // 1日付変換処理
        SimpleDateFormat sdfTo = new SimpleDateFormat(fmtTo);

        return sdfTo.format(target);
    }

    /**
     * 渡された値を指定された日付フォーマット変換可能であるかチェック
     *
     * @param target 変換対象の値
     * @param fmtTo 変換後のフォーマット
     * @return 変換された値
     */
    public boolean isTranDateFormat(String target, String fmtTo) {
        if (isBlank(target) || "0".equals(target)) {
            // 1 空文字或いは０の場合は処理しない
            return false;
        }

        try {
            // 1日付変換処理
            SimpleDateFormat sdfTo = new SimpleDateFormat(fmtTo);
            sdfTo.setLenient(false);

            Date dt = sdfTo.parse(target);
            sdfTo.format(dt);

        } catch (ParseException e) {
            // フォーマットに失敗した場合はfalse
            return false;
        }
        return true;
    }

    /**
     * 渡された値を指定された日付フォーマットに変換する 変換できなかった場合は引数の値をそのまま返す
     *
     * @param target 変換対象の値
     * @param fmtTo 変換後のフォーマット
     * @return 変換された値(日付型)
     */
    public Date tranDateFormatDate(String target, String fmtTo) {
        if (isBlank(target) || "0".equals(target)) {
            // 1 空文字或いは０の場合は処理しない
            return null;
        }
        Date dt = null;

        try {
            // 1日付変換処理
            SimpleDateFormat sdfTo = new SimpleDateFormat(fmtTo);
            dt = sdfTo.parse(target);

        } catch (ParseException e) {
            LogUtils.warn(String.format("Date format failed. Target value : %s   fmtTo : %s   Error message : %s",
                            target, fmtTo, e.getMessage()));
        }
        return dt;
    }

    /**
     * 渡された日付に指定された日付の加減算を行う
     *
     * @param target 変換対象の値
     * @param day 加減算する日付
     * @return 変換された値(日付型)
     */
    public Date dateCalc(Date target, Integer day) {
        if (target == null) {
            // 指定日付が空の場合は処理しない
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(target);
        calendar.add(Calendar.DAY_OF_MONTH, day);

        return calendar.getTime();
    }

    /**
     * 3桁区切りの数値をクリアする
     *
     * @param target クリア対象の値
     * @return クリア後の値
     */
    public static String clearNumFmt(String target) {
        if (isEmpty(target)) {
            return "";
        }
        return target.replaceAll(Const.COMMA, "");
    }

    /**
     * 渡された値が数値に変換できる場合に、 前方のゼロ埋めを消去して返す
     *
     * @param str 対象の文字列
     * @return 省略
     */
    public static String removeZeroPad(String str) {
        Integer ret = 0;
        try {
            ret = Integer.parseInt(str);
        } catch (@SuppressWarnings("unused") Exception e) {
            // 数値に変換できない場合は引数をそのまま返す
            return str;
        }
        return String.valueOf(ret);
    }

    /**
     * <pre>
     * 第一引数の文字列が、第二引数以降の文字列群の中に一致があるか判定する
     * </pre>
     *
     * @param baseStr 判定対象文字列
     * @param strings 判定文字列群
     * @return true：一致する文字列がある false：一致する文字列がない
     */
    public static boolean equals(Object baseStr, Object... strings) {
        if (baseStr != null && strings != null) {
            for (Object targetStr : strings) {
                if (toString(baseStr).equals(toString(targetStr))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <pre>
     * 一致する文字列を含んでいるかどうかを判定して返す
     * </pre>
     *
     * @param baseList 比較対象文字列リスト
     * @param strings 文字列群
     * @return true：一致する文字列あり false：一致する文字列なし
     */
    public static boolean contains(String[] baseList, String... strings) {
        boolean equals = false;
        if (baseList != null && strings != null) {
            BREAKPOINT: for (int i = 0; i < baseList.length; i++) {
                for (int j = 0; j < strings.length; j++) {
                    if (baseList[i].equals(strings[j])) {
                        equals = true;
                        break BREAKPOINT;
                    }
                }
            }
        }
        return equals;
    }

    /**
     * <pre>
     * 一致する文字列を含んでいるかどうかを判定して返す
     * </pre>
     *
     * @param baseList 比較対象文字列リスト
     * @param strings 文字列群
     * @return true：一致する文字列あり false：一致する文字列なし
     */
    public static boolean contains(List<String> baseList, String... strings) {
        boolean contains = false;
        if (baseList != null && strings != null) {
            for (int i = 0; i < strings.length; i++) {
                if (baseList.contains(strings[i])) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * <pre>
     * 末尾文字カット。
     * 左（先頭）から指定文字数分を返す。
     * 文字サイズが指定桁数よりも小さいときは、その文字列を返す
     * </pre>
     *
     * @param str 切り取り対象文字
     * @param num 指定桁数
     * @return 切り取られた文字
     */
    public static String left(String str, Integer num) {
        return left(str, num, str);
    }

    /**
     * <pre>
     * 末尾文字カット。
     * 左（先頭）から指定文字数分を返す。
     * 文字サイズが指定桁数よりも小さいときは、第三引数の文字列を返す。
     * </pre>
     *
     * @param str 切り取り対象文字
     * @param num 指定桁数
     * @param rtnStr 文字サイズが指定桁数よりも小さい場合の返却値
     * @return 切り取られた文字
     */
    public static String left(String str, Integer num, String rtnStr) {
        if (str != null && str.length() > num) {
            return str.substring(0, num);
        }
        return rtnStr;
    }

    /**
     * <pre>
     * 末尾文字カット。
     * 左（先頭）から指定バイト数分を返す。
     * カット時、末尾文字が全角の場合、文字化けしないようにカット
     * </pre>
     *
     * @param str 切り取り対象文字
     * @param bNum 指定バイト数
     * @param encode エンコード文字(SJIS、UTF8など)
     * @return 切り取られた文字
     * @throws Exception
     */
    public static String leftB(String str, Integer bNum, String encode) {
        StringBuffer sb = new StringBuffer();
        Integer cnt = 0;

        try {
            for (int i = 0; i < str.length(); i++) {
                String tmpStr = str.substring(i, i + 1);
                byte[] b = tmpStr.getBytes(encode);
                if (cnt + b.length > bNum) {
                    return sb.toString();
                } else {
                    sb.append(tmpStr);
                    cnt += b.length;
                }
            }
        } catch (Exception ex) {
            LogUtils.error(ex);
            throw new CPException(ex.getMessage());
        }
        return sb.toString();
    }

    /**
     * <pre>
     * 先頭文字カット。
     * 右（末尾）から指定文字数分を返す。
     * 文字サイズが指定桁数よりも小さいときは、その文字列を返す
     * </pre>
     *
     * @param str 切り取り対象文字
     * @param num 指定桁数
     * @return 切り取られた文字
     */
    public static String right(String str, Integer num) {
        return right(str, num, str);
    }

    /**
     * <pre>
     * 先頭文字カット。
     * 右（末尾）から指定文字数分を返す
     * 文字サイズが指定桁数よりも小さいときは、第三引数の文字列を返す。
     * </pre>
     *
     * @param str 切り取り対象文字
     * @param num 指定桁数
     * @return 切り取られた文字
     */
    public static String right(String str, Integer num, String rtnStr) {
        if (str != null && str.length() > num) {
            return str.substring(str.length() - num);
        }
        return rtnStr;
    }

    /**
     * <pre>
     * マトリクスデータ（二次元配列）から指定された行データ（全列分）を返す
     * 先頭から検索して、最初に該当した行データを返す
     * </pre>
     *
     * @param table マトリクスデータ（二次元配列）
     * @param keys 行指定のためのキー
     * @return 行データ（全列分）
     */
    public static String[] getTableRecord(String[][] table, String... keys) {

        if (table != null && keys != null && table[0].length >= keys.length) {
            // テーブル分ループ
            for (int i = 0; i < table.length; i++) {
                boolean hit = true;

                // キー分ループ
                for (int j = 0; j < keys.length; j++) {
                    // キーが不一致になった時点で、次の行の判定へ
                    if (!table[i][j].equals(keys[j])) {
                        hit = false;
                        break;
                    }
                }

                // キーが全て一致した場合、その行データを返す
                if (hit) {
                    return table[i];
                }
            }
        }

        return new String[0];
    }

    /**
     * <pre>
     * マトリクスデータ（二次元配列）から指定された行の値（指定列）を返す
     * 先頭から検索して、最初に該当した行データの指定された列番号の値の返す
     * </pre>
     *
     * @param table マトリクスデータ（二次元配列）
     * @param idx 取得列の番号
     * @param keys 行指定のためのキー
     * @return 値（指定列の値）
     */
    public static String getTableData(String[][] table, Integer idx, String... keys) {
        String[] tableRecord = getTableRecord(table, keys);
        if (tableRecord != null && tableRecord.length > idx) {
            return tableRecord[idx];
        }
        return null;
    }

    /**
     * <pre>
     * マトリクスデータ（二次元配列）から指定された行の値（最終列の値）を返す
     * 先頭から検索して、最初に該当した行データを返す
     * </pre>
     *
     * @param table マトリクスデータ（二次元配列）
     * @param keys 行指定のためのキー
     * @return 値（最終列の値）
     */
    public static String getTableData(String[][] table, String... keys) {
        if (table != null && keys != null && table[0].length >= keys.length) {
            return getTableData(table, table[0].length - 1, keys);
        }
        return null;
    }

    /**
     * <pre>
     * マトリクスデータ（二次元配列）から指定された列のみの配列を返す
     * </pre>
     *
     * @param table マトリクスデータ（二次元配列）
     * @param columnNo 取得対象列番号
     * @return 取得列データ（配列）
     */
    public static String[] getTableSingleColumnList(String[][] table, Integer columnNo, boolean topBlank) {
        String[] singleColumnList = null;
        if (table != null) {
            Integer addCnt = topBlank ? 1 : 0;
            singleColumnList = new String[table.length + addCnt];
            for (int i = 0; i < table.length; i++) {
                singleColumnList[i + addCnt] = table[i + addCnt][columnNo];
            }
        }
        return singleColumnList;
    }

    /**
     * <pre>
     * ゼロサプレス（前ゼロカット）
     * </pre>
     *
     * @param strVal 対象文字列
     * @return 加工後文字列
     */
    public static String zerosuppress(String strVal) {

        if (isEmpty(strVal)) {
            return strVal;
        }

        return String.valueOf(Long.parseLong(strVal));
    }

    /**
     * <pre>
     * ゼロパディング（String型版）
     * </pre>
     *
     * @param strVal 対象文字列
     * @param keta 完成系文字列桁数
     * @return 連結後の文字列
     */
    public static String zeropadding(String strVal, Integer keta) {
        return padding(strVal, "0", keta, true);
    }

    /**
     * <pre>
     * ゼロパディング（int型版）
     * </pre>
     *
     * @param num 対象数値
     * @param keta 完成系文字列桁数
     * @return 加工後文字列
     */
    public static String zeropadding(Integer num, Integer keta) {
        return padding(String.valueOf(num), "0", keta, true);
    }

    /**
     * <pre>
     * 第一引数文字列に第三引数桁数になるまで、先頭に第二引数文字列を加える
     * </pre>
     *
     * @param str 対象文字列
     * @param s 追加する文字列
     * @param keta 完成系文字列桁数
     * @return 連結後の文字列
     */
    public static String padding(String str, String s, Integer keta) {
        return padding(str, s, keta, true);
    }

    /**
     * <pre>
     * 第一引数文字列に第三引数桁数になるまで、第二引数文字列を加える
     * </pre>
     *
     * @param str 対象文字列
     * @param s 追加する文字列
     * @param keta 完成系文字列桁数
     * @param frontJoin true：前方連結 false：後方連結
     * @return 連結後の文字列
     */
    public static String padding(String str, String s, Integer keta, boolean frontJoin) {

        if (isEmpty(str)) {
            return str;
        }

        String rtnStr = toString(str);

        // 既に指定桁数以上のときはそのまま返す
        if (toString(str).length() < keta) {

            // 連結文字がブランクだったら無限ループになってしまうので、半角スペースに変換する
            String renketumoji = s;
            if (isEmpty(s)) {
                renketumoji = " ";
            }

            StringBuilder rtnBuil = new StringBuilder();
            rtnBuil.append(toString(str));

            // 前方連結
            if (frontJoin) {
                while (rtnBuil.toString().length() < keta) {
                    rtnBuil.insert(0, renketumoji);
                }

                // 後方連結
            } else {
                while (rtnBuil.toString().length() < keta) {
                    rtnBuil.append(renketumoji);
                }
            }

            // 連結文字が２文字以上のとき、指定した桁数を超えてしまう場合がある。
            // それを考慮して、最後に無条件でsubstringする
            rtnStr = rtnBuil.toString().substring(0, keta);
        }

        return rtnStr;
    }

    /**
     * <pre>
     * String型をint型に変換して返す
     * 変換できないときはゼロを返す
     * </pre>
     *
     * @param str 変換対象数値
     * @return 数値
     */
    public static Integer toInt(String str) {
        return toInt(str, true);
    }

    /**
     * <pre>
     * String型をint型に変換して返す
     * 変換できないときはゼロを返す
     * </pre>
     *
     * @param str 変換対象数値
     * @param escapeException 例外時にエスケープ（ゼロを返す）するか
     * @return 数値
     */
    public static Integer toInt(String str, boolean escapeException) {
        Integer num = 0;
        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException nex) {
            if (!escapeException) {
                throw nex;
            }
        }
        return num;
    }

    /**
     * 数値変換処理(String -> Integer) ① 引数が空白文字の場合、0を返却する ② 全ての半角スペースを除去する ③ 小数値を切り捨て、整数値を返却する
     *
     * @param num 数値変換したい値
     * @return 数値変換後の値
     */
    public static Integer toIntByFormValue(String num) {
        // 引数チェック
        String tempString = num;
        if (isEmptyTrimBlank(num)) {
            return 0;
        }
        // 半角スペース除去
        tempString = tempString.replace(Const.HALF_SPACE, "");
        // 小数値の切り捨て
        String[] tempArray = tempString.split("\\" + Const.DOT);
        if (isEmptyTrimBlank(tempArray[0])) {
            return 0;
        }
        // 整数値変換
        Integer returnValue = 0;
        try {
            returnValue = Integer.parseInt(tempArray[0]);
        } catch (Exception ex) {
            return 0;
        }
        return returnValue;
    }

    /**
     * <pre>
     * String型をlong型に変換して返す
     * 変換できないときはゼロを返す
     * </pre>
     *
     * @param str 変換対象数値
     * @return 数値
     */
    public static long toLong(String str) {
        return toLong(str, true);
    }

    /**
     * <pre>
     * String型をlong型に変換して返す
     * 変換できないときはゼロを返す
     * </pre>
     *
     * @param str 変換対象数値
     * @param escapeException 例外時にエスケープ（ゼロを返す）するか
     * @return 数値
     */
    public static long toLong(String str, boolean escapeException) {
        long num = 0;
        try {
            num = Long.parseLong(str);
        } catch (NumberFormatException nex) {
            if (!escapeException) {
                throw nex;
            }
        }
        return num;
    }

    /**
     * 数値変換処理(String -> long) ① 引数が空白文字の場合、0を返却する ② 全ての半角スペースを除去する ③ 小数値を切り捨て、整数値を返却する
     *
     * @param num 数値変換したい値
     * @return 数値変換後の値
     */
    public static long toLongByFormValue(String num) {
        // 引数チェック
        String tempString = num;
        if (isEmptyTrimBlank(num)) {
            return 0;
        }
        // 半角スペース除去
        tempString = tempString.replace(Const.HALF_SPACE, "");
        // 小数値の切り捨て
        String[] tempArray = tempString.split("\\" + Const.DOT);
        if (isEmptyTrimBlank(tempArray[0])) {
            return 0;
        }
        // 整数値変換
        long returnValue = 0;
        try {
            returnValue = Long.parseLong(tempArray[0]);
        } catch (Exception ex) {
            return 0;
        }
        return returnValue;
    }

    /**
     * 型を判定する
     *
     * @param val 変換対象値
     * @return 型
     */
    public static Type judgeType(Object val) {
        Type rtnType = Type.OTHER;

        if (val == null) {
            rtnType = Type.NULL;
        } else if (val instanceof String) {
            rtnType = Type.STRING;
        } else if (val instanceof Character) {
            rtnType = Type.CHAR;
        } else if (val instanceof BigDecimal) {
            rtnType = Type.BIGDECIMAL;
        } else if (val instanceof Double) {
            rtnType = Type.DOUBLE;
        } else if (val instanceof Byte) {
            rtnType = Type.BYTE;
        } else if (val instanceof Float) {
            rtnType = Type.FLOAT;
        } else if (val instanceof Integer) {
            rtnType = Type.INTEGER;
        } else if (val instanceof Long) {
            rtnType = Type.LONG;
        } else if (val instanceof Short) {
            rtnType = Type.SHORT;
        } else if (val instanceof Date) {
            rtnType = Type.DATE;
        } else if (val instanceof Boolean) {
            rtnType = Type.BOOLEAN;
        }

        return rtnType;
    }

    /**
     * String型に変換して返す。 ※nullのときはNullPointerException
     *
     * @param val 変換対象値
     * @return String型に変換された値
     */
    public static String toString(Object val) {
        if (judgeType(val) == Type.NULL) {
            throw new NullPointerException();
        }
        return toString(val, null);
    }

    /**
     * String型に変換して返す。
     *
     * @param val 変換対象値
     * @param defaultVal 変換対象値がnullの場合、返却する値 ※null設定時はnullを返す
     * @return String型に変換された値
     */
    public static String toString(Object val, String defaultVal) {
        switch (judgeType(val)) {
            case STRING:
            case CHAR:
            case BIGDECIMAL:
                return val.toString();
            case DOUBLE:
            case BYTE:
            case FLOAT:
            case INTEGER:
            case LONG:
            case SHORT:
            case BOOLEAN:
                return String.valueOf(val);
            default:
                break;
        }

        return defaultVal;
    }

    /**
     * BigDecimal型に変換して返す。 ※nullのときはNullPointerException
     *
     * @param val 変換対象値
     * @return BigDecimal型に変換された値
     */
    public static BigDecimal toBigDecimal(Object val) {
        if (judgeType(val) == Type.NULL) {
            throw new NullPointerException();
        }
        return toBigDecimal(val, null);
    }

    /**
     * BigDecimal型に変換して返す。
     *
     * @param val 変換対象値
     * @param defaultVal 変換対象値がnullの場合、返却する値 ※null設定時はnullを返す
     * @return BigDecimal型に変換された値
     */
    public static BigDecimal toBigDecimal(Object val, BigDecimal defaultVal) {
        switch (judgeType(val)) {
            case STRING:
                return new BigDecimal((String) val);
            case BIGDECIMAL:
                return new BigDecimal(val.toString());
            case DOUBLE:
                return BigDecimal.valueOf((Double) val);
            case FLOAT:
                return BigDecimal.valueOf((Float) val);
            case INTEGER:
                return new BigDecimal((Integer) val);
            case LONG:
                return new BigDecimal((Long) val);
            case SHORT:
                return new BigDecimal((Short) val);
            case NULL:
                break;
            default:
                throw new NumberFormatException();
        }

        return defaultVal;
    }

    /**
     * Integer型に変換して返す。
     *
     * @param val 変換対象値
     * @return BigDecimal型に変換された値
     */
    @SuppressWarnings("unused")
    public static Integer toInteger(String val) {
        Integer rtn = 0;
        try {
            rtn = Integer.parseInt(toString(val, null));
        } catch (Exception e) {
            return null;
        }
        return rtn;
    }

    /**
     * 数値変換処理(String -> BigDecimal) ① 引数が空白文字の場合、0を返却する ② 全ての半角スペースを除去する
     *
     * @param num 数値変換したい値
     * @return 数値変換後の値
     */
    public static BigDecimal toDecimalByFormValue(String num) {
        // 引数チェック
        String tempString = num;
        if (isEmptyTrimBlank(num)) {
            return BigDecimal.ZERO;
        }
        // 半角スペース除去
        tempString = tempString.replace(Const.HALF_SPACE, "");
        // 小数値変換
        BigDecimal returnValue = BigDecimal.ZERO;
        try {
            returnValue = new BigDecimal(tempString);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
        return returnValue;
    }

    /**
     * 数値変換処理(String -> double) ① 引数が空白文字の場合、0を返却する ② 全ての半角スペースを除去する
     *
     * @param num 数値変換したい値
     * @return 数値変換後の値
     */
    public static double toDoubleByFormValue(String num) {
        // 引数チェック
        String tempString = num;
        if (isEmptyTrimBlank(num)) {
            return 0;
        }
        // 半角スペース除去
        tempString = tempString.replace(Const.HALF_SPACE, "");
        // 小数値変換
        double returnValue = 0;
        try {
            returnValue = Double.parseDouble(tempString);
        } catch (Exception ex) {
            return 0;
        }
        return returnValue;
    }

    /**
     * <pre>
     * String型の引数がブランクの場合に、NULLにして返す
     * </pre>
     *
     * @param str 変換対象文字列
     * @return 変換後の文字列
     */
    public static String toNull(String str) {
        if (str != null && "".equals(str)) {
            return null;
        }
        return str;
    }

    /**
     * <pre>
     * null空チェック。
     * </pre>
     *
     * @param obj 判定対象オブジェクト
     * @return true ：null or 空 false：null or 空でない
     */
    public static boolean isEmpty(Object obj) {
        return obj == null || (obj instanceof String && "".equals(obj.toString()));
    }

    /**
     * <pre>
     * null空チェック（前後のスペースをトリムして判定する）。
     * </pre>
     *
     * @param obj 判定対象オブジェクト
     * @return true ：null or 空 false：null or 空でない
     */
    public static boolean isEmptyTrimBlank(Object obj) {
        return obj == null || (obj instanceof String && "".equals(obj.toString().trim()));
    }

    /**
     * <pre>
     * 数値有無を判定して返す
     * </pre>
     *
     * @param value 判定対象文字列
     * @return true：数値データ false：数値データでない
     */
    public static boolean isNumber(String value) {
        try {
            Long.parseLong(value);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * <pre>
     * 可変文字列List変換
     * 可変引数の文字列をArrayList型に変換して返す
     * </pre>
     *
     * @param strings 文字列群（可変引数）
     * @return 加工後ArrayListデータ
     */
    public static List<String> convertFromStringsToArrayList(String... strings) {
        List<String> list = null;
        if (strings != null) {
            list = Arrays.asList(strings);
        }
        return list;
    }

    /**
     * <pre>
     * List→文字列変換（連結子指定）
     * </pre>
     *
     * @param list リスト
     * @param delimita 連結子
     * @return 変換後文字列
     */
    public static String convertFromArrayToString(List<String> list, String delimita) {
        StringBuilder s = new StringBuilder();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    s.append(",");
                }
                s.append(list.get(i));
            }
        }
        return s.toString();
    }

    /**
     * <pre>
     * List→文字列配列変換
     * </pre>
     *
     * @param fromList リスト
     * @return 変換後文字列
     */
    public static String[] convertFromArrayToStrings(List<String> fromList) {
        String[] toList = null;
        if (fromList != null) {
            toList = new String[fromList.size()];
            for (int i = 0; i < fromList.size(); i++) {
                toList[i] = fromList.get(i);
            }
        }
        return toList;
    }

    /**
     * <pre>
     * List→文字列変換（カンマ連結）
     * </pre>
     *
     * @param list リスト
     * @return 変換後文字列
     */
    public static String convertFromArrayToString(List<String> list) {
        return convertFromArrayToString(list, Const.COMMA);
    }

    /**
     * <pre>
     * 改行コードで分解する
     * </pre>
     *
     * @param str 対象文字列
     * @return 分解後の文字列
     */
    public static String[] splitParagraphCode(String str) {
        String[] rtnList = null;
        if (str != null) {
            List<String> list = new ArrayList<>();
            String[] strS1 = str.split("\r\n");
            for (int i = 0; i < strS1.length; i++) {
                String[] strS2 = strS1[i].split("\n");
                list.addAll(Arrays.asList(strS2));
            }
            rtnList = list.toArray(new String[0]);
        }
        return rtnList;
    }

    /**
     * <pre>
     * 改行コードをカットする
     * </pre>
     *
     * @param str 対象文字列
     * @return カット後の文字列
     */
    public static String cutParagraphCode(String str) {
        String rtnStr = null;
        if (str != null) {
            rtnStr = str.replace("\r\n", "");
            rtnStr = rtnStr.replace("\n", "");
        }
        return rtnStr;
    }

    /**
     * <pre>
     * 第一引数文字列の中に存在する改行コードを入力不可能文字(※)に変換する。
     * 第一引数文字列の中に存在する入力不可能文字を改行コードに変換する。
     *
     * ※入力不可能文字＝アスキーコード127
     * </pre>
     *
     * @param str 変換対象文字列
     * @param forward 変換方向 true ：改行コードをアスキーコードに変換 false：アスキーコードを改行コードに変換
     * @return 変換後の文字列
     */
    public static String convertParagraphCode(String str, boolean forward) {
        String rtnStr = null;
        if (str != null) {
            String convertedAscii = new String(new byte[] {127}, StandardCharsets.US_ASCII);
            if (forward) {
                // 改行コード変換
                rtnStr = str.replace("\r\n", convertedAscii);
                rtnStr = rtnStr.replace("\n", convertedAscii);
            } else {
                // 改行コード逆変換
                rtnStr = str.replace(convertedAscii, "\r\n");
            }
        }
        return rtnStr;
    }

    /**
     * <pre>
     * NULL値変換。
     * 第一引数がNULLのとき、第二引数を返す。
     * 第一引数がNULLでないとき、判定対象文字列をそのまま返す。
     * </pre>
     *
     * @param baseValue 判定対象文字列
     * @param convValue1 変換後文字列（NULLのとき用）
     * @return 変換後の文字列
     */
    public static String convertNull(String baseValue, String convValue1) {
        return convertNull(baseValue, convValue1, false);
    }

    /**
     * <pre>
     * NULL値変換。
     * 第一引数がNULLのとき、第二引数を返す。
     * 第一引数がNULLでないとき、判定対象文字列をそのまま返す。
     *
     * ※第三引数で、ブランクをNULLとみなすかどうかを指定できる
     * </pre>
     *
     * @param baseValue 判定対象文字列
     * @param convValue1 変換後文字列（NULLのとき用）
     * @param blankIsNull ブランクをNULLとみなすか（true：みなす false：みなさない）
     * @return 変換後の文字列
     */
    public static String convertNull(String baseValue, String convValue1, boolean blankIsNull) {
        return convertNull(baseValue, convValue1, baseValue, blankIsNull);
    }

    /**
     * <pre>
     * NULL値変換。
     * 第一引数がNULLのとき、第二引数を返す。
     * 第一引数がNULLでないとき、第三引数を返す。
     * </pre>
     *
     * @param baseValue 判定対象文字列
     * @param convValue1 変換後文字列（NULLのとき用）
     * @param convValue2 変換後文字列（NULLでないとき用）
     * @return 変換後の文字列
     */
    public static String convertNull(String baseValue, String convValue1, String convValue2) {
        return convertNull(baseValue, convValue1, convValue2, false);
    }

    /**
     * <pre>
     * NULL値変換。
     * 第一引数がNULLのとき、第二引数を返す。
     * 第一引数がNULLでないとき、第三引数を返す。
     *
     * ※第四引数で、ブランクをNULLとみなすかどうかを指定できる
     * </pre>
     *
     * @param baseValue 判定対象文字列
     * @param convValue1 変換後文字列（NULLのとき用）
     * @param convValue2 変換後文字列（NULLでないとき用）
     * @param blankIsNull ブランクをNULLとみなすか（true：みなす false：みなさない）
     * @return 変換後の文字列
     */
    public static String convertNull(String baseValue, String convValue1, String convValue2, boolean blankIsNull) {
        if (blankIsNull && "".equals(baseValue)) {
            return convValue1;
        }
        if (baseValue == null) {
            return convValue1;
        }
        return convValue2;
    }

    /**
     * <pre>
     * NULL値変換。
     * 第一引数がNULLのとき、第二引数を返す。
     *
     * </pre>
     *
     * @param baseValue 判定対象値
     * @param convValue1 変換後値（NULLのとき用）
     * @return 変換後の値
     */
    public static BigDecimal convertNull(BigDecimal baseValue, BigDecimal convValue1) {
        return convertNull(baseValue, convValue1, baseValue);
    }

    /**
     * <pre>
     * NULL値変換。
     * 第一引数がNULLのとき、第二引数を返す。
     * 第一引数がNULLでないとき、第三引数を返す。
     * </pre>
     *
     * @param baseValue 判定対象値
     * @param convValue1 変換後値（NULLのとき用）
     * @param convValue2 変換後値（NULLでないとき用）
     * @return 変換後の値
     */
    public static BigDecimal convertNull(BigDecimal baseValue, BigDecimal convValue1, BigDecimal convValue2) {
        if (baseValue == null) {
            return convValue1;
        }
        return convValue2;
    }

    /**
     * 文字列を指定桁数の数ごとに分解する
     *
     * @param mojiList 対象文字列（単数）
     * @param maxLen 指定桁数
     * @return 分解した文字リスト
     */
    public static List<String> divide(String moji, int maxLen) {
        List<String> rtnList = new ArrayList<>();
        Integer lineCounter = 0;
        // 繰り返し条件
        // ・最初の一行目
        // ・または、開始indexが文字桁数を越えるまで
        while (lineCounter == 0 || moji.length() > maxLen * lineCounter) {
            rtnList.add(substring(moji, maxLen * lineCounter, maxLen * (lineCounter + 1)));
            lineCounter++;
        }
        return rtnList;
    }

    /**
     * 文字列を指定桁数の数ごとに分解する
     *
     * @param mojiList 対象文字列（複数）
     * @param maxLen 指定桁数
     * @return 分解した文字リスト
     */
    public static List<String> divide(String[] mojiList, int maxLen) {
        List<String> rtnList = new ArrayList<>();
        for (int i = 0; i < mojiList.length; i++) {
            rtnList.addAll(divide(mojiList[i], maxLen));
        }
        return rtnList;
    }

    /**
     * 桁数指定した範囲の文字列を返す
     *
     * @param str 対象文字列
     * @param start 開始index
     * @param end 終了index
     * @return 抽出された文字列
     */
    public static String substring(String str, Integer start, Integer end) {
        // 文字列がnull、または、開始indexと終了indexの関係が不正の場合はnullを返す
        if (str == null || start >= end) {
            return null;
        }
        // 文字桁数が開始index以下の場合はブランクを返す
        if (str.length() <= start) {
            return "";
        }
        // 文字桁数が終了index以下の場合は、開始index以降の文字列を返す
        if (str.length() <= end) {
            return str.substring(start);
        }
        // 開始index～終了indexの範囲の文字列を返す
        return str.substring(start, end);
    }

    /**
     * String配列を１つの文字列に連結する
     *
     * @param strings String配列
     * @return 連結文字列
     */
    public static String join(String... strings) {
        StringBuilder rtn = new StringBuilder();
        if (strings != null) {
            for (int i = 0; i < strings.length; i++) {
                rtn.append(strings[i]);
            }
        }
        return rtn.toString();
    }

    /**
     * String文字列を指定された数分連結する
     *
     * @param s 文字列
     * @param num 連結回数
     * @return 連結文字列
     */
    public static String join(String s, Integer count) {
        StringBuilder rtn = new StringBuilder();
        if (s != null) {
            for (int i = 0; i < count; i++) {
                rtn.append(s);
            }
        }
        return rtn.toString();
    }

    /**
     * 文字列リストを１つの文字列に連結する
     *
     * @param list 文字列リスト
     * @return 連結文字列
     */
    public static String join(List<String> list) {
        StringBuilder rtn = new StringBuilder();
        if (list != null) {
            rtn = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                rtn.append(list.get(i));
            }
        }
        return rtn.toString();
    }

    /**
     * String配列を１つの文字列に連結する
     *
     * @param delimita 連結子
     * @param strings String配列
     * @return 連結文字列
     */
    public static String joinByDelimita(String delimita, String... strings) {
        StringBuilder rtn = new StringBuilder();
        if (strings != null) {
            for (int i = 0; i < strings.length; i++) {
                if (i > 0) {
                    rtn.append(delimita);
                }
                rtn.append(strings[i]);
            }
        }
        return rtn.toString();
    }

    /**
     * String配列を１つの文字列に連結する(ブランクは除外)
     *
     * @param delimita 連結子
     * @param strings String配列
     * @return 連結文字列
     */
    public static String joinExceptBlankByDelimita(String delimita, Object... strings) {
        String cutVal = "";
        StringBuilder rtn = new StringBuilder();
        if (strings != null) {
            for (Object target : strings) {
                if (isEmpty(target)) {
                    continue;
                }
                if (!isEmpty(cutVal)) {
                    rtn.append(cutVal);
                }
                rtn.append(toString(target));
                cutVal = delimita;
            }
        }
        return rtn.toString();
    }

    /**
     * String配列を１つの文字列に連結する
     *
     * @param delimita 連結子
     * @param list String配列
     * @return 連結文字列
     */
    public static String joinByDelimita(String delimita, List<String> list) {
        StringBuilder rtn = new StringBuilder();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    rtn.append(delimita);
                }
                rtn.append(list.get(i));
            }
        }
        return rtn.toString();
    }

    /**
     * 文字列の桁数判定
     *
     * @param value 判定対象文字列
     * @param length 判定桁数
     * @return
     * 
     *         <pre>
     *       0：判定不能（文字列がnull）
     *       1：判定桁数より小さい
     *       2：判定桁数と一致
     *       3：判定桁数より大きい
     *         </pre>
     */
    public static Integer judgeStrLength(String value, long length) {
        Integer result = 0;
        if (value != null) {
            if (value.length() < length) {
                result = 1;
            } else if (value.length() > length) {
                result = 3;
            } else {
                result = 2;
            }
        }
        return result;
    }

    /**
     * 引数の先頭文字を大文字に変換する
     *
     * @param s 変換対象文字列
     * @return 変換後文字列
     */
    public static String convertUpperTop(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder convertedVal = new StringBuilder();
        convertedVal.append(left(s, 1).toUpperCase());
        if (s.length() > 1) {
            convertedVal.append(s.substring(1));
        }
        return convertedVal.toString();
    }

    /**
     * 文字列をユニコードに変換する
     *
     * @param str 変換対象文字列
     * @return 変換後ユニコード
     */
    public static String convertToUnicode(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            Integer bytelen = str.substring(i, i + 1).getBytes().length;
            if (bytelen == 1) {
                sb.append(str.substring(i, i + 1));
                continue;
            }
            sb.append(String.format("\\u%04X", Character.codePointAt(str, i)));
        }
        return sb.toString();
    }

    /**
     * ユニコードを文字列に変換する
     *
     * @param unicodes 変換対象ユニコード
     * @return 変換後文字列
     */
    public static String convertToOriginal(String unicodes) {
        if (unicodes == null) {
            return null;
        }
        // ユニコードを分解
        String[] preUnicodes = unicodes.split("\\\\u");
        if (preUnicodes.length == 1) {
            preUnicodes = unicodes.split("%u");
        }
        // ユニコードが１文字も含まれていない場合は、そのまま返す
        if (preUnicodes.length == 1) {
            return unicodes.replace("%0D%0A", Const.LN_SPRT);
        }

        StringBuilder sb = new StringBuilder();

        // ユニコード分解した配列の先頭インデックスはユニコードでないためそのままセット
        sb.append(preUnicodes[0]);

        // ユニコードを文字に変換
        List<String> strs = new ArrayList<>();
        int[] codePoints = new int[preUnicodes.length - 1];
        for (int i = 0; i < codePoints.length; i++) {
            // ユニコードを文字コードに変換
            String unicode = left(preUnicodes[i + 1], 4);
            codePoints[i] = Integer.parseInt(unicode, 16);

            // ユニコードでない文字を保持（後で連結するため）
            strs.add(right(preUnicodes[i + 1], preUnicodes[i + 1].length() - 4, ""));
        }
        // ユニコードだけを文字列に変換
        sb.append(new String(codePoints, 0, codePoints.length));

        // さっきのループで保持しておいたユニコードでない文字を所定の場所に連結
        for (int i = strs.size() - 1; i >= 0; i--) {
            String s = strs.get(i);
            if (!"".equals(s)) {
                sb.insert(i + preUnicodes[0].length() + 1, s);
            }
        }

        // 完成
        return sb.toString().replace("%0D%0A", Const.LN_SPRT);
    }

    /**
     * ファイル/フォルダ名に使用不可の記号を変換
     *
     * @param value 変換前のファイル/フォルダ名
     * @return 変換後のファイル/フォルダ名
     */
    public static String replaceFileName(String value) {
        if (value == null) {
            return null;
        }

        // 変換パターン
        String replacePattern = "[\\\\/:*?\"<>|]";

        return value.replaceAll(replacePattern, Const.UNDERBAR);
    }

    /**
     * 右トリム
     *
     * @param str 変換対象文字列
     * @return 変換後文字列
     */
    public static String rTrim(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll(" +$", "");
    }

    /**
     * 前後空白トリム（全角半角）
     *
     * @param str 変換対象文字列
     * @return 変換後文字列
     */
    public static String allTrim(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("^(\\s|　)+|(\\s|　)+$", "");
    }

    /**
     * 引数の値が全て一致するか判定して返す
     *
     * @param values 判定対象文字列
     * @return true:すべて一致 false：１個以上不一致がある
     */
    public static boolean equalsAll(String... values) {
        if (values == null) {
            return false;
        }
        String tmpValue = "";
        for (int i = 0; i < values.length; i++) {
            if (i > 0 && !tmpValue.equals(values[i])) {
                return false;
            }
            tmpValue = values[i];
        }
        return true;
    }

    /**
     * 第一引数の値が、第二引数の値と全て一致するか判定して返す
     *
     * @param value 判定対象文字列
     * @param values 判定対象文字列群
     * @return true:すべて一致 false：１個以上不一致がある
     */
    public static boolean equalsAll(String value, String... values) {
        if (value == null || values == null) {
            return false;
        }
        for (int i = 0; i < values.length; i++) {
            if (i > 0 && !value.equals(values[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 引数の値が全て一致するか判定して返す
     *
     * @param values 判定対象文字列
     * @return true:すべて一致 false：１個以上不一致がある
     */
    public static boolean equalsAll(List<Map<String, String>> lists, String mapKey) {
        if (lists == null) {
            return false;
        }
        String tmpValue = "";
        for (int i = 0; i < lists.size(); i++) {
            String value = lists.get(i).get(mapKey);
            if (i > 0 && !ComUtils.equals(tmpValue, value)) {
                return false;
            }
            tmpValue = value;
        }
        return true;
    }

    /**
     * 第一引数の値が、第二引数の値と全て一致するか判定して返す
     *
     * @param value 判定対象文字列
     * @param lists
     * @param mapKey
     * @return true:すべて一致 false：１個以上不一致がある
     */
    public static boolean equalsAll(String value, List<Map<String, String>> lists, String mapKey) {
        if (lists == null) {
            return false;
        }
        for (int i = 0; i < lists.size(); i++) {
            if (i > 0 && !ComUtils.equals(value, lists.get(i).get(mapKey))) {
                return false;
            }
        }
        return true;
    }

    /**
     * リストテーブルをマップテーブルに置き換える（コードと名称の組み合わせ）
     *
     * @param mapList 変換対象リストテーブル
     * @param codeName キー（リストテーブルにあるマップの値）
     * @param labelName 値（リストテーブルにあるマップの値）
     * @return マップ
     */
    public static Map<String, String> convertListTableToMapTable(List<Map<String, String>> mapList, String codeName,
                    String labelName) {
        Map<String, String> returnMap = null;
        if (mapList != null) {
            returnMap = new HashMap<>();
            for (int i = 0; i < mapList.size(); i++) {
                returnMap.put(mapList.get(i).get(codeName), mapList.get(i).get(labelName));
            }
        }
        return returnMap;
    }

    /**
     * リストテーブルをマップテーブルに置き換える（コードに対して１件分のデータ）
     *
     * @param mapList 変換対象リストテーブル
     * @param codeName キー（リストテーブルにあるマップの値）
     * @param labelName 値（リストテーブルにあるマップの値）
     * @return マップテーブル
     */
    public static Map<String, Map<String, String>> convertListTableToMapTable(List<Map<String, String>> mapList,
                    String codeName) {
        Map<String, Map<String, String>> returnMap = null;
        if (mapList != null) {
            returnMap = new HashMap<>();
            for (int i = 0; i < mapList.size(); i++) {
                returnMap.put(mapList.get(i).get(codeName), mapList.get(i));
            }
        }
        return returnMap;
    }

    /**
     * 配列要素トリム処理
     *
     * @param ary 対象配列
     * @param allspace 全角スペーストリム可否フラグ
     * @return トリム後配列
     */
    public static String[] trimElements(String[] array, boolean allspace) {
        String[] returnArray = array.clone();
        if (allspace) {
            for (int i = 0; i < returnArray.length; i++) {
                returnArray[i] = ComUtils.allTrim(returnArray[i]);
            }
        } else {
            for (int i = 0; i < returnArray.length; i++) {
                returnArray[i] = returnArray[i].trim();
            }
        }
        return returnArray;
    }

    /**
     * <pre>
     * エンコードを行う。
     *
     * 半角スペース文字は「+」に変換されるため、Javascriptでのエンコード文字「%20」とは異なる。
     * よって、第二引数でJavascriptでのエンコード文字に合わせるかどうかを指定できる。
     * </pre>
     *
     * @param s エンコード対象文字列
     * @param fromHalfSpaceToCode 半角スペースのエンコード文字をJavascriptに合わせるか
     * @return エンコード変換された文字列
     */
    public static String encode(String s, boolean fromHalfSpaceToCode) {
        if (isEmpty(s)) {
            return s;
        }
        String encodeStr = null;
        try {
            encodeStr = URLEncoder.encode(s, "UTF-8");
            if (fromHalfSpaceToCode) {
                encodeStr = encodeStr.replace(Const.PLUS, "%20");
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return encodeStr;
    }

    /**
     * デコードを行う
     *
     * @param s デコード対象文字列
     * @return デコード変換された文字列
     */
    public static String decode(String s) {
        if (isEmpty(s)) {
            return s;
        }
        String decodeStr = null;
        try {
            decodeStr = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return decodeStr;
    }

    /**
     * 型
     */
    public enum Type {
        OTHER, NULL, STRING, CHAR, BIGDECIMAL, DOUBLE, BYTE, FLOAT, INTEGER, LONG, SHORT, DATE, BOOLEAN;
    }

    /**
     * 値を加算
     *
     * @param vals 数値群
     * @return 計算後値(String)
     */
    public static String decAdd(Object... vals) {
        BigDecimal rtnVal = toBigDecimal("0");
        for (Object val : vals) {
            BigDecimal num = toBigDecimal(val);
            rtnVal = rtnVal.add(num);
        }
        return toString(rtnVal);
    }

    /**
     * 値を積算
     *
     * @param vals 数値群
     * @return 計算後値(String)
     */
    public static String decMlt(Object... vals) {
        BigDecimal rtnVal = toBigDecimal("0");
        if (vals.length != 0) {
            rtnVal = toBigDecimal(vals[0]);
            for (int i = 1; i < vals.length; i++) {
                BigDecimal num = toBigDecimal(vals[i]);
                rtnVal = rtnVal.multiply(num);
            }
        }
        return toString(rtnVal);
    }

    /**
     * 値を割算
     *
     * @param scale 小数以下桁数
     * @param roundingMode RoundingMode
     * @param vals 数値群
     * @return 計算後値(String)
     */
    public static String decDiv(Integer scale, RoundingMode roundingMode, Object... vals) {
        BigDecimal rtnVal = toBigDecimal("0");
        if (vals.length != 0) {
            rtnVal = toBigDecimal(vals[0]);
            for (int i = 1; i < vals.length; i++) {
                BigDecimal num = toBigDecimal(vals[i]);
                rtnVal = rtnVal.divide(num, scale, roundingMode);
            }
        }
        return toString(rtnVal);
    }

    /**
     * 値を割算
     *
     * @param vals 数値群
     * @return 計算後値(String)
     */
    public static String decDiv(Object... vals) {
        return decDiv(10, RoundingMode.HALF_EVEN, vals);
    }

    /**
     * <pre>
     * BASEプロジェクトで保持するイメージファイルのパスを取得する
     * </pre>
     *
     * @param fileNm 拡張子を含むファイル名
     */
    public String getImageFilePath(String fileNm) {
        URL url = getClass().getClassLoader().getResource("static/images/" + fileNm);
        if (url == null) {
            return null;
        }
        return decode(url.getPath());
    }

    /**
     * ファイルの先頭に文字列を追加
     *
     * @param f
     * @param content
     * @return
     * @throws IOException
     */
    public static boolean write(File f, String content) throws IOException {
        File tmp = File.createTempFile("tmp", Const.CSV);
        try (FileOutputStream tmpOut = new FileOutputStream(tmp)) {
            try (FileInputStream tmpIn = new FileInputStream(tmp)) {
                try (RandomAccessFile raf = new RandomAccessFile(f, "rw")) {
                    byte[] buf = new byte[64];
                    Integer hasRead = 0;
                    while ((hasRead = raf.read(buf)) > 0) {
                        tmpOut.write(buf, 0, hasRead);
                    }
                    raf.seek(0L);
                    raf.write(content.getBytes());
                    while ((hasRead = tmpIn.read(buf)) > 0) {
                        raf.write(buf, 0, hasRead);
                    }
                }
            }
        }
        return true;
    }

    public static File csvCodeChange(File f) throws IOException {
        File file1 = File.createTempFile("temp", Const.CSV);
        String row;
        try (BufferedReader in =
                        new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            try (BufferedWriter out = new BufferedWriter(
                            new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1), "Shift_JIS")))) {
                while ((row = in.readLine()) != null) {
                    out.append(row).append("\r\n");
                }
            }
        }
        return file1;
    }
}
