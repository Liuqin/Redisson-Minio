package org.qin.common;

/**
 * @title: ResultStatus
 * @decription:
 * @author: liuqin
 * @date: 2020/8/19 14:34
 */
public enum ResultStatus {
    /**
     * 成功
     */
    SUCCESS(200, "成功"), //未授权限，请联系管理员
    PARAM_ERROR(9999, "参数有误"),
    SYS_BUSY(-9998, "系统繁忙，请稍候再试！"),
    SYS_ERROR(-9999, "系统错误！"),
    UPDATE_ERROR(-10000, "转换出错！"),
    DELETE_ERROR(-9997, "删除失败！"),
    TOKEN_REMOTE_LOGIN(-11000, "异地登录"),
    JIM_ERROR(-9996, "极光IM错误"),
    VERSION_NO(-12000, "当前版本已停止使用，请安装新版本APP"),
    THIRD_PARTY_ACCOUNT_NO_EXIST(-13000, "第三方账号不存在，优先绑定手机号"),
    ;


    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;


    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }
}

