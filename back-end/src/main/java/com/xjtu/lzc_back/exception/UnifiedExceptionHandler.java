package com.xjtu.lzc_back.exception;

import com.xjtu.lzc_back.enums.ResponseEnum;
import com.xjtu.lzc_back.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Component
@RestControllerAdvice //抓住异常
public class UnifiedExceptionHandler {
    /**
     * 监听系统异常规则
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> handleException(Exception e){
        log.error("系统异常=》{}"+e);
        return Result.error(ResponseEnum.FAILD.getCode(),ResponseEnum.FAILD.getMessage());
    }

    /**
     * 监听用户自定义异常规则
     * @param e
     * @return
     */
    @ExceptionHandler(value = AccountException.class)
    public Result<?> handleBusinessPayException(AccountException e){
        log.error("用户自定义异常=》{}"+e);
        return Result.error(e.getCode(), e.getMessage());
    }
}
