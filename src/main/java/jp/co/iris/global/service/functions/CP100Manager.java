package jp.co.iris.global.service.functions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.iris.global.bean.CP10001Bean;
import jp.co.iris.global.bean.CP10008Bean;
import jp.co.iris.global.bean.CP100AsBean;
import jp.co.iris.global.bean.CP100CsBean;
import jp.co.iris.global.mapper.CP100Mapper;

/**
 * ＣＰＲ到着確認
 */
@Component
public class CP100Manager {

    @Autowired
    CP100Mapper cp100Mapper;

    /**
     * CP到着データを取得する
     */
    public List<CP10001Bean> getCPDataList(String stNo) {
        return cp100Mapper.execSCP10001(stNo);
    }

    /**
     * 枚数１を取得する
     */
    public Integer countOutsiji(String stNo) {
        return cp100Mapper.execSCP10002(stNo);
    }

    /**
     * 枚数２を取得する
     */
    public Integer countHanso(String stNo) {
        return cp100Mapper.execSCP10003(stNo);
    }

    /**
     * ロケを取得する
     */
    public Integer getLocation(String okibaCd, String itemCd) {
        return cp100Mapper.execSCP10004(okibaCd, itemCd);
    }

    /**
     * IRIS 検品データ（ケース）を取得する
     */
    public List<CP100CsBean> getCsList(String ymd, String syoCd) {
        return cp100Mapper.execSCP10005(ymd, syoCd);
    }

    /**
     * IRIS 検品データ（アソート）を取得する
     */
    public List<CP100AsBean> getAsList(String ymd, String syoCd) {
        return cp100Mapper.execSCP10006(ymd, syoCd);
    }

    /**
     * AMAZON 検品データ（アマゾン）、PLAZA 検品データ（佐川）、PLAZA 検品データ（Ｌ1Ｍ）を取得する
     */
    public List<CP10008Bean> getAmazonPlazaList(String SHUKA_DATE, String SYOCD) {
        return cp100Mapper.execSCP10008(SHUKA_DATE, SYOCD);
    }
}
