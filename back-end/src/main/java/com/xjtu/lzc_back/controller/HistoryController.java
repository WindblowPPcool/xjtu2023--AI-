package com.xjtu.lzc_back.controller;

import com.xjtu.lzc_back.entity.po.History;
import com.xjtu.lzc_back.result.Result;
import com.xjtu.lzc_back.service.HistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(value = "HistoryController",tags = "聊天记录控制层")
@RestController
@RequestMapping("/history")
public class HistoryController {
    @Resource
    private HistoryService historyService;

    @PostMapping("/store")
    @ApiOperation(value = "存入记录")
    public Result store(@RequestBody History history){
        //System.out.println(history.getCharacter_id());
        boolean b = historyService.store(history);
        return Result.ok(0);
    }

    @PostMapping("/read")
    @ApiOperation(value = "读出记录")
    public Result<List<History>> queryHistoryList(@RequestBody History history){
        return Result.ok(historyService.queryHistoryList(history),0);
    }
}
