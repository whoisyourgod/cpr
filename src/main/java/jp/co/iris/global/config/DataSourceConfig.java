package jp.co.iris.global.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import jp.co.iris.global.bean.CP10009Bean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * データベース
 * 
 */
@Configuration
public class DataSourceConfig {

    @Value("${datasource.jdbc-url}")
    private String jdbc_url;

    @Value("${datasource.username}")
    private String username;

    @Value("${datasource.password}")
    private String password;

    @Value("${datasource.driverClassName}")
    private String driverClassName;

    public static Map<Integer, Object> koujoServerMap = new HashMap<>();

    /**
     * データベースsvr015
     * 
     * @return
     */
    @Bean(name = "svr015")
    @ConfigurationProperties(prefix = "spring.datasource.svr015")
    DataSource dataSource015() {
        return DataSourceBuilder.create().build();
    }

    /**
     * ダイナミックデータベース
     * 
     * @return
     */
    @Primary
    @Bean(name = "dynamicDataSource")
    DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> dsMap = new HashMap<>();
        dynamicDataSource.setDefaultTargetDataSource(dataSource015());
        String sql = "{call SPDB.dbo.SCP10009()}";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource015());
        List<CP10009Bean> cp10009List = jdbcTemplate.query(sql, new RowMapper<CP10009Bean>() {
            @Override
            public CP10009Bean mapRow(ResultSet rs, int rowNum) throws SQLException {
                CP10009Bean info = new CP10009Bean();
                info.setAW_SVR_IP(rs.getString("AW_SVR_IP"));
                info.setAW_SVR_NM(rs.getString("AW_SVR_NM"));
                info.setKOUJO_CD(rs.getInt("KOUJO_CD"));
                info.setSVR_IP(rs.getString("SVR_IP"));
                info.setSVR_NM(rs.getString("SVR_NM"));
                return info;
            }
        });

        cp10009List.stream().forEach(a -> {
            koujoServerMap.put(a.getKOUJO_CD(), a);

            if (StringUtils.isNotBlank(a.getSVR_NM())) {
                dsMap.put(a.getSVR_NM(), DataSourceBuilder.create().driverClassName(driverClassName)
                                .url(jdbc_url + a.getSVR_NM() + ";").username(username).password(password).build());
            }
            if (StringUtils.isNotBlank(a.getAW_SVR_NM())) {
                dsMap.put(a.getAW_SVR_NM(), DataSourceBuilder.create().driverClassName(driverClassName)
                                .url(jdbc_url + a.getAW_SVR_NM() + ";").username(username).password(password).build());
            }
        });

        dsMap.put("svr015", dataSource015());
        dynamicDataSource.setTargetDataSources(dsMap);
        return dynamicDataSource;
    }

    @Bean
    PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}
