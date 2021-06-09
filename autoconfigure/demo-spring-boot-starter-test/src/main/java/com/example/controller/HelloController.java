package com.example.controller;

import com.example.starter.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author damon
 * @desc
 * @date 2021/6/9
 */
@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/hello")
    public String sayhello(String name) {
        return helloService.sayHello(name);
    }
}
