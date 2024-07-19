package com.xjtu.lzc_back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjtu.lzc_back.entity.po.ESpeak;
import com.xjtu.lzc_back.mapper.ESpeakMapper;
import com.xjtu.lzc_back.service.ESpeakService;
import com.xjtu.lzc_back.utils.Base64Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Base64;
import java.io.File;

@Slf4j
@Service
public class ESpeakServiceImpl extends ServiceImpl<ESpeakMapper, ESpeak> implements ESpeakService {

    private final ESpeakMapper espeakMapper;
    private ESpeak eSpeak;

    public ESpeakServiceImpl(ESpeakMapper espeakMapper) {
        this.espeakMapper = espeakMapper;
    }

    @Override
    public String textToVoice(String text0) throws IOException {
        String text = text0;
        //String voice = espeak.getVoice();
        //String outputFileName = espeak.getOutputFileName();

        // eSpeak.exe 的完整路径,自行设置
        String espeakPath = "";

        /*
        // 创建输出文件的目录（如果不存在）
        File outputFile = new File(outputFileName);
        outputFile.getParentFile().mkdirs();
         */

        // 生成 eSpeak 命令，指定语音音色
        String command = String.format("echo \"%s\" | \"%s\" -v zh+f1 -w \"output.wav\"", text, espeakPath);//output.wav为输出文件存储地址，自行设置

        // 使用 ProcessBuilder 执行命令
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        //System.out.println(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        String tempBase64Str = "";

        // 等待命令执行完成
        try {
            int exitValue = process.waitFor();
            if (exitValue != 0) {
                throw new IOException("Text to speech conversion failed with exit code " + exitValue);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Text to speech conversion interrupted", e);
        }
        File file=new File("output.wav");//同样自行设置输出文件地址
        tempBase64Str = Base64Utils.fileToBase64Str(file);
        //file.delete();
        return tempBase64Str;
    }
}

