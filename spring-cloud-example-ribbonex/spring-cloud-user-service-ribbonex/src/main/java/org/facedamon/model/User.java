package org.facedamon.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author damon
 * @desc
 * @date 2021/6/10
 */
@Data
@Builder
public class User implements Serializable {
    private Integer id;
    private String name;
}
