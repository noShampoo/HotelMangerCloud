package com.xust.hotel.common.restful;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * @author bhj
 */
@AllArgsConstructor
public enum StatusEnum implements Serializable {

    /**
     * 成功
     */
    OK(20000, "成功"),

    /**
     * 失败
     */
    ERROR(20001, "失败"),

    /**
     * 登录失败
     */
    LOGIN_ERROR(20002, "用户名或密码错误"),

    /**
     * 权限不足
     */
    ACCESS_ERROR(20003, "权限不足"),

    /**
     * 远程调用失败
     */
    REMOTE_ERROR(20004, "远程调用失败"),

    /**
     * 重复操作
     */
    REP_ERROR(20004, "重复操作"),

    /**
     * 参数错误
     */
    PARAM_ERROR(20006, "参数错误"),

    /**
     * 权限过期
     */
    EXPIRED_ERROR(20007, "权限过期"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(2008, "用户不存在"),

    /**
     * 客户信息有误
     */
    CUSTOMER_INFO_ERROR(20009, "客户信息有误"),

    /**
     * can't reserve error
     */
    CAN_NOT_RESERVE(30000, "无法预定"),

    /**
     * can't delete
     */
    CAN_NOT_DELETE(30001, "无法删除"),

    /**
     * no such key
     */
    NO_SUCH_KEY(30002, "不存在此key"),

    /**
     * key exist
     */
    KEY_EXIST(30003, "key已存在"),

    /**
     * status error
     */
    STATUS_ERROR(30004, "status error"),

    /**
     * can not chancel
     */
    CAN_NOT_CHANCEL(30005, "取消失败"),

    /**
     * BIZ_INFO_ERROR
     */
    BIZ_INFO_ERROR(30006, "业务信息错误"),

    /**
     * can not operate
     */
    CAN_NOT_OPERATE(30007, "无法操作"),

    /**
     * inner error
     */
    INNER_ERROR(50000, "内部异常")
    ;
    /**
     * 状态码
     */
    private int code;

    /**
     * 状态说明
     */
    private String desc;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
