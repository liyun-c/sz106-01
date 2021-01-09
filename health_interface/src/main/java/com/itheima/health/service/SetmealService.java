package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Setmeal;

import java.util.List;

/**
 * @author liyun
 * @date 2021/1/8 19:00
 */
public interface SetmealService {

    /**
     * 添加套餐
     * @param setmeal 套餐基本信息
     * @param checkgroupIds 套餐与检查组的关系数组
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 使用分页插件进行分页条件查询
     * @param queryPageBean
     * @return  PageResult<Setmeal>
     */
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);


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
     * 修改套餐  相当与添加套餐  删除旧关系添加新关系
     * @param setmeal  套餐基本信息  套餐表
     * @param checkgroupIds 套餐与检查组之间的关系数组     套餐与检查组之间的关系表
     */
    void update(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 通过id删除套餐
     * 需要分析套餐是否跟其他表存在多对一的关系   即一方拥有多个套餐信息
     * 如果没有可以直接删除
     * @param id
     */
    void deleteById(int id);

    /**
     * 找到套餐表中的所有图片信息
     * @return List<String>
     */
    List<String> findImgs();

}
