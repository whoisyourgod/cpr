package jp.co.iris.global.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * データソースクラスです.
 */

@Data
public class CP10009Bean {

    /* 工場コード */
    @JsonProperty("KOUJO_CD")
    private Integer KOUJO_CD;

    /* サーバー名 */
    @JsonProperty("SVR_NM")
    private String SVR_NM;

    /* サーバーアドレス */
    @JsonProperty("SVR_IP")
    private String SVR_IP;

    /* サーバー名 */
    @JsonProperty("AW_SVR_NM")
    private String AW_SVR_NM;

    /* サーバーアドレス */
    @JsonProperty("AW_SVR_IP")
    private String AW_SVR_IP;

}
