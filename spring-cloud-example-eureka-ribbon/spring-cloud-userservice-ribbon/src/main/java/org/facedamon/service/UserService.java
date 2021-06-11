package org.facedamon.service;

import org.facedamon.model.User;
import org.facedamon.vo.UserVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author damon
 * @desc
 * @date 2021/6/10
 */
@Service
public class UserService {

    @Value("${server.port}")
    protected Integer serverPort = 0;

    public List<UserVo> find() {
        return Stream.of(
                User.builder().id(1).name("张三").build(),
                User.builder().id(2).name("王五").build())
                .map(UserVo::new)
                .collect(Collectors.toList());
    }

    public UserVo findById(Integer userId) {
        Optional<UserVo> userVo = this.find().stream()
                .filter(v -> v.getId().compareTo(userId) == 0)
                .findFirst();
        userVo.ifPresent(u -> u.setUserServicePort(serverPort));
        return userVo.orElse(null);
    }
}
