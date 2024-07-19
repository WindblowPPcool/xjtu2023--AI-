package com.xjtu.lzc_back.service;

import com.xjtu.lzc_back.entity.po.Character;

import java.util.List;


/**
 * 商品服务接口
 */
public interface CharacterService {

    //sign in interface
    /**
     * sign in
     * @param character
     * @return
     */
     boolean new_character(Character character);


    //sign up interface
    /**
     * sign up

     * @return
     */
    List<Character> set_character(Character character);

    //boolean set_character(Character character);

}
