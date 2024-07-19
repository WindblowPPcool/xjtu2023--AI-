package com.xjtu.lzc_back.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjtu.lzc_back.entity.po.Account;
import com.xjtu.lzc_back.entity.po.Character;
import com.xjtu.lzc_back.entity.po.History;
import com.xjtu.lzc_back.mapper.CharacterMapper;
import com.xjtu.lzc_back.service.CharacterService;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CharacterServiceImpl extends ServiceImpl<CharacterMapper, Character> implements CharacterService {

    private final CharacterMapper characterMapper;
    private Character character;

    public CharacterServiceImpl(CharacterMapper characterMapper) {
        this.characterMapper = characterMapper;
    }

    //@Override
    public boolean new_character(Character character) {

        String name = character.getName();
        String description = character.getDescription();
        String role = character.getRole();
        String goal = character.getGoal();
        String style = character.getStyle();
        String tokens = String.format("你现在是%s，是%s，你将作为%s与我对话，对话目的为%s，对话风格为%s。在以下的对话中，始终保持此身份和我对话。",name,description,role,goal,style);
        character.setTokens(tokens);

        int num=baseMapper.insert(character);
        return num>0;
    }

    public List<Character> set_character(Character character) {

        QueryWrapper<Character> queryWrapper = new QueryWrapper<>();
        QueryWrapper<Character> foundWrapper = queryWrapper.eq("id", character.getId());
        return list(foundWrapper);
    }
}
