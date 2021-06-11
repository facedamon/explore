package org.facedamon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author damon
 * @desc
 * @date 2021/6/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVoForProduct implements Serializable {
    private Integer id;
    private String name;
    private Integer userServicePort;
}
