package com.itheima.health.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装返回结果, ajax, json 返回ajax请求时的json数据  直接返回result对象
 *
 * @author liyun
 * @date 2021/1/6 16:50
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result implements Serializable {
    private boolean flag;//执行结果，true为执行成功 false为执行失败
    private String message;//返回结果信息
    private Object data;//返回数据

    public Result(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }
}
