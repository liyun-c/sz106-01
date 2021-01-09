package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MyMessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liyun
 * @date 2021/1/6 17:41
 */

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    /**
     * 订阅检查项服务
     *
     * @Reference 一定得是阿里巴巴包的
     */
    @Reference
    private CheckItemService checkItemService;

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll() {

        // 调用服务查询
        List<CheckItem> list = checkItemService.findAll();
        // 封装到Result再返回
        return new Result(true, MyMessageConstant.QUERY_CHECKITEM_SUCCESS, list);
    }

    /**
     * 添加检查项
     *
     * @param checkItem
     * @return Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        // 调用服务添加
        checkItemService.add(checkItem);
        // 返回操作的结果
        return new Result(true, MyMessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 检查项的分页查询
     *
     * @param queryPageBean
     * @return Result
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        // 调用服务 分页查询
        PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);

        return new Result(true, MyMessageConstant.QUERY_CHECKITEM_SUCCESS, pageResult);
    }

    /**
     * 通过id查询检查项信息  回显数据
     *
     * @param id
     * @return Result
     */
    @GetMapping("/findById")
    public Result findById(int id) {
        CheckItem checkItem = checkItemService.findById(id);
        return new Result(true, MyMessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
    }

    /**
     * 在回显数据表单中提交修改数据 修改当前检查项通过id修改
     *
     * @param checkItem
     * @return Result
     */
    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem) {
        // 调用服务更新
        checkItemService.update(checkItem);
        // 返回操作的结果
        return new Result(true, MyMessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 通过id删除检查项
     * @param id
     * @return Result
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        checkItemService.deleteById(id);
        return new Result(true, MyMessageConstant.DELETE_CHECKITEM_SUCCESS);
    }
}
