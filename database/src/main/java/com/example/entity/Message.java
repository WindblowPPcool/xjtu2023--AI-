package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("history")
public class Message extends Model<Message> {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer userId;

    private Integer characterId;

    private String question;

    private String answer;

    private Timestamp timestamp;
}
