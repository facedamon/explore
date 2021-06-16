package org.facedamon.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.facedamon.vo.UserVoForProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

/**
 * @author damon
 * @desc
 * @date 2021/6/10
 */
@RestController
@RequestMapping("/products")
@Slf4j
public class ProductEndpoint {

    @Autowired
    @Qualifier(value = "restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier(value = "lbcRestTemplate")
    private RestTemplate lbcRestTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public UserVoForProduct findById(@PathVariable Integer userId) {
        UserVoForProduct vo = this.restTemplate
                .getForEntity("http://user-service/users/{userId}", UserVoForProduct.class, userId)
                .getBody();
        if (!Objects.isNull(vo)) {
            log.info("i came from server: {}", vo.getUserServicePort());
        }
        return vo;
    }

    @RequestMapping(value = "/findByIdEx/{userId}", method = RequestMethod.GET)
    public UserVoForProduct findByIdEx(@PathVariable Integer userId) {
        ServiceInstance instance = this.loadBalancerClient.choose("user-service");
        URI userUri = URI.create(String.format("http://%s:%s/users/{userId}", instance.getHost(), instance.getPort()));
        log.info("Target server uri = {}. ", userUri.toString());
        UserVoForProduct vo = this.lbcRestTemplate.getForEntity(userUri.toString(), UserVoForProduct.class, userId).getBody();
        if (!Objects.isNull(vo)) {
            log.info("i came from server : {}", vo.getUserServicePort());
        }
        return vo;
    }
}
