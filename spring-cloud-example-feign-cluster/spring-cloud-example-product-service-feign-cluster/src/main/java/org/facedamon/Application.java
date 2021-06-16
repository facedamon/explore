package org.facedamon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author damon
 * @desc
 * @date 2021/6/10
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"org.facedamon.api"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
