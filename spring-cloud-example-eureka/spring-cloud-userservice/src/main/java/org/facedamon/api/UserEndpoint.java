package org.facedamon.api;

import org.facedamon.service.UserService;
import org.facedamon.vo.UserVo;
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
@RequestMapping("/users/")
public class UserEndpoint {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public List<UserVo> find(@PathVariable Integer userId) {
        return this.userService.find();
    }
}
