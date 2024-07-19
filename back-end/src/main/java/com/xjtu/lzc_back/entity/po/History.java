package com.xjtu.lzc_back.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`history`")
@ApiModel(value="History", description="聊天记录表")
public class History implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private int user_id;

    @ApiModelProperty(value = "角色ID")
    private int character_id;

    @ApiModelProperty(value = "用户问题")
    @TableField(jdbcType= JdbcType.LONGVARCHAR)
    private String question;

    @ApiModelProperty(value = "模型回答")
    @TableField(jdbcType= JdbcType.LONGVARCHAR)
    private String answer;

    @ApiModelProperty(value = "时间戳")
    @TableField("`timestamp`")
    private String timestamp;
}
