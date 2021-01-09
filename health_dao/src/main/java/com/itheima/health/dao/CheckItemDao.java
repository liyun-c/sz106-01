package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * CheckItemDao 操作检查项数据的Dao
 *
 * @author liyun
 * @date 2021/1/6 17:30
 */
public interface CheckItemDao {

    /**
     * 查询所有检查项
     *
     * @return List<CheckItem>
     */
    List<CheckItem> findAll();

    /**
     * 添加检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 检查项的分页查询
     * @param queryString
     * @return  Page<CheckItem>
     */
    Page<CheckItem> findPage(String queryString);

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
     * 根据传过来的检查项id 在检查项与检查组的关系表中查询该id是否关联检查组
     *
     * 即统计该检查项id在表中的记录条数
     * @param id
     * @return
     */
    int findCountByCheckItemId(int id);

    /**
     * 删除检查项
     * @param id
     */
    void deleteById(int id);
}
