package jp.co.iris.global.constant;

/**
 * 共通定数クラス.
 */
public class Const {
    private Const() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ZERO = "0";

    // マッピング関連
    public static final String SL = "/";
    public static final String UN_BAR = "_";
    public static final String INI_H = "H"; // templateの頭文字
    public static final String FORWARD = "forward:";
    public static final String REDIRECT = "redirect:";

    /**
     * ブランク
     */
    public static final String DEFAULT_BLANK = "";

    /**
     * 0(ゼロ)
     */
    public static final Integer DEFAULT_ZERO = 0;
    public static final Integer DEFAULT_ERROR_NO = -1;
    public static final Integer DEFAULT_NEGATIVE_ONE = -1;

    // Template項目ID

    /**
     * 共通ヘッダメッセージ表示域
     */
    public static final String H_MSG = "hMsg";

    public static final String REDIRECT_URL = "redirectUrl";

    /**
     * PDF
     */
    public static final String PDF = ".pdf";

    /**
     * CSV
     */
    public static final String CSV = ".csv";

    /**
     * ポップアップ表示用
     */
    public static final String CONTENT_FORMAT = "attachment; filename=%s";

    /**
     * コンテンツ-配備
     */
    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    /**
     * コンテンツの種類
     */
    public static final String CONTENT_TYPE = "text/html;charset=UTF-8";

    // シンボル
    /**
     * \\
     */
    public static final String YEN = "\\";
    /**
     * -
     */
    public static final String HYFN = "-";
    /**
     * '/
     */
    public static final String SLSH = "/";
    /**
     * \"
     */
    public static final String DBQ = "\"";
    /**
     * '
     */
    public static final String SGQ = "'";
    /**
     * ,
     */
    public static final String COMMA = ",";
    /**
     * ,
     */
    public static final String COLON = ":";
    /**
     * ,
     */
    public static final String SEMICOLON = ";";
    /**
     * (
     */
    public static final String BRACKET1_S = "(";
    /**
     * )
     */
    public static final String BRACKET1_E = ")";
    /**
     * [
     */
    public static final String BRACKET2_S = "[";
    /**
     * ]
     */
    public static final String BRACKET2_E = "]";
    /**
     * <
     */
    public static final String BRACKET3_S = "<";
    /**
     * >
     */
    public static final String BRACKET3_E = ">";
    /**
     * \r\n
     */
    public static final String LN_SPRT = "\r\n";
    /**
     * \r
     */
    public static final String YEN_R = "\r";
    /**
     * \t
     */
    public static final String YEN_T = "\t";
    /**
     * \n
     */
    public static final String YEN_N = "\n";
    /**
     * (半角スペース)
     */
    public static final String HALF_SPACE = " ";
    /**
     * %
     */
    public static final String PERCENT = "%";
    /**
     * ?
     */
    public static final String QUESTION = "?";
    /**
     * =
     */
    public static final String EQUAL = "=";
    /**
     * _
     */
    public static final String UNDERBAR = "_";
    /**
     * .
     */
    public static final String DOT = ".";
    /**
     * +
     */
    public static final String PLUS = "+";
    /**
     * -
     */
    public static final String MINUS = "-";
    /**
     * $
     */
    public static final String DOLLAR = "$";
    /**
     * デリミタ「,」
     */
    public static final String DELIMITA = ",";
    /**
     * サブ画面
     */
    public static final String SUB_MENU = "01";

    /**
     * プログラムID
     */
    public static final String CP000 = "CP000"; // 共通
    public static final String CP100 = "CP100";

    /*** テンプレートID ***/

    public static final String H_CP100 = INI_H + CP100;
    public static final String H_CP0001 = INI_H + CP000 + "1";

    /*** URL ***/

    public static final String M_CP0001_AUTHENTICATION = SL + "authentication";
    public static final String M_CP000 = SL + CP000;
    public static final String M_CP100 = SL + CP100;

    /*** Error ***/
    public static final String COMMON_ERROR_CODE = "10000";
    public static final String COMMEN_ERROR_EXCEPTION = "エラーが発生しました。システム部へ連絡してください。";

    /*** Common ***/
    public static final String TITLE = "Title";
    public static final String NOWTIME = "NowTime";
    public static final String NOWDATE = "NowDate";
    public static final String CID = "CID";
    public static final String OID = "OID";
    public static final String IP = "IP";
    public static final String EMP_CD = "EMP_CD";
    public static final String EMP_NM = "EMP_NM";
    public static final String LAB_NM = "LAB_NM";
    public static final String TORINM = "TORINM";
    public static final String BCDNM = "BCDNM";
    public static final String KMKCDNM = "KMKCDNM";
    public static final String EMPCDNM = "EMPCDNM";
    public static final String TEKICDNM = "TEKICDNM";
    public static final String GENZEICDNM = "GENZEICDNM";

    public static final String HEAD_DATA = "HEAD_DATA";

    public static final String KOUJO_SERVER_DATA = "KOUJO_SERVER_DATA";

    public static final String AUTHENTICATION_URL = "http://svr601/authentication.php";
    public static final String LOGIN_URL = "https://portal.irisohyama.co.jp/menu/login";

    public static final Integer BUNRUI_305 = 305;


    public static final String EXCEPTION_STR = "exception";
    public static final String URL_STR = "url";
    public static final String DEFAULT_ERROR_VIEW = "error";

}
