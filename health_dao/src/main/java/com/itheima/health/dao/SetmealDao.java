package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liyun
 * @date 2021/1/8 19:27
 */
public interface SetmealDao {

    /**
     * 添加套餐基本信息
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 添加套餐与检查组之间的关系  两参数数据类型相同 取别名
     * @param setmealId
     * @param checkgroupId
     */
    void addSetmealCheckGroup(@Param("setmealId")Integer setmealId,@Param("checkgroupId")Integer checkgroupId);

    /**
     * 使用分页插件进行分页条件查询
     * 插件无法进行条件查询 自己手动添加
     * @param queryString
     * @return  PageResult<Setmeal>
     */
    Page<Setmeal> findPage(String queryString);

    /**
     * 通过id查询套餐  回显套餐基本信息 操作套餐表
     * @param id
     * @return Setmeal
     */
    Setmeal findById(int id);

    /**
     * 查询选中的检查组id集合  即套餐与检查组之间的关系 操作套餐与检查组之间的关系表
     * @param id
     * @return  List<Integer>
     */
    List<Integer> findCheckGroupIdsBySetmealId(int id);

    /**
     * 更新套餐基本信息  操作套餐表
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 删除套餐与检查组的旧关系 通过该套餐id操作套餐与检查组之间的关系表
     * @param id
     */
    void deleteSetmealCheckGroup(Integer id);

    /**
     * 检查套餐与订单是否存在关联  即统计两者的关系表通过套餐id
     * @param id
     * @return
     */
    int findCountBySetmealId(int id);

    /**
     * 通过套餐id删除套餐
     * @param id
     */
    void deleteById(int id);

    /**
     * 找到套餐表中的所有图片信息
     * @return List<String>
     */
    List<String> findImgs();

}
