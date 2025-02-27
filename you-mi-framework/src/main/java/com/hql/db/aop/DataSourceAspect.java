package com.hql.db.aop;

import com.hql.db.aop.anno.DataSourceType;
import com.hql.db.config.DataSourceContextHolder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DataSourceAspect {

    @Before("@annotation(dataSourceType)")
    public void switchDataSource(DataSourceType dataSourceType) {
        DataSourceContextHolder.setDataSourceType(dataSourceType.value());
    }

    @After("@annotation(com.hql.db.aop.anno.DataSourceType)")
    public void clearDataSource() {
        DataSourceContextHolder.clearDataSourceType();
    }
}
