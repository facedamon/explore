# 分布式概要

在分布式系统领域有个CAP定理（CAP theorem），又被称为布鲁尔定理（Brewer's theorem），</br>
它指出对于一个分布式计算系统来说，不可能同时满足以下3点。

1. 一致性（Consistency）：同一个数据在集群中的所有节点，同一时刻是否都是同样的值。
2. 可用性（Availability）：集群中一部分节点故障后，集群整体是否还能处理客户端的请求。
3. 分区容忍性（Partition tolerance）：是否允许数据的分区，数据分区的意思是指是否允许集群中的节点之间无法通信。

# eureka分布式使用步骤

&emsp;&emsp;eureka采用AP原则，也就是宁可保留错误的服务注册信息，也不盲目注销任何可能健康的服务实例。
&emsp;&emsp;为了能够让Eureka服务高可用，必须让Eureka服务器之间能够互相复制、同步所注册服务的实例信息

- eureka配置中心
    1. 添加依赖
    ```
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka-server</artifactId>
    </dependency>
    //引入该pom，则说明eureka需要登录，登录信息在application.yml中配置
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    ```
    2. 程序入口添加`@EnableEurekaServer`
    3. 配置ip node
       ```
        127.0.0.1 spdeer1
        127.0.0.1 sdpeer2
        127.0.0.1 sdpeer3
       ```
    3. 配置文件见`application-sdpeer1.yml application-sdpeer2.yml application-sdpeer3.yml`
    4. maven 打jar文件
    5. 启动
        ```
       java -jar -Dspring.profiles.active=sdpeer1 service-discovery-0.0.1-SNAPSHOT.jar
       java -jar -Dspring.profiles.active=sdpeer2 service-discovery-0.0.1-SNAPSHOT.jar
       java -jar -Dspring.profiles.active=sdpeer3 service-discovery-0.0.1-SNAPSHOT.jar
       ```

- UserService 用户服务
    1. 添加依赖
    ```
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
    ```
    2. 程序入口添加`@EnableDiscoveryClient`
    3. 配置文件见`application.yml`
    4. 启动，在eureka网页查看是否成功注册

- ProductService 消费服务-》用户
    1. 添加依赖
    ```
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-feign</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>
    ```
    2. 编写接口见UserService。使用`@FeignClient("user-service")`注明
    2. 程序入口添加`@EnableDiscoveryClient` `@EnableFeignClients(basePackages = {"org.facedamon.api"})`扫描接口包
    3. 配置文件见`application.yml`
    4. 启动，在eureka网页查看是否成功注册
    5. 验证：`curl http://127.0.0.1:2200/products/1`