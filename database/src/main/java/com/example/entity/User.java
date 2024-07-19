package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.example.common.handler.ListHandler;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@TableName(value = "account", autoResultMap = true)
public class User extends Model<User> {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String gender;

    private String nickname;

    private String birthday;

    private String profession;

    private String region;

    private String hobbies;

    private String signature;

    private String avatar;

    /**
     * 角色
     */
    @TableField(typeHandler = ListHandler.class)
    private List<Long> role;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    /**
     * 权限
     */
    @TableField(exist = false)
    private List<Permission> permission;

}
