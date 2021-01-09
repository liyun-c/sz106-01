package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

/**
 * @author liyun
 * @date 2021/1/8 16:56
 */
public interface CheckGroupService {
    /**
     * 添加检查组
     * @param checkgroup
     * @param checkitemIds
     */
    void add(CheckGroup checkgroup, Integer[] checkitemIds);

    /**
     * 检查组的分页查询
     * @param queryPageBean
     * @return PageResult
     */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    /**
     * 通过id查询检查组
     * @param id
     * @return CheckGroup
     */
    CheckGroup findById(int id);

    /**
     * 通过检查组id查询选中的检查项id
     * @param id
     * @return  List<Integer>
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 修改检查组  相当于添加
     * @param checkgroup 检查组信息
     * @param checkitemIds 选中的检查项id数组
     */
    void update(CheckGroup checkgroup, Integer[] checkitemIds);

    /**
     * 通过id删除检查组
     * @param id
     * @return Result
     */
    void deleteById(int id);

    /**
     * 查询所有的检查组
     * @return List<CheckGroup>
     */
    List<CheckGroup> findAll();

}
