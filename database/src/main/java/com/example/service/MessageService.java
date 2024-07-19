package com.example.service;

import com.example.entity.Message;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MessageService extends ServiceImpl<MessageMapper, Message> {

    @Resource
    private MessageMapper messageMapper;

}
