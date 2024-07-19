package com.xjtu.lzc_back.controller;

import com.xjtu.lzc_back.entity.po.Character;
import com.xjtu.lzc_back.entity.po.History;
import com.xjtu.lzc_back.enums.ResponseEnum;
import com.xjtu.lzc_back.result.Result;
import com.xjtu.lzc_back.service.CharacterService;
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
@Api(value = "CharacterController",tags = "虚拟人物控制层")
@RestController
@RequestMapping("/character")
public class CharacterController {
    @Resource
    private CharacterService characterService;

    @PostMapping("/new_character")
    @ApiOperation(value = "新建虚拟人物")
    public Result new_character(@RequestBody Character character){
        //System.out.println(history.getUser_id());
        boolean b = characterService.new_character(character);
        if(b){
            return Result.ok(character,0);
        }
        else{
            return Result.setResult(ResponseEnum.FAILD);
        }
    }

    @PostMapping("/set_character")
    @ApiOperation(value = "设置虚拟人物")
    public Result<List<Character>> set_character(@RequestBody Character character){
        return Result.ok(characterService.set_character(character),0);
    }
}
