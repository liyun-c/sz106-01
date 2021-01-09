package com.itheima.health.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author liyun
 * @date 2021/1/9 20:14
 */

/**
 * 使用阿里巴巴的包，发布服务 interfaceClass指定服务的接口类
 * 如果不使用interfaceClass指定服务在有的dabble版本中  服务发布上去服务前缀名会不是我们的包名
 * 导致无法订阅
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private static OrderSettingDao orderSettingDao;

    /**
     * 将集合中数据导入数据库
     *
     * @param orderSettingList
     */
    @Override
    public void addBatch(List<OrderSetting> orderSettingList) {
        // 首先要判断orderSettingList是否为空
        // 以免出现不可预期异常
        // 使用org.springframework.util中的
        if (!CollectionUtils.isEmpty(orderSettingList)) {
            // 遍历导入的预约设置信息orderSettingList
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (OrderSetting orderSetting : orderSettingList) {
                // 以免插入相同数据 首先我们可以
                // 通过预约的日期来查询预约设置表
                // 看这个日期的设置信息有没有
                OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());

                panduan(osInDB, orderSetting);

            }
        }
    }

    /**
     * 通过月份查询预约设置信息
     *
     * @param month
     * @return List<Map   <   String   ,       I   nteger>>
     */
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        //使用模糊查询 不用再映射配置文件中加%号了
        // select CAST(DATE_FORMAT(orderdate,'%d') AS SIGNED) date,number,reservations
        // From t_ordersetting where orderDate like #{month}

        //前端日期 显示为     1,2.3....9,10........
        //数据库存入的日期为  2021/01/09
        //所以要对查出的数据进行加工 去别名为date
        month += " %";
        return orderSettingDao.getOrderSettingByMonth(month);
    }

    /**
     * 通过日期设置可预约的最大数
     * 前端页面单击数据显示框中的设置按钮  设置最大预约人数存入
     * 预约设置表中
     *
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        // 更新数据库中的最大预约
        // 但是最大预约人数必不能小于当前预约人数  所以要作出判断

        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());

        panduan(osInDB, orderSetting);

    }

    public static void panduan(OrderSetting osInDB, OrderSetting orderSetting) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 如果没有预约设置(表中没有这个日期的记录)
        if (null == osInDB) {
            // 就可以插入数据
            orderSettingDao.add(orderSetting);
        } else {
            // 有预约设置(表中有这个日期的记录)  就根据对应数据可以进行更新
            // 根据表的用途和结构 分析出能进行更新的数据只有最大预约人数
            // 但是最大预约人数必不能小于当前预约人数  所以要作出判断
            // 判断已预约人数是否大于要更新的最大预约数

            // 已预约人数  reservations
            int reservations = osInDB.getReservations();

            //要更新的最大预约数 number
            int number = orderSetting.getNumber();
            if (reservations > number) {
                // 大于则要报错，接口方法 异常声明
                throw new MyException(sdf.format(orderSetting.getOrderDate()) + ": 最大预约数不能小于已预约人数");
            } else {
                // 小于，则可以更新最大预约数
                orderSettingDao.updateNumber(orderSetting);
            }
        }
    }
}
