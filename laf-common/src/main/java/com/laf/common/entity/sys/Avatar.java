package com.laf.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Avatar)表实体类
 *
 * @author Kamisora
 * @since 2022-06-25 10:39:22
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_avatar")

public class Avatar {
    //用户头像唯一id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //用户头像url
    private String avatarUrl;
    //用户头像启用状态（0启用,1禁用）
    private Integer status;
    //用户头像对应用户id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

}

