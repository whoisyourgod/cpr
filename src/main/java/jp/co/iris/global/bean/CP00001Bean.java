package jp.co.iris.global.bean;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * ヘッダー項目定義クラスです.
 */

@Data
public class CP00001Bean {

    /* 1 IP */
    @JsonProperty("IP")
    private String IP;

    /* 2 EMP_CD */
    @JsonProperty("EMP_CD")
    private Integer EMP_CD;

    /* 3 EMP_NM */
    @JsonProperty("EMP_NM")
    private String EMP_NM;

    /* 4 LAB_NM */
    @JsonProperty("LAB_NM")
    private String LAB_NM;

    /* 5 CID */
    @JsonProperty("CID")
    private String CID;

}
