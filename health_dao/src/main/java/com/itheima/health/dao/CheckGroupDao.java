package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liyun
 * @date 2021/1/8 17:10
 */

public interface CheckGroupDao {

    /**
     * 添加检查组中的基本信息  (表中字段信息)
     * @param checkgroup
     */
    void add(CheckGroup checkgroup);

    /**
     * 在检查组与检查项的关系表中添加检查组与检查项之间的关系
     * 两个参数为相同类型  给参数命名
     * @param checkgroupId
     * @param checkitemId
     */
    void addCheckGroupCheckItem(@Param("checkgroupId")Integer checkgroupId,@Param("checkitemId")Integer checkitemId);

    /**
     * 检查组的分页查询 使用分页插件进行分页查询  但是插件无法使用条件搜寻
     * 需要自己添加
     * @param queryString
     * @return PageResult
     */
    Page<CheckGroup> findPage(String queryString);

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
     * 更新检查组基本信息(操作检查组表)
     * @param checkgroup
     */
    void update(CheckGroup checkgroup);

    /**
     * 在检查组与检查项的关系表中删除检查组与检查项之间的旧关系
     * @param id
     */
    void deleteCheckGroupCheckItem(Integer id);

    /**
     * 通过检查组id 统计查询检查组与套餐之间的关系
     * @param id
     * @return int
     */
    int findCountByCheckGroupId(int id);

    /**
     * 删除检查组
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询所有的检查组
     * @return List<CheckGroup>
     */
    List<CheckGroup> findAll();

}
