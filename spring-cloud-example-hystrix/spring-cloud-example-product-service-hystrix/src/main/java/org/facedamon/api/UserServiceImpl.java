package org.facedamon.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.facedamon.vo.UserVoForProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author damon
 * @desc 用户服务
 * @date 2021/6/16
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @HystrixCommand(fallbackMethod = "findAllFallback")
    public List findAll() {
        return this.restTemplate.getForObject("http://user-service/users/", List.class);
    }

    @Override
    @HystrixCommand(fallbackMethod = "loadFallback")
    public UserVoForProduct findById(Integer userId) {
        return this.restTemplate
                .getForEntity("http://user-service/users/{userId}", UserVoForProduct.class, userId)
                .getBody();
    }

    protected List<UserVoForProduct> findAllFallback() {
        List<UserVoForProduct> vos = new ArrayList<>();
        vos.add(new UserVoForProduct(1, "zhangSan_static", 2100));
        vos.add(new UserVoForProduct(2, "lisi_static", 2100));
        vos.add(new UserVoForProduct(3, "wangwu_static", 2200));
        vos.add(new UserVoForProduct(4, "yanxiaoliu_static", 2200));
        return vos;
    }

    protected UserVoForProduct loadFallback(Integer userId) {
        return new UserVoForProduct(userId, "Anonymous", 2100);
    }
}
