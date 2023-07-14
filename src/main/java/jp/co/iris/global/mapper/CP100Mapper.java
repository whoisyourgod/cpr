package jp.co.iris.global.mapper;

import java.util.List;
import jp.co.iris.global.bean.CP10001Bean;
import jp.co.iris.global.bean.CP10008Bean;
import jp.co.iris.global.bean.CP100AsBean;
import jp.co.iris.global.bean.CP100CsBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CP100Mapper {

    /**
     * CP到着データ取得
     */
    List<CP10001Bean> execSCP10001(@Param("stNo") String stNo);

    /**
     * 枚数１取得
     */
    Integer execSCP10002(@Param("stNo") String stNo);

    /**
     * 枚数２取得
     */
    Integer execSCP10003(@Param("stNo") String stNo);

    /**
     * ロケ取得
     */
    Integer execSCP10004(@Param("okibaCd") String okibaCd, @Param("itemCd") String itemCd);

    /**
     * IRIS 検品データ（ケース）取得
     */
    List<CP100CsBean> execSCP10005(@Param("ymd") String ymd, @Param("syoCd") String syoCd);

    /**
     * IRIS 検品データ（アソート）取得
     */
    List<CP100AsBean> execSCP10006(@Param("ymd") String ymd, @Param("syoCd") String syoCd);

    /**
     * AMAZON 検品データ（アマゾン）、PLAZA 検品データ（佐川）、PLAZA 検品データ（Ｌ1Ｍ）取得
     */
    List<CP10008Bean> execSCP10008(@Param("SHUKA_DATE") String SHUKA_DATE, @Param("SYOCD") String SYOCD);

}
