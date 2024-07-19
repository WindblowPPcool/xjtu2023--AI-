package com.xjtu.lzc_back.result;

import com.xjtu.lzc_back.enums.IResponseEnum;
import com.xjtu.lzc_back.enums.ResponseEnum;
import lombok.Data;

/**
 * 统一返回结果
 * @author lily
 */
@Data
public class Result<T> {

    private Integer code;
    private String message;
    private T data;
    private int user_id;

    public Result(){}

    public Result(Integer code, String message, T data, int user_id) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.user_id = user_id;
    }

    /** 成功 */
    public static final Integer SUCCESS = ResponseEnum.SUCCESS.getCode();

    /** 失败 */
    public static final Integer FAIL = ResponseEnum.FAILD.getCode();

    private static <T> Result<T> restResult(T data, int code, String message, int user_id)
    {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMessage(message);
        apiResult.setUser_id(user_id);
        return apiResult;
    }

    /**
     * 返回 失败结果
     */
    public static <T> Result<T> ok(int i){
        return restResult(null, SUCCESS, ResponseEnum.SUCCESS.getMessage(), i);
    }

    public static <T> Result<T> ok(T data, int i){
        return restResult(data, SUCCESS, ResponseEnum.SUCCESS.getMessage(), i);
    }

    public static <T> Result<T> ok(T data, String msg, int i) {
        return restResult(data, SUCCESS, msg, i);
    }

    /**
     * 返回 失败结果
     */
    public static <T> Result<T> error() {
        return restResult(null, FAIL, ResponseEnum.FAILD.getMessage(), -1);
    }

    public static <T> Result<T> error(String msg) {
        return restResult(null, FAIL, msg, -1);
    }

    public static <T> Result<T> error(T data) {
        return restResult(data, FAIL, ResponseEnum.FAILD.getMessage(), -1);
    }

    public static <T> Result<T> error(T data, String msg) {
        return restResult(data, FAIL, msg, -1);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return restResult(null, code, msg, -1);
    }

    /**
     * 返回 指定结果
     */
    public  static<T>  Result<T> setResult(IResponseEnum responseEnum){
        return restResult(null, (Integer) responseEnum.code(), (String) responseEnum.message(), 0);
    }
    public  static<T>  Result<T> setResult(IResponseEnum responseEnum, T data){
        return restResult(data, (Integer) responseEnum.code(), (String) responseEnum.message(), 0);
    }

}

