package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * CheckItemService 操作检查项服务的接口
 *
 * @author liyun
 * @date 2021/1/6 17:28
 */

public interface CheckItemService {
    /**
     * 查询所有
     *
     * @return List<CheckItem>
     */
    List<CheckItem> findAll();

    /**
     * 添加检查项
     *
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 检查项的分页查询
     *
     * @param queryPageBean
     * @return PageResult<CheckItem>
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    /**
     * 通过id查询检查项信息  回显数据
     *
     * @param id
     * @return CheckItem
     */
    CheckItem findById(int id);

    /**
     * 在回显数据表单中提交修改数据 修改当前检查项通过id修改
     *
     * @param checkItem
     */
    void update(CheckItem checkItem);

    /**
     * 通过id删除检查项
     *
     * @param id
     */
    void deleteById(int id);
}