package jp.co.iris.global.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jp.co.iris.global.bean.CP00001Bean;
import jp.co.iris.global.constant.Const;
import jp.co.iris.global.utils.ComUtils;
import jp.co.iris.global.utils.DateUtils;

@Service
public class BaseService {

    /**
     * 画面Infoを取得する
     *
     * @return
     */
    public Map<String, Object> getViewsInfo() {
        Map<String, Object> map = new HashMap<>();
        CP00001Bean headdata = ComUtils.getHeadData();
        String ipAddr = ComUtils.getIp();
        map.put(Const.IP, ipAddr);
        map.put(Const.EMP_CD, headdata.getEMP_CD());
        map.put(Const.EMP_NM, headdata.getEMP_NM());
        map.put(Const.LAB_NM, headdata.getLAB_NM());
        map.put(Const.OID, headdata.getEMP_CD());
        map.put(Const.CID, headdata.getCID());
        return map;
    }

    /**
     * システム時間取得
     *
     * @param
     * @return
     */
    public String getNowTime() {
        String nowTime = DateUtils.getSysDate(DateUtils.FRM_YMDHMS);
        String ymdhms = DateUtils.FRM_YMD_SL + Const.HALF_SPACE + "HH:mm:ss"; // yyyy/MM/dd HH:mm:ss
        return DateUtils.toDateText(DateUtils.toDate(nowTime, DateUtils.FRM_YMDHMS), ymdhms);
    }

    /**
     * システム日付取得
     *
     * @param
     * @return
     */
    public String getNowDate() {
        String nowTime = DateUtils.getSysDate(DateUtils.FRM_YMD);
        return DateUtils.toDateText(DateUtils.toDate(nowTime, DateUtils.FRM_YMD), DateUtils.FRM_YMD_SL);
    }
}
