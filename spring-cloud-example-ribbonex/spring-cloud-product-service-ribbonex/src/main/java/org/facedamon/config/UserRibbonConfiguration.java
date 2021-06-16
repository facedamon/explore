package org.facedamon.config;

import com.netflix.loadbalancer.*;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author damon
 * @desc 手动指定listOfServers
 * @date 2021/6/16
 */
@Configuration
public class UserRibbonConfiguration {

    /**
     * @author damon
     * @desc 构建固定的服务器列表
     * @date 13:54 2021/6/16
     **/
    @Bean
    public ServerList<Server> ribbonServerList() {
        List<Server> list = new ArrayList<>();
        list.add(new Server("http://127.0.0.1:2100"));
        list.add(new Server("http://127.0.0.1:2110"));
        return new StaticServerList<>(list.toArray(new Server[0]));
    }

    /**
     * @author damon
     * @desc 将服务器存活探测策略更改为通过URL来判定
     * @date 13:53 2021/6/16
     **/
    @Bean
    public IPing ribbonPing() {
        return new PingUrl(false, "/cs/hostRunning");
    }

    /**
     * @author damon
     * @desc 负载均衡策略定义未区域感知策略
     * @date 13:53 2021/6/16
     **/
    @Bean
    public IRule ribbonRule() {
        return new ZoneAvoidanceRule();
    }
}
