package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Book;
import com.example.service.BookService;
import com.example.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.example.exception.CustomException;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Resource
    private BookService bookService;
    @Resource
    private HttpServletRequest request;

    public User getUser() {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomException("-1", "请登录");
        }
        return user;
    }

    @PostMapping
    public Result<?> save(@RequestBody Book book) {
        return Result.success(bookService.save(book));
    }

    @PutMapping
    public Result<?> update(@RequestBody Book book) {
        return Result.success(bookService.updateById(book));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        bookService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(bookService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(bookService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                              @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Book> query = Wrappers.<Book>lambdaQuery().orderByDesc(Book::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Book::getName, name);
        }
        return Result.success(bookService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        List<Book> all = bookService.list();
        for (Book obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("序号", obj.getId());
            row.put("用户ID", obj.getUserId());
            row.put("角色名称", obj.getName());
            row.put("角色描述", obj.getDescription());
            row.put("角色", obj.getRole());
            row.put("目标", obj.getGoal());
            row.put("样式", obj.getStyle());
            row.put("总提示词", obj.getTokens());
            row.put("语音", obj.getVoice());
            row.put("创建时间", obj.getCreatedAt());
            row.put("更新时间", obj.getUpdatedAt());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("虚拟角色信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

    @GetMapping("/upload/{fileId}")
    public Result<?> upload(@PathVariable String fileId) {
        String basePath = System.getProperty("user.dir") + "/src/main/resources/static/file/";
        List<String> fileNames = FileUtil.listFileNames(basePath);
        String file = fileNames.stream().filter(name -> name.contains(fileId)).findAny().orElse("");
        List<List<Object>> lists = ExcelUtil.getReader(basePath + file).read(1);
        List<Book> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Book obj = new Book();
            obj.setUserId(Integer.parseInt((String) row.get(1)));
            obj.setName((String) row.get(2));
            obj.setDescription((String) row.get(3));
            obj.setRole((String) row.get(4));
            obj.setGoal((String) row.get(5));
            obj.setStyle((String) row.get(6));
            obj.setTokens((String) row.get(7));
            obj.setVoice((String) row.get(8));
            obj.setCreatedAt(Timestamp.valueOf((String) row.get(9)));
            obj.setUpdatedAt(Timestamp.valueOf((String) row.get(10)));

            saveList.add(obj);
        }
        bookService.saveBatch(saveList);
        return Result.success();
    }
}
