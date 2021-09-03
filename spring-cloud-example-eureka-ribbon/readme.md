# 负载均衡概要

- 集中式负载均衡方案，在服务消费者和服务提供者之间有一个独立的负载均衡系统来承担负载均衡功能，</br>
  这种方案和之前的传统单体架构负载均衡实现原理一致

- 进程内负载均衡方案。该方案将负载均衡处理功能以库的方式整合到服务消费者应用中，因此该方案也被称为客户端负载均衡方案。</br>
  这个解决方案需要配合服务发现功能，在服务消费者启动时需要从服务发现服务器中获取所有服务注册信息，并定时同步这些注册信息。</br>
  当服务消费者需要访问某个服务时，内置的负载均衡器就会以某种负载均衡策略选择一个目标服务实例，</br>
  然后在本地所缓存的服务注册表信息中查询该目标服务的具体地址，最后向目标服务发起请求

- 主机独立负载均衡进程方案。该方案是针对第二种解决方案的一种折中处理方案，原理和第二种方案基本类似，</br>
  所不同之处是将负载均衡和服务发现功能从服务消费者的进程内移出来，变成同一个主机上的一个独立进程，</br>
  为该主机上的一个或者多个服务消费者提供负载均衡处理

# eureka配合ribbon使用步骤

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
    <dependency>
        <groupId>org.springframework.retry</groupId>
        <artifactId>spring-retry</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
    </dependency>
    ```
    2. 程序入口添加`@EnableDiscoveryClient`
    3. 配置文件见`application.yml`
    4. `java -jar Application.jar --server.port=2100`
    5. `java -jar Application.jar --server.port=2101`
    6. 在eureka网页查看是否成功注册

- ProductService 消费服务-》用户
    1. 添加依赖
    ```
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>
    ```
    2. 程序入口添加`@EnableDiscoveryClient`
    3. 给RestTemplate 添加`@LoadBalanced`
    4. 配置文件见`application.yml`
    5. 启动，在eureka网页查看是否成功注册
    6. 验证：`curl http://127.0.0.1:2200/products/1`
    7. 不断刷新连接，观察端口变化