package com.example.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author damon
 * @desc 自动配置类
 * @date 2021/6/9
 */
//web应用才生效
@ConditionalOnWebApplication
//让属性文件生效
@EnableConfigurationProperties(HelloProperties.class)
//声明是一个配置
@Configuration
public class HelloServiceAutoConfiguration {
    @Autowired
    private HelloProperties helloProperties;

    @Bean
    public HelloService helloService() {
        HelloService helloService = new HelloService();
        helloService.setHelloProperties(helloProperties);
        return helloService;
    }
}
