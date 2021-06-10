package org.facedamon.vo;

import lombok.Data;
import org.facedamon.model.User;

import java.io.Serializable;

/**
 * @author damon
 * @desc
 * @date 2021/6/10
 */
@Data
public class UserVo implements Serializable {
    private Integer id;
    private String name;

    public UserVo(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
