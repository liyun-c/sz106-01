package com.itheima.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * CheckItemService 操作检查项的服务实现类
 *
 * @author liyun
 * @date 2021/1/6 17:25
 */

/**
 * 使用阿里巴巴的包，发布服务 interfaceClass指定服务的接口类
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    /**
     * 依赖health_dao子模块工程  创建核心容器可以直接注入 di
     */
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 查询所有
     *
     * @return List<CheckItem>
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 添加检查项
     *
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 使用分页插件进行检查项的分页查询
     *
     * @param queryPageBean
     * @return PageResult<CheckItem>
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //使用分页插件进行分页查询
        // pageSize不能无限大？如果数值太大 会让数据库负担增加 直至BOOM
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 分页查询是可能会夹带条件查询
        //首先要判断是否有条件
        if (StringUtils.isNotEmpty(queryPageBean.getQueryString())) {
            // 如果有查询条件,就进行模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //page类型是继承arrayList 相当与也是一个集合 并且page<>类型是插件中的内置对象
        //此处使用page集合封装CheckItem较好
        Page<CheckItem> page = checkItemDao.findPage(queryPageBean.getQueryString());

        PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(), page.getResult());

        return pageResult;
    }

    /**
     * 通过id查询检查项信息  回显数据
     *
     * @param id
     * @return CheckItem
     */
    @Override
    public CheckItem findById(int id) {

        return checkItemDao.findById(id);
    }

    /**
     * 在回显数据表单中提交修改数据 修改当前检查项通过id修改
     *
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    /**
     * 通过id删除检查项 但是要检查此检查项是否有关联记录
     * 一般删除任何数据库中记录  都要观察该记录是否有关联数据
     *
     * @param id
     */
    @Override
    public void deleteById(int id) {
        //  查看当前检查项id是否被检查组使用  如果统计个数大于0 就表示该检查项被使用了，不能删除
        //  此时可以抛一个异常  而且此异常属于可预测异常  可以创建一个自定义异常抛出去
        int count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            throw new MyException("该检查项被使用了，不能删除!");
        }
        // 删除
        checkItemDao.deleteById(id);
    }
}

