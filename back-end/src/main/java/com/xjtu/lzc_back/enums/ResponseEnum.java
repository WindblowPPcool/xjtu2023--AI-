package com.xjtu.lzc_back.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author LZC
 * 常规操作的异常返回
 */
@Getter
@ToString
@AllArgsConstructor
public enum ResponseEnum implements IResponseEnum<Integer, String, ResponseEnum> {

    SUCCESS(200, "成功"),
    UNAUTHORIZED(401, "未登录"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "路径错误"),
    FAILD(500, "失败"),
    PARAM_MISS(501, "参数异常"),
    MAINTENANCE(-500, "网站维护中。。。"),
    // code 可以自己规范一下，不同值的范围
    USERNAME_ERROR(-201,"用户名已存在"),
    EMAIL_ERROR(-202, "邮箱已被注册"),
    PASSWORD_ERROR(-203, "密码错误"),
    REGISTER_ERROR(-204, "尚未注册"),
    VERIFY_ERROR(-205, "验证码错误"),
    MAIL_ERROR(-206, "发送失败"),
    PARAM_ERROR(-207, "非法参数");
    private Integer code;
    private String message;
    @Override
    public ResponseEnum get() {
        return this;
    }
    @Override
    public Integer code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
