package jp.co.iris.global.filters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jp.co.iris.global.aop.handler.ModelView;
import jp.co.iris.global.bean.CP00001Bean;
import jp.co.iris.global.bean.CP00002Bean;
import jp.co.iris.global.bean.CP00003Bean;
import jp.co.iris.global.config.DataSourceConfig;
import jp.co.iris.global.constant.Const;
import jp.co.iris.global.utils.ComUtils;
import jp.co.iris.global.utils.HttpUtils;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    HttpSession httpSession;

    @Override
    @ModelView
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                    throws IOException {
        System.out.println("this");
        String servletPath = request.getServletPath();
        String pageId = servletPath.split("/")[1];
        String systemId = ComUtils.getSystemId(pageId);
        String ip = ComUtils.getIp();

        CP00002Bean cp00002Bean = new CP00002Bean();
        cp00002Bean.setIp(ip);
        cp00002Bean.setSystem_id(systemId);
        CP00003Bean cp00003Bean = HttpUtils.post(Const.AUTHENTICATION_URL, cp00002Bean, CP00003Bean.class);
        if (!cp00003Bean.getResult()) {
            String noAuthenticationUrl = request.getContextPath() + Const.M_CP000 + Const.M_CP0001_AUTHENTICATION;
            httpSession.setAttribute(Const.H_MSG, cp00003Bean.getError());
            response.setStatus(HttpStatus.FOUND.value());
            response.sendRedirect(noAuthenticationUrl);
            return false;
        }

        CP00001Bean cp00001Bean = new CP00001Bean();
        cp00001Bean.setEMP_CD(cp00003Bean.getEmp_cd());
        cp00001Bean.setEMP_NM(cp00003Bean.getEmp_nm());
        cp00001Bean.setCID(cp00003Bean.getC_id());
        httpSession.setAttribute(Const.HEAD_DATA, cp00001Bean);

        httpSession.setAttribute(Const.KOUJO_SERVER_DATA,
                        DataSourceConfig.koujoServerMap.get(cp00003Bean.getFactory_cd()));

        httpSession.setAttribute(Const.HEAD_DATA, cp00001Bean);
        return true;
    }
}
