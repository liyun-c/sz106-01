package com.itheima.health.controller;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MyMessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author liyun
 * @date 2021/1/8 18:59
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    private static Logger log = LoggerFactory.getLogger(SetmealController.class);

    @Reference
    private SetmealService setmealService;

    /**
     * 上传图片
     * @param imgFile
     * @return Result
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        // 获取源文件名
        String originalFilename = imgFile.getOriginalFilename();
        // 截取后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 生成随机且唯一的文件名id
        String uniqueId = UUID.randomUUID().toString();
        // 拼接唯一文件名
        String filename = uniqueId + suffix;
        // 调用common子模块中工具包的QiNiuUtils7牛工具类上传图片
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), filename);
            // 构建返回的数据
              /*
              页面回显图片需要图片的完整路径 域名加文件名
              但是我们不应该将完整路径存入数据库  一旦我们不用七牛云存储图片 页面将无法回显图片
              将两者分开是较好的选择
              imgName: 图片名 , 补全前端绑定的formData.img
              domain: 七牛的域名 图片回显的完整路径imageUrl = domain+图片名
              */
            Map<String,String> map = new HashMap<String,String>(2);

            map.put("imgName", filename);
            map.put("domain",QiNiuUtils.DOMAIN);

            return new Result(true, MyMessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            log.error("上传文件失败了",e);
            return new Result(false, MyMessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加套餐
     * @param setmeal 套餐基本信息
     * @param checkgroupIds 套餐与检查组的关系数组
     * @return Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用服务添加套餐
        setmealService.add(setmeal,checkgroupIds);
        return new Result(true, MyMessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return Result
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> setmealPageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MyMessageConstant.QUERY_SETMEAL_SUCCESS,setmealPageResult);
    }

    /**
     * 通过id查询套餐  回显套餐基本信息 操作套餐表
     * @param id
     * @return Result
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 查询套餐基本信息
        Setmeal setmeal = setmealService.findById(id);
        // 构建前端需要的数据, 还要有域名用于回显页面图片
        Map<String,Object> map = new HashMap<String,Object>(2);
        map.put("setmeal",setmeal);
        map.put("domain",QiNiuUtils.DOMAIN);
        return new Result(true, MyMessageConstant.QUERY_SETMEAL_SUCCESS,map);
    }

    /**
     * 查询选中的检查组id集合  即套餐与检查组之间的关系 操作套餐与检查组之间的关系表
     * @param id
     * @return Result
     */
    @GetMapping("/findCheckGroupIdsBySetmealId")
    public Result findCheckGroupIdsBySetmealId(int id){
        List<Integer> checkgroupIds = setmealService.findCheckGroupIdsBySetmealId(id);
        return new Result(true, MyMessageConstant.QUERY_SETMEAL_SUCCESS,checkgroupIds);
    }

    /**
     * 修改套餐  相当与添加套餐  删除旧关系添加新关系
     * @param setmeal  套餐基本信息  套餐表
     * @param checkgroupIds 套餐与检查组之间的关系数组     套餐与检查组之间的关系表
     * @return Result
     */
    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用服务修改套餐
        setmealService.update(setmeal,checkgroupIds);
        return new Result(true, "编辑套餐成功");
    }

    /**
     * 通过id删除套餐
     * 需要分析套餐是否跟其他表存在多对一的关系   即一方拥有多个套餐信息
     * 如果没有可以直接删除
     * @param id
     * @return Result
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        setmealService.deleteById(id);
        return new Result(true, "删除套餐成功");
    }


}
