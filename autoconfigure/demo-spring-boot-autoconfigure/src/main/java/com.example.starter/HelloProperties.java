package com.example.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author damon
 * @desc 自动绑定类
 * @date 2021/6/9
 */
@ConfigurationProperties(prefix = "demo.hello")
public class HelloProperties {

    private String suffix;

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
