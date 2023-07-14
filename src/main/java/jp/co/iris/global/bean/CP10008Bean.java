package jp.co.iris.global.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * AMAZON、PLAZA検品データ結果定義クラスです.
 */
@Data
public class CP10008Bean {

    @JsonProperty("DATA_KBN")
    private String dataKbn;

    @JsonProperty("KOJOCD")
    private String kojoCd;

    @JsonProperty("UNSOCD")
    private String unsoCd;

    @JsonProperty("TODOKECD")
    private Integer todokeCd;

    @JsonProperty("CNLF")
    private Integer cnlf;

    @JsonProperty("ITEM_CD")
    private String itemCd;

    @JsonProperty("FLD_4")
    private String fld4;

}
