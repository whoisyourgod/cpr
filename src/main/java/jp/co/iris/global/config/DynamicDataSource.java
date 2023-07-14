package jp.co.iris.global.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import jp.co.iris.global.utils.DataSourceUtil;

/**
 * ダイナミックデータベースクラス
 * 
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceUtil.getDB();
    }
}
