package com.itheima.health.service;

import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author liyun
 * @date 2021/1/9 20:13
 */

public interface OrderSettingService {

    /**
     * 将集合中数据导入数据库
     * @param orderSettingList
     */
    void addBatch(List<OrderSetting> orderSettingList);

    /**
     * 通过月份查询预约设置信息
     * @param month
     * @return List<Map<String, Integer>>
     */
    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    /**
     * 通过日期设置可预约的最大数
     * 前端页面单击数据显示框中的设置按钮  设置最大预约人数存入
     * 预约设置表中
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting);
}
