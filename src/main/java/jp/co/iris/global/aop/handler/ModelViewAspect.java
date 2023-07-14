package jp.co.iris.global.aop.handler;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ModelViewAspect {
    @Pointcut("@annotation(jp.co.iris.global.aop.handler.ModelView)")
    public void pointcut() {

    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void afterThrowable(Throwable e) {
        if (e instanceof Exception) {
            throw ModelViewException.transfer((Exception) e);
        }
    }
}
