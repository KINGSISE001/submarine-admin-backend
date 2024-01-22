package com.htnova.mt.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @TableName o2o_brand_info
 */
@TableName(value ="o2o_brand_info")
@Data
public class O2oBrandInfo implements Serializable {
    /**
     * 应用名称
     */

    private String appname;

    /**
     * 应用ID
     *
     */
    @TableId
    private Integer mtappid;

    /**
     * 应用key
     */
    private String mtappkey;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌ID
     */
    private Integer brandId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
