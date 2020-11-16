package org.qin.common;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @title: ResultModel
 * @decription:
 * @author: liuqin
 * @date: 2020/8/19 14:42
 */

public class ResultModel<T> implements Serializable {

    /**
     * 返回码
     */
//    @ApiModelProperty(value = "返回码", required = true)
    private int status;

    /**
     * 返回结果描述
     */
//    @ApiModelProperty(value = "结果描述", required = true)
    private String message;

    /**
     * 返回内容
     */
//    @ApiModelProperty(value = "数据", required = true)
    private T data;


    public int getStatus() {
        return status;
    }


    public String getMessage() {
        return message;
    }


    public ResultModel() {
        super();
    }


    public ResultModel(int status, String message, T content) {
        this.status = status;
        this.message = message;
        this.data = content;
    }


    public ResultModel(ResultStatus status) {
        this.status = status.getCode();
    }


    public ResultModel(ResultStatus status, T data) {
        this.status = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }


    public ResultModel<T> setStatus(int status) {
        this.status = status;
        return this;
    }


    public ResultModel<T> setMessage(String message) {
        this.message = message;
        return this;
    }


    public T getData() {
        return data;
    }


    public ResultModel<T> setData(T data) {
        this.data = data;
        return this;
    }


    public static ResultModel suc(Object data) {
        return new ResultModel(ResultStatus.SUCCESS, data);
    }


    public static ResultModel suc() {
        return new ResultModel(ResultStatus.SUCCESS, null);
    }


    public static ResultModel ok() {
        return new ResultModel(ResultStatus.SUCCESS, new HashMap<>(20));
    }


    public static ResultModel success() {
        return new ResultModel(ResultStatus.SUCCESS);
    }


    public static ResultModel error(ResultStatus error) {
        return new ResultModel(error, new HashMap<>(20));
    }
}
