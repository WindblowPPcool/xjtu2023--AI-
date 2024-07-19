package com.xjtu.lzc_back.exception;


import com.xjtu.lzc_back.enums.IResponseEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 业务异常
 * 自定义异常---手动抛出业务中的异常信息
 * @author LZC
 */

@Data
@NoArgsConstructor
public class AccountException extends RuntimeException {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    public AccountException(IResponseEnum<?,?,?> responseEnum) {
        this.code = (Integer) responseEnum.code();
        this.message = (String) responseEnum.message();
    }
}