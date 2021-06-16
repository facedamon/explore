package org.facedamon.api;

import org.facedamon.vo.UserVoForProduct;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author toplus
 */
@FeignClient("user-service")
public interface UserService {

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    List<UserVoForProduct> findAll();

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    UserVoForProduct findById(@PathVariable(value = "userId") Integer userId);
}
