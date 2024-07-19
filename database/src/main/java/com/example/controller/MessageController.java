package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Message;
import com.example.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Resource
    private MessageService messageService;

    @PostMapping
    public Result<?> save(@RequestBody Message message) {
        return Result.success(messageService.save(message));
    }

    @PutMapping
    public Result<?> update(@RequestBody Message message) {
        return Result.success(messageService.updateById(message));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        messageService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Message> findById(@PathVariable Long id) {
        return Result.success(messageService.getById(id));
    }

    @GetMapping
    public Result<List<Message>> findAll() {
        return Result.success(messageService.list());
    }

    @GetMapping("/page")
    public Result<IPage<Message>> findPage(@RequestParam(required = false, defaultValue = "") String question,
                                           @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                           @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Message> queryWrapper = Wrappers.<Message>lambdaQuery()
                .like(StrUtil.isNotBlank(question), Message::getQuestion, question)
                .orderByDesc(Message::getId);
        return Result.success(messageService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
