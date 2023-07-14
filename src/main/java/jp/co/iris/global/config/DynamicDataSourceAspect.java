package jp.co.iris.global.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import jp.co.iris.global.utils.DataSourceUtil;

@Aspect
@Component
public class DynamicDataSourceAspect {

    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, TargetDataSource ds) throws Throwable {
        DataSourceUtil.setDB(ds.name());
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
        DataSourceUtil.clearDB();
    }
}
