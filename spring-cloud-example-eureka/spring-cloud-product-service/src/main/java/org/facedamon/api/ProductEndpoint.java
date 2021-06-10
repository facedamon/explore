package org.facedamon.api;

import lombok.extern.slf4j.Slf4j;
import org.facedamon.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public List<UserVo> findUser(@PathVariable Integer userId) {
        return this.restTemplate
                .getForEntity(
                        "http://user-service/users/{id}",
                        List.class,
                        userId)
                .getBody();
    }
}
