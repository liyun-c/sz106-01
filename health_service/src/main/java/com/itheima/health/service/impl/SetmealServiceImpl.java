package com.itheima.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liyun
 * @date 2021/1/8 19:25
 */

/**
 * 使用阿里巴巴的包，发布服务 interfaceClass指定服务的接口类
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 添加套餐
     * @param setmeal 套餐基本信息
     * @param checkgroupIds 套餐与检查组的关系数组
     */
    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 分步添加  事务控制
        // 先添加套餐 操作套餐表 查询自增长id
        setmealDao.add(setmeal);
        // 获取套餐的id
        Integer setmealId = setmeal.getId();
        // 遍历checkgroupIds数组
        // 添加套餐与检查组的关系
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId,checkgroupId);
            }
        }
    }

    /**
     * 使用分页插件进行分页条件查询
     * @param queryPageBean
     * @return  PageResult<Setmeal>
     */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        //使用分页插件进行分页查询
        // pageSize不能无限大如果数值太大 会让数据库负担增加 直至BOOM
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 分页查询是可能会夹带条件查询
        //首先要判断是否有条件
        if (StringUtils.isNotEmpty(queryPageBean.getQueryString())) {
            // 如果有查询条件,就进行模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //page类型是继承arrayList 相当与也是一个集合 并且page<>类型是插件中的内置对象
        //此处使用page集合封装Setmeal较好
        Page<Setmeal> page = setmealDao.findPage(queryPageBean.getQueryString());

        PageResult<Setmeal> pageResult = new PageResult<Setmeal>(page.getTotal(), page.getResult());

        return pageResult;
    }

    /**
     * 通过id查询套餐  回显套餐基本信息 操作套餐表
     * @param id
     * @return Setmeal
     */
    @Override
    public Setmeal findById(int id) {

        return setmealDao.findById(id);
    }

    /**
     * 查询选中的检查组id集合  即套餐与检查组之间的关系 操作套餐与检查组之间的关系表
     * @param id
     * @return  List<Integer>
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(int id) {

        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐  相当与添加套餐  删除旧关系添加新关系
     * @param setmeal  套餐基本信息  套餐表
     * @param checkgroupIds 套餐与检查组之间的关系数组     套餐与检查组之间的关系表
     */
    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        // 分步修改  事务控制
        // 首先更新套餐
        setmealDao.update(setmeal);
        // 在删除旧关系
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        // 然后遍历关系数组  添加套餐与检查组的新关系
        // 使用上面添加时的方法
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
    }

    /**
     * 通过id删除套餐
     * 需要分析套餐是否跟其他表存在多对一的关系   即一方拥有多个套餐信息
     * 如果没有可以直接删除
     * @param id
     */
    @Override
    public void deleteById(int id) {
        // 检查表关系 发现订单与套餐存在一对多的关系  所以要先分析套餐是否被订单关联
        // 判断套餐是否被订单使用了
        // 统计关系表
        int count = setmealDao.findCountBySetmealId(id);
        // 大于0表示套餐被使用了,可以抛出自定义异常
        if(count > 0){
            throw new MyException("该套餐被订单使用了，不能删除");
        }
        // 没使用,就要先删除套餐与检查组的关系
        setmealDao.deleteSetmealCheckGroup(id);
        // 再删除套餐
        setmealDao.deleteById(id);
    }

    /**
     * 找到套餐表中的所有图片信息
     * @return List<String>
     */
    @Override
    public List<String> findImgs() {

        return setmealDao.findImgs();
    }
}
