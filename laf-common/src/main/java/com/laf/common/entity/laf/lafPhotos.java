package com.laf.common.entity.laf;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (laf_photos)表实体类
 *
 * @author Kamisora
 * @since 2022-06-25 10:42:58
 */

@Data
@NoArgsConstructor
@AllArgsConstructor

@TableName(value = "laf_photos")
public class lafPhotos {
    //启示对应图片id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //启示对应图片url
    @TableField("laf_photoUrl")
    private String lafPhotoUrl;
    //图片对应的启示id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long lafId;
    //0（启用）1（禁用）
    private String status;
    //是否为首页展示图片
    private String indexDisplay;
}

