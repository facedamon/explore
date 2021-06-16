package org.facedamon.api;

import org.facedamon.vo.UserVoForProduct;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author toplus
 */

public interface UserService {

    List<UserVoForProduct> findAll();

    UserVoForProduct findById(@PathVariable(value = "userId") Integer userId);
}
