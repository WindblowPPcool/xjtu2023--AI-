package com.xjtu.lzc_back.controller;

import com.xjtu.lzc_back.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "测试控制层")
public class TestController {
    //日志依赖的测试方法
    @GetMapping("/testLog")
    @ApiOperation("日志依赖的测试方法")
    public String testLog(){
        log.info("日志业务方法被执行");
        return "success";
    }

    //swagger依赖的测试方法
    @GetMapping("/testSwagger")
    @ApiOperation("swagger依赖的测试方法")
    public String testSwagger(){
        return "testSwagger";
    }
    //统一返回结果的测试方法
    @GetMapping("/testResult")
    @ApiOperation("统一返回结果的测试方法")
    public Result testResult(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("草莓","0.01");
        map.put("张飞","男");
        map.put("关羽","男");
        map.put("貂蝉","女");
        return Result.ok(map,0);
    }
}
