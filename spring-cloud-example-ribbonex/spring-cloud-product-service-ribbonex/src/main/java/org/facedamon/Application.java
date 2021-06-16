package org.facedamon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * @author damon
 * @desc
 * @date 2021/6/10
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Application {

    @Bean(value = "restTemplate")
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Primary
    @Bean(value = "lbcRestTemplate")
    RestTemplate lbcTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
