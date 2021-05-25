package org.facedamon.stream;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author damon
 * @desc TODO
 * @date 2021/5/21
 */
public class Columns {
    public static void main(String[] args) {
        List<FlowTempColumns> columns = new ArrayList<>();

        columns.add(new FlowTempColumns("1", "a", "", "", ""));
        columns.add(new FlowTempColumns("2", "b", "", "", ""));
        columns.add(new FlowTempColumns("3", "c", "", "", ""));
        System.out.println(columns.stream().filter(c -> c.getColumnCode().equals("4")).map(c -> c.getColumnName()).collect(Collectors.joining()));
    }

    @Data
    @AllArgsConstructor
    private static class FlowTempColumns {
        //字段code
        private String columnCode;
        //字段名称
        private String columnName;
        //默认值
        private String columnDefault;
        //默认值code
        private String columnDefaultCode;
        //是否必填
        private String columnReq;
    }
}
