package jp.co.iris.global.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.iris.global.bean.CP10001Bean;
import jp.co.iris.global.bean.CP10008Bean;
import jp.co.iris.global.bean.CP10009Bean;
import jp.co.iris.global.bean.CP100AsBean;
import jp.co.iris.global.bean.CP100AsBindBean;
import jp.co.iris.global.bean.CP100CsBean;
import jp.co.iris.global.bean.CP100CsBindBean;
import jp.co.iris.global.constant.Const;
import jp.co.iris.global.service.functions.CP100Manager;
import jp.co.iris.global.utils.DataSourceUtil;

/**
 * ＣＰＲ到着確認
 */
@Service
public class CP100Service extends BaseService {

    @Autowired
    CP100Manager cp100Manager;

    @Autowired
    HttpSession httpSession;

    private CP10009Bean koujoServerBean;

    /**
     * 画面Infoを取得する
     *
     */
    public Map<String, Object> getViewsInfo() {
        Map<String, Object> map = super.getViewsInfo();
        map.put(Const.BCDNM, "BCDNM");
        map.put(Const.KMKCDNM, "KMKCDNM");
        map.put(Const.EMPCDNM, "EMPCDNM");
        map.put(Const.TEKICDNM, "TEKICDNM");
        map.put(Const.GENZEICDNM, "GENZEICDNM");
        return map;
    }

    /**
     * CP到着データを取得する
     */
    public List<CP10001Bean> getCPDataList(String stNo) {

        koujoServerBean = (CP10009Bean) httpSession.getAttribute(Const.KOUJO_SERVER_DATA);

        DataSourceUtil.setDB(koujoServerBean.getAW_SVR_NM());

        List<CP10001Bean> cp10001List = cp100Manager.getCPDataList(stNo);
        Integer mai1 = cp100Manager.countOutsiji(stNo);
        Integer mai2 = cp100Manager.countHanso(stNo);

        Integer maisu = mai1 + mai2;

        DataSourceUtil.setDB(koujoServerBean.getSVR_NM());
        for (CP10001Bean cp10001Bean : cp10001List) {
            Integer location = cp100Manager.getLocation(cp10001Bean.getToOki(), cp10001Bean.getShohinCd());
            cp10001Bean.setLocation(location);
            cp10001Bean.setMaisu(maisu);
        }

        return cp10001List;
    }

    /**
     * IRIS 検品データ（ケース）を取得する
     */
    public List<CP100CsBindBean> getCsList(String ymd, String syoCd) {

        koujoServerBean = (CP10009Bean) httpSession.getAttribute(Const.KOUJO_SERVER_DATA);

        DataSourceUtil.setDB(koujoServerBean.getSVR_NM());

        List<CP100CsBean> cp100CsList = cp100Manager.getCsList(ymd, syoCd);

        Map<String, List<CP100CsBean>> csMap =
                        cp100CsList.stream().collect(Collectors.groupingBy(CP100CsBean::getUnsmei));

        List<CP100CsBindBean> csBindList = new ArrayList<>();

        csMap.forEach((key, value) -> {
            CP100CsBindBean csBindBean = new CP100CsBindBean();
            csBindBean.setUnsmei(value.get(0).getUnsmei());
            csBindBean.setCsUnsmei(value.get(0).getCsUnsmei());
            csBindBean.setCs(value.get(0).getCs());

            csBindBean.setTaneNo1(value.get(0).getTaneNo());
            csBindBean.setCsTane1(value.get(0).getCsTane());

            if (value.size() > 1) {
                csBindBean.setTaneNo2(value.get(1).getTaneNo());
                csBindBean.setCsTane2(value.get(1).getCsTane());
            }

            if (value.size() > 2) {
                csBindBean.setTaneNo3(value.get(2).getTaneNo());
                csBindBean.setCsTane3(value.get(2).getCsTane());
            }

            if (value.size() > 3) {
                csBindBean.setTaneNo4(value.get(3).getTaneNo());
                csBindBean.setCsTane4(value.get(3).getCsTane());
            }

            csBindList.add(csBindBean);
            System.out.println(key);
            System.out.println(value);
        });

        return csBindList.stream().sorted(Comparator.comparing(CP100CsBindBean::getUnsmei))
                        .collect(Collectors.toList());
    }

    /**
     * IRIS 検品データ（アソート）を取得する
     */
    public List<CP100AsBindBean> getAsList(String ymd, String syoCd) {

        koujoServerBean = (CP10009Bean) httpSession.getAttribute(Const.KOUJO_SERVER_DATA);

        DataSourceUtil.setDB(koujoServerBean.getSVR_NM());

        List<CP100AsBean> cp100AsList = cp100Manager.getAsList(ymd, syoCd);

        Map<String, List<CP100AsBean>> asMap =
                        cp100AsList.stream().collect(Collectors.groupingBy(CP100AsBean::getUnsmei));

        List<CP100AsBindBean> asBindList = new ArrayList<>();

        asMap.forEach((key, value) -> {
            CP100AsBindBean asBindBean = new CP100AsBindBean();
            asBindBean.setUnsmei(value.get(0).getUnsmei());
            asBindBean.setCsUnsmei(value.get(0).getCsUnsmei());
            asBindBean.setCs(value.get(0).getCs());

            asBindBean.setTaneNo1(value.get(0).getTaneNo());
            asBindBean.setCsTane1(value.get(0).getCsTane());

            if (value.size() > 1) {
                asBindBean.setTaneNo2(value.get(1).getTaneNo());
                asBindBean.setCsTane2(value.get(1).getCsTane());
            }

            if (value.size() > 2) {
                asBindBean.setTaneNo3(value.get(2).getTaneNo());
                asBindBean.setCsTane3(value.get(2).getCsTane());
            }

            if (value.size() > 3) {
                asBindBean.setTaneNo4(value.get(3).getTaneNo());
                asBindBean.setCsTane4(value.get(3).getCsTane());
            }

            asBindList.add(asBindBean);
        });

        return asBindList.stream().sorted(Comparator.comparing(CP100AsBindBean::getUnsmei))
                        .collect(Collectors.toList());
    }

    /**
     * AMAZON 検品データ（アマゾン）、PLAZA 検品データ（佐川）、PLAZA 検品データ（Ｌ1Ｍ）を取得する
     */
    public Map<String, List<CP10008Bean>> getAmazonPlazaList(String SHUKA_DATE, String SYOCD) {

        koujoServerBean = (CP10009Bean) httpSession.getAttribute(Const.KOUJO_SERVER_DATA);

        DataSourceUtil.setDB(koujoServerBean.getAW_SVR_NM());

        List<CP10008Bean> cp10008List = cp100Manager.getAmazonPlazaList(SHUKA_DATE, SYOCD);

        Map<String, List<CP10008Bean>> resultMap = new HashMap<>();

        List<CP10008Bean> resultAz = cp10008List.stream().filter(a -> StringUtils.equals(a.getDataKbn(), "AMAZON"))
                        .collect(Collectors.toList());

        List<CP10008Bean> resultPlaza1 = cp10008List.stream().filter(a -> StringUtils.equals(a.getDataKbn(), "PLAZA1"))
                        .collect(Collectors.toList());

        List<CP10008Bean> resultPlaza2 = cp10008List.stream().filter(a -> StringUtils.equals(a.getDataKbn(), "PLAZA2"))
                        .collect(Collectors.toList());

        resultMap.put("AMAZON", resultAz);
        resultMap.put("PLAZA1", resultPlaza1);
        resultMap.put("PLAZA2", resultPlaza2);
        return resultMap;
    }

}
