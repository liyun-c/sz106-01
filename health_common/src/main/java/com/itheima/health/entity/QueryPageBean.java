package com.itheima.health.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装分页查询是的查询条件
 *
 * @author liyun
 * @date 2021/1/6 16:48
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryPageBean implements Serializable {
    private Integer currentPage;//页码
    private Integer pageSize;//每页记录数
    private String queryString;//查询条件

    public QueryPageBean(Integer currentPage, Integer pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public QueryPageBean(Integer pageSize, String queryString) {
        this.pageSize = pageSize;
        this.queryString = queryString;
    }
}
