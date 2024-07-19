package com.xjtu.lzc_back.controller;

import com.xjtu.lzc_back.entity.po.ESpeak;
import com.xjtu.lzc_back.enums.ResponseEnum;
import com.xjtu.lzc_back.result.Result;
import com.xjtu.lzc_back.service.ESpeakService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@Api(value = "ESpeakController",tags = "文本转语音控制层")
@RestController
@RequestMapping("/espeak")
public class ESpeakController {
    @Resource
    private ESpeakService espeakService;

    @PostMapping("/textToVoice")
    @ApiOperation(value = "文本转语音文件")
    public Result textToVoice(@RequestBody String text){
        try {
            //String outputFileName = "output.wav";
            System.out.println(text);
            String newtext=text.substring(9, text.length()-2);
            System.out.println(newtext);
            String wav= espeakService.textToVoice(newtext);
            if(!wav.equals("")){
                //System.out.println(wav);
                return Result.ok(wav,0);
            }
            else return Result.setResult(ResponseEnum.FAILD);
        } catch (IOException e) {
            return Result.error("文本转语音失败: " + e.getMessage());
        }
    }
}
