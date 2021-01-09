package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liyun
 * @date 2021/1/9 20:17
 */

public interface OrderSettingDao {

    /**
     * 通过预约的日期来查询预约设置表，看这个日期的设置信息有没有
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 插入预约设置数据
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 更新预约设置数据
     * @param orderSetting
     */
    void updateNumber(OrderSetting orderSetting);

    /**
     * 通过月份查询预约设置信息
     * @param month
     * @return List<Map<String, Integer>>
     */
    List<Map<String, Integer>> getOrderSettingByMonth(String month);
}
