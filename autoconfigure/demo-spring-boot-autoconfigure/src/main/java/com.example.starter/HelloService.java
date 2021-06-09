package com.example.starter;

/**
 * @author damon
 * @desc 自动处理类
 * @date 2021/6/9
 */
public class HelloService {

    HelloProperties helloProperties;

    public HelloProperties getHelloProperties() {
        return helloProperties;
    }

    public void setHelloProperties(HelloProperties helloProperties) {
        this.helloProperties = helloProperties;
    }

    public String sayHello(String name) {
        return "Hello" + name + "," + helloProperties.getSuffix();
    }
}
