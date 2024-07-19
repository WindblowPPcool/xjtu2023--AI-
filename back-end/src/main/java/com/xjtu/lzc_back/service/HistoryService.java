package com.xjtu.lzc_back.service;

import com.xjtu.lzc_back.entity.po.History;

import java.util.List;

/**
 * 商品服务接口
 */
public interface HistoryService {

    //sign in interface
    /**
     * sign in
     * @param history
     * @return
     */
     boolean store(History history);


    //sign up interface
    /**
     * sign up

     * @return
     */
    List<History> queryHistoryList(History history);

}
