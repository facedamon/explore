package org.facedamon.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.facedamon.api.UserService;
import org.facedamon.vo.UserVoForProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private UserService userService;

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<?> findAll() {
        return this.userService.findAll();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public UserVoForProduct findById(@PathVariable Integer userId) {
        return this.userService.findById(userId);
    }
}
