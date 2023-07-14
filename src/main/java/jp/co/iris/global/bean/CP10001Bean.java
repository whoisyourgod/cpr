package jp.co.iris.global.bean;

import lombok.Data;

/**
 * CPR到着データクラスです.
 */
@Data
public class CP10001Bean {

    /* シリアルNO */
    private String snNo;

    /* TOSTNO */
    private String tostNo;

    /* TO置場 */
    private String toOki;

    /* 置場名 */
    private String okiNm;

    /* 社員コード */
    private String empCd;

    /* 社員名 */
    private String empNm;

    /* 設定日時 */
    private String setDate;

    /* 状態コード */
    private String statusCd;

    /* JYT */
    private String jyt;

    /* 備考 */
    private String biko;

    /* 会社コード */
    private String comCd;

    /* COMM */
    private String comm;

    /* 得意先コード */
    private String tokuCd;

    /* 得意先名 */
    private String tokuNm;

    /* 品質異常NO */
    private String ijoNo;

    /* FROM棚番 */
    private String fromTana;

    /* 商品コード */
    private String shohinCd;

    /* JANコード */
    private Double janCd;

    /* 商品名 */
    private String shohinNm;

    /* ロット年 */
    private String lotYear;

    /* ロット種別 */
    private String lotKbn;

    /* ケース入数 */
    private String ksHaisu;

    /* ケース数 */
    private String ksSu;

    /* 数量 */
    private String suryo;

    /* 到着完了日時 */
    private String arrivalEndDate;

    /* 引当数量 */
    private String hikiateSu;

    /* ロケ */
    private Integer location;

    /* 枚数 */
    private Integer maisu;

}
