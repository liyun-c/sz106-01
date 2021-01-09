package com.itheima.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author liyun
 * @date 2021/1/8 17:01
 */

/**
 * 使用阿里巴巴的包，发布服务 interfaceClass指定服务的接口类
 * 如果不使用interfaceClass指定服务在有的dabble版本中  服务发布上去服务前缀名会不是我们的包名
 * 导致无法订阅
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 添加检查组
     *
     * @param checkgroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void add(CheckGroup checkgroup, Integer[] checkitemIds) {

        String code = checkgroup.getCode();
        if (StringUtils.isEmpty(code)) {
            throw new MyException("编码不能为空");
        }
        if (code.length() > 32) {
            throw new MyException("编码的长度不能超过32位");
        }
        // 先添加检查组基本信息 (表中字段信息) 并且添加后要查询自增长id
        // 方便下面添加检查组与检查项的关系在检查组与检查项关系表中
        checkGroupDao.add(checkgroup);
        // 获取检查组的id
        Integer checkgroupId = checkgroup.getId();
        // 遍历在页面选中的检查项id的数组
        if (null != checkitemIds) {
            for (Integer checkitemId : checkitemIds) {
                //- 添加检查组与检查项的关系
                checkGroupDao.addCheckGroupCheckItem(checkgroupId, checkitemId);
            }
        }
        // 添加事务控制 避免添加完检查组信息后 还未添加检查组与检查项之间的关系是
        // 服务器发生异常导致信息不全面  此时应该进行回滚
        // 使用注解方式 在方法上添加@Transactional
    }

    /**
     * 检查组的分页查询
     *
     * @param queryPageBean
     * @return PageResult
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
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
        //此处使用page集合封装CheckGroup较好
        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());

        PageResult<CheckGroup> pageResult = new PageResult<CheckGroup>(page.getTotal(), page.getResult());

        return pageResult;

    }

    /**
     * 通过id查询检查组基本信息
     *
     * @param id
     * @return CheckGroup
     */
    @Override
    public CheckGroup findById(int id) {

        return checkGroupDao.findById(id);
    }


    /**
     * 通过检查组id查询选中的检查项id
     *
     * @param id
     * @return List<Integer>
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {

        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 修改检查组  相当于添加
     *
     * @param checkgroup   检查组信息
     * @param checkitemIds 选中的检查项id数组
     */
    @Override
    @Transactional
    public void update(CheckGroup checkgroup, Integer[] checkitemIds) {
        // 更新检查组 更新检查组基本信息
        checkGroupDao.update(checkgroup);
        // 更新检查组与检查项之间的关系 但是我们此时无法得知它们之间已有的关系
        // 所以直接删除旧关系
        checkGroupDao.deleteCheckGroupCheckItem(checkgroup.getId());
        // 然后遍历在页面选中的检查项id的数组
        if (null != checkitemIds) {
            for (Integer checkitemId : checkitemIds) {
                // 添加检查组与检查项的关系(新关系) 直接调用原有方法
                checkGroupDao.addCheckGroupCheckItem(checkgroup.getId(), checkitemId);
            }
        }
        // 分步操作数据库 最好添加事务控制 有回滚功能
        // 添加事务控制


    }

    @Override
    @Transactional  // 事务控制
    public void deleteById(int id) {
        // 检查组分别于检查项和套餐有关系  但是检查组与检查项的关系是一对多 山吃检查组并不会对检查项造成影响
        // 而检查组与套餐的关系是多对一  如果此检查组与套餐有关系name删除检查组会造成套餐的数据丢失
        // 所以先通过检查组id查询是否被套餐使用了
        int count = checkGroupDao.findCountByCheckGroupId(id);
        // 使用了，抛出异常
        if(count > 0){
            throw new MyException("该检查组被套餐使用了，不能删除");
        }
        // 没使用，删除检查组与检查项的关系
        checkGroupDao.deleteCheckGroupCheckItem(id);
        // 删除检查组
        checkGroupDao.deleteById(id);
    }

    /**
     * 查询所有的检查组
     * @return List<CheckGroup>
     */
    @Override
    public List<CheckGroup> findAll() {

        return checkGroupDao.findAll();
    }
}