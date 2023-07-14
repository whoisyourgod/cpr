package jp.co.iris.global.bean;

import lombok.Data;

/**
 * IRIS 検品データ（ケース）クラスです.
 */
@Data
public class CP100CsBean {

    /* 合計出荷数 */
    private Integer cs;

    /* 運送会社 */
    private String unsmei;

    /* 運送会社出荷数 */
    private Integer csUnsmei;

    /* 種まき */
    private Double taneNo;

    /* 種まき出荷数 */
    private Integer csTane;

}
