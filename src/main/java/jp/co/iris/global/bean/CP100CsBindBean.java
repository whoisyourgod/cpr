package jp.co.iris.global.bean;

import lombok.Data;

/**
 * IRIS 検品データ（ケース）クラスです.
 */
@Data
public class CP100CsBindBean {

    /* 運送会社 */
    private String unsmei;

    /* 合計出荷数 */
    private Integer cs;

    /* 運送会社出荷数 */
    private Integer csUnsmei;

    /* 種まき */
    private Double taneNo1;

    /* 種まき出荷数 */
    private Integer csTane1;

    /* 種まき */
    private Double taneNo2;

    /* 種まき出荷数 */
    private Integer csTane2;

    /* 種まき */
    private Double taneNo3;

    /* 種まき出荷数 */
    private Integer csTane3;

    /* 種まき */
    private Double taneNo4;

    /* 種まき出荷数 */
    private Integer csTane4;

}
