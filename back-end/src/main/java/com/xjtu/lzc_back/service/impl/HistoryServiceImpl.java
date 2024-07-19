package com.xjtu.lzc_back.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjtu.lzc_back.entity.po.History;
import com.xjtu.lzc_back.mapper.HistoryMapper;
import com.xjtu.lzc_back.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements HistoryService {

    private final HistoryMapper historyMapper;
    private History history;

    public HistoryServiceImpl(HistoryMapper historyMapper) {
        this.historyMapper = historyMapper;
    }

    @Override
    public boolean store(History history) {
        int num=historyMapper.insert(history);
        return num>0;
    }

    //@Override
    public List<History> queryHistoryList(History history) {
        QueryWrapper<History> newWrapper = new QueryWrapper<>();
        QueryWrapper<History> Character_Wrapper = newWrapper.eq("character_id", history.getCharacter_id());
        //System.out.println(list(Character_Wrapper));
        return list(Character_Wrapper);
    }




}
