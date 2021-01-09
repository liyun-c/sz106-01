package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MyMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liyun
 * @date 2021/1/9 20:00
 */

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    private static final Logger log = LoggerFactory.getLogger(OrderSettingController.class);

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 使用 Apache POI 批量导入预约设置
     * 前端页面传Excel文件过来  后端解析
     *
     * @param excelFile
     * @return Result
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile) {
        // Apache POI 提供给java 操作Microsoft office 格式文档读写功能
        // 使用Excel文件进行预约设置的多条数据插入
        // 使用编写的poi工具包文成对传输过来的 MultipartFile 文件 (选中的Excel文件) 读取

        // 解析excel文件，调用POI工具包解析文件，得到List<String[]> ，
        // 每一个数组就代表着一行记录
        try {
            List<String[]> excelData = POIUtils.readExcel(excelFile);

            log.debug("导入预约设置读取到了{}条记录", excelData.size());
            //转成List<Ordersetting>，再调用service 方法做导入，返回给页面

            // String[] 长度为2, 0:日期，1：数量

            // 将日期转换成数据库所需的格式
            final SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);

            //使用缓冲流的方式 遍历(forEach) 数组
            List<OrderSetting> orderSettingList = excelData.stream().map(arr -> {

                //创建实体类  封装数据
                OrderSetting orderSetting = new OrderSetting();

                try {
                    orderSetting.setOrderDate(sdf.parse(arr[0]));
                    orderSetting.setNumber(Integer.valueOf(arr[1]));
                } catch (ParseException e) {
                }

                //流中的方法  相当与for循环  不断将 orderSetting返回进orderSettingList 集合中
                return orderSetting;

            }).collect(Collectors.toList());
            // 调用服务将集合中数据导入数据库
            orderSettingService.addBatch(orderSettingList);

            return new Result(true, MyMessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            log.error("导入预约设置失败", e);
            return new Result(false, MyMessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 通过月份查询预约设置信息
     * @param month
     * @return Result
     */
    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        // 按月查询预约设置信息
        // 返回的数据为集合(选中月份所在预约设置表中最大预约人数,已预约人数,日期)
        // 没有实体类封装  直接用map传输

        List<Map<String,Integer>> data = orderSettingService.getOrderSettingByMonth(month);
        return new Result(true, MyMessageConstant.GET_ORDERSETTING_SUCCESS,data);
    }

    /**
     * 通过日期设置可预约的最大数
     * 前端页面单击数据显示框中的设置按钮  设置最大预约人数存入
     * 预约设置表中
     * @param orderSetting
     * @return Result
     */
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        // 更新数据
        orderSettingService.editNumberByDate(orderSetting);
        return new Result(true, MyMessageConstant.ORDERSETTING_SUCCESS);
    }
}

