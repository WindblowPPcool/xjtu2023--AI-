package com.xjtu.lzc_back.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`virtual_character`")
@ApiModel(value="Character", description="角色信息表")
public class Character implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Integer user_id;

    @ApiModelProperty(value = "角色名")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "身份")
    private String role;

    @ApiModelProperty(value = "聊天目的")
    private String goal;

    @ApiModelProperty(value = "聊天风格")
    private String style;

    @ApiModelProperty(value = "总提示词")
    @TableField(jdbcType= JdbcType.LONGVARCHAR)
    private String tokens;
}
