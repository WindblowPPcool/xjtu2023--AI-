package com.xjtu.lzc_back.controller;

import com.xjtu.lzc_back.entity.po.Account;
import com.xjtu.lzc_back.enums.ResponseEnum;
import com.xjtu.lzc_back.mapper.AccountMapper;
import com.xjtu.lzc_back.result.Result;
import com.xjtu.lzc_back.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@Api(value = "AccountController",tags = "账户控制层")
@RestController
@RequestMapping("/account")
public class AccountController {
    @Resource
    private AccountService accountService;

    @PostMapping("/signIn")
    @ApiOperation(value = "注册用户")
    public Result singIn(@RequestBody Account account){
        int b = accountService.signIn(account);
        if (b>0){
            if(accountService.sentCode(account)){
                accountService.transfer(account);
                return Result.ok(b);
            }
            else{
                return Result.error(ResponseEnum.MAIL_ERROR);
            }
        }else if (b==-0) {
            //throw new AccountException(ResponseEnum.EMAIL_ERROR);
            return Result.setResult(ResponseEnum.EMAIL_ERROR);
        }
        else {
            //throw new AccountException(ResponseEnum.USERNAME_ERROR);
            return Result.setResult(ResponseEnum.USERNAME_ERROR);
        }
    }

    @PostMapping("/verify")
    @ApiOperation(value = "注册验证")
    public Result verify(@RequestBody String code){
        String verifycode = code.substring(code.length()-8, code.length()-2);
        boolean flag = code.substring(8,12).equals("true");
        int b = accountService.verify(verifycode, flag);
        if(b>0){
            return Result.ok(b);
        }
        else{
            return Result.setResult(ResponseEnum.VERIFY_ERROR);
        }
    }

    @PostMapping("/signUp")
    @ApiOperation(value = "用户登录")
    public Result singUp(@RequestBody Account account){
        //System.out.println(account.getUsername());
        int b = accountService.signUp(account);
        //System.out.println(b);
        if (b>0){
            //System.out.println(b);
            //AccountMapper accountMapper = null;
            return Result.ok(b);
        }else if (b==0){
            //throw new AccountException(ResponseEnum.PARAM_ERROR);
            return Result.setResult(ResponseEnum.PASSWORD_ERROR);
        }
        else{
            //throw new AccountException(ResponseEnum.REGISTER_ERROR);
            return Result.setResult(ResponseEnum.REGISTER_ERROR);
        }
    }

    @PostMapping("/mailLogin")
    @ApiOperation(value = "邮箱登录")
    public Result MailLogIn(@RequestBody Account account) {
        boolean b = accountService.sentCode(account);
        if (!b)
            return Result.setResult(ResponseEnum.EMAIL_ERROR); // 这有问题，应该是邮箱不存在
        else {
            //accountService.sentCode(account);
            accountService.transfer(account);
            return Result.ok(0);
        }
    }

    @PostMapping("/Delete")
    @ApiOperation(value = "用户注销")
    public Result Delete(@RequestBody Account account){
        int b = accountService.Delete(account);
        if(b>0){
            return Result.ok(b);
        }
        else{
            return Result.setResult(ResponseEnum.FAILD);
        }
    }
}
