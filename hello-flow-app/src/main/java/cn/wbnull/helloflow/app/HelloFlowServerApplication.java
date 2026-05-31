package cn.wbnull.helloflow.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * HelloFlow启动类
 *
 * @author null
 * @date 2026-05-26
 */
@SpringBootApplication
@MapperScan("cn.wbnull.helloflow.data.mapper")
@ComponentScan(basePackages = {
        "cn.wbnull.helloflow.app",
        "cn.wbnull.helloflow.data",
        "cn.wbnull.helloflow.security",
        "cn.wbnull.helloflow.common"})
public class HelloFlowServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloFlowServerApplication.class, args);
    }
}
