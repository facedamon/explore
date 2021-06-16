package org.facedamon.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * @author damon
 * @desc 声明UserService的Ribbon客户端使用UserRibbonConfiguration类进行配置
 * @date 2021/6/16
 */
@Configuration
@RibbonClient(name = "user-service", configuration = UserRibbonConfiguration.class)
public class RibbonConfiguration {
}
