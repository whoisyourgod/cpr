package jp.co.iris.global.aop.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import jp.co.iris.global.constant.Const;
import jp.co.iris.global.message.ErrorMessage;
import jp.co.iris.global.utils.LogUtils;

/**
 * @author zhaoDeMing
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * error handler
     *
     * @param e RuntimeException
     * @return ErrorMessage
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage exceptionHandler(Exception e) {
        LogUtils.error(e);
        return new ErrorMessage(Const.COMMON_ERROR_CODE, Const.COMMEN_ERROR_EXCEPTION);
    }

    @ExceptionHandler(value = ModelViewException.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject(Const.EXCEPTION_STR, e);
        mav.addObject(Const.URL_STR, req.getRequestURL());
        mav.setViewName(Const.DEFAULT_ERROR_VIEW);
        return mav;
    }
}
