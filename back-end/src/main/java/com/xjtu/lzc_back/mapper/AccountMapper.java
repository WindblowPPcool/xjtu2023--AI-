package com.xjtu.lzc_back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.xjtu.lzc_back.entity.po.Account;
import org.apache.ibatis.annotations.Select;

public interface AccountMapper extends BaseMapper<Account> {
    //1.注册时查询用户名是否已被占用，bool

    //2.注册时查询邮箱是否已经注册，bool

    //3.查询成功后写入新的数据，注册成功，bool

    //4.登陆时验证username+password是否登陆成功，bool

    @Select("SELECT * FROM account ORDER BY id DESC LIMIT 1")
    Account selectLastRow();
}
