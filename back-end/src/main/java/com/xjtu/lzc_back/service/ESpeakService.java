package com.xjtu.lzc_back.service;

import com.xjtu.lzc_back.entity.po.ESpeak;

import java.io.IOException;


/**
 * 语音服务接口
 */
public interface ESpeakService {

    //sign in interface
    /**
     * sign in
     * @param text
     * @return
     */
    String textToVoice(String text) throws IOException;

}
