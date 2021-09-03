# 使用eureka注册中心步骤

- eureka配置中心
    1. 添加依赖
    ```
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka-server</artifactId>
    </dependency>
    ```
    2. 程序入口添加`@EnableEurekaServer`
    3. 配置文件见`application.yml`
    4. 启动，网页输入`http://127.0.0.1:8761`

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
    ```
    2. 程序入口添加`@EnableDiscoveryClient`
    3. 配置文件见`application.yml`
    4. 启动，在eureka网页查看是否成功注册
    5. 验证：`curl http://127.0.0.1:2200/products/1`