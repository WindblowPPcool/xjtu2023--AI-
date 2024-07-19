package com.xjtu.lzc_back.service;

import com.xjtu.lzc_back.entity.po.Account;

/**
 * 商品服务接口
 */
public interface AccountService {

    //sign in interface
    /**
     * sign in
     * @param account
     * @return
     */
    int signIn(Account account);


    //sign up interface
    /**
     * sign up
     * @param account
     * @return
     */
    int signUp(Account account);
    int verify(String code, boolean flag);
    boolean sentCode(Account account);
    void transfer(Account account);

    int Delete(Account account);
    //int mailLogin(Account account);
    //boolean mailVerify(String code);
}
