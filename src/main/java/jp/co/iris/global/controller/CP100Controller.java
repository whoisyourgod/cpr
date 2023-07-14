package jp.co.iris.global.controller;

import java.util.List;
import java.util.Map;
import jp.co.iris.global.aop.handler.ModelView;
import jp.co.iris.global.bean.CP10001Bean;
import jp.co.iris.global.bean.CP10008Bean;
import jp.co.iris.global.bean.CP100AsBindBean;
import jp.co.iris.global.bean.CP100CsBindBean;
import jp.co.iris.global.constant.Const;
import jp.co.iris.global.service.CP100Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

/**
 * ＣＰＲ到着確認
 */
@Controller
@RequestMapping(Const.M_CP100)
public class CP100Controller {

    @Autowired
    CP100Service cp100Service;

    /**
     * ＣＰＲ到着確認(CP100)画面の初期表示を行う．
     *
     * @param mav ModelAndView
     * @return 表示する画面のView
     */
    @GetMapping
    @ModelView
    public ModelAndView getViewsInfo(ModelAndView mav) {
        mav.addAllObjects(cp100Service.getViewsInfo());
        mav.setViewName(Const.H_CP100);

        return mav;
    }

    /**
     * CP到着データ検索
     *
     * @param stNo STNO
     * @return CP
     */
    @GetMapping("/cpSearch")
    @ResponseBody
    public List<CP10001Bean> cpSearch(@RequestParam(name = "STNO") String stNo) {

        List<CP10001Bean> cp100CPList = cp100Service.getCPDataList(stNo);

        return cp100CPList;
    }

    /**
     * IRIS 検品データ（ケース）検索
     *
     * @param ymd 年月日
     * @param syoCd 商品コード
     * @return CP
     */
    @GetMapping("/csSearch")
    @ResponseBody
    public List<CP100CsBindBean> csSearch(@RequestParam(name = "YMD") String ymd,
                    @RequestParam(name = "SYOCD") String syoCd) {

        List<CP100CsBindBean> csBindList = cp100Service.getCsList(ymd, syoCd);

        // List<CP100CsBean> cp100CsList = this.createCsData();

        return csBindList;
    }

    /**
     * IRIS 検品データ（アソート）検索
     *
     * @param ymd 年月日
     * @param syoCd 商品コード
     * @return CP
     */
    @GetMapping("/asSearch")
    @ResponseBody
    public List<CP100AsBindBean> asSearch(@RequestParam(name = "YMD") String ymd,
                    @RequestParam(name = "SYOCD") String syoCd) {
        List<CP100AsBindBean> asBindList = cp100Service.getAsList(ymd, syoCd);
        // List<CP100AsBean> cp100AsList = this.createAsData();

        return asBindList;
    }

    /**
     * AMAZON 検品データ（アマゾン）、PLAZA 検品データ（佐川）、PLAZA 検品データ（Ｌ1Ｍ）検索
     *
     * @param ymd 年月日
     * @param syoCd 商品コード
     * @return CP
     */
    @GetMapping("/azPlazaSearch")
    @ResponseBody
    public Map<String, List<CP10008Bean>> azSearch(@RequestParam(name = "YMD") String ymd,
                    @RequestParam(name = "SYOCD") String syoCd) {

        Map<String, List<CP10008Bean>> resultMap = cp100Service.getAmazonPlazaList(ymd, syoCd);

        return resultMap;
    }

    @GetMapping(value = Const.M_CP0001_AUTHENTICATION)
    @ModelView
    public ModelAndView authentication(@SessionAttribute(Const.H_MSG) String errMsg, ModelAndView mav) {
        mav.addObject(Const.H_MSG, errMsg);
        mav.addObject(Const.REDIRECT_URL, Const.LOGIN_URL);
        mav.setViewName(Const.H_CP0001);
        return mav;
    }
}
