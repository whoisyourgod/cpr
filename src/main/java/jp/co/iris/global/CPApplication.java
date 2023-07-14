package jp.co.iris.global;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("jp.co.iris.global")
@MapperScan("jp.co.iris.global.mapper")
public class CPApplication {

    public static void main(String[] args) {
        SpringApplication.run(CPApplication.class, args);
    }

}
