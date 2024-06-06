package com.htnova.mt.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.htnova.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@TableName("shop")
public class Shop extends BaseEntity {


	/**
	* 门店编码
	*/
	private String bma;

	/**
	* 门店名称
	*/
	private String mcheng;

	/**
	* 门店类别：美团 饿了么 药急送 等
	*/
	private String lbei;

	/**
	* 行业：医药 水果 文体
	*/
	private String hye;

	/**
	* 类型：全国店  普通店
	*/
	private String lxing;

	/**
	* 大区
	*/
	private String dqu;

	/**
	* 省
	*/
	private String sheng;

	/**
	* 城市
	*/
	private String city;

	/**
	* 门店电话
	*/
	private String dhua;

	/**
	* 状态：0 正常  1：停业  -1：不合作
	*/
	private Integer ztai;

	/**
	* 运营人员
	*/
	private String yyren;

	/**
	* 联系人
	*/
	private String xsren;

	/**
	* 联系人1-原始人员
	*/
	private String xsren1;

	/**
	* 财务entid:tSysUser
	*/
	private String zwentId;

	/**
	* 线下门店id:tSysUserPoi
	*/
	private Integer sShopId;

	/**
	* 运营费率
	*/
	private BigDecimal yyflv;

	/**
	* 处方类型：0： 单价  1：比例
	*/
	private BigDecimal cfLxing;

	/**
	* 处方价：开方+审方
	*/
	private BigDecimal cfjia;

	/**
	* 审方价
	*/
	private BigDecimal sfjia;

	/**
	* 备注
	*/
	private String bzhu;

	/**
	* 分账状态0: 未分账 1：已分账
	*/
	private Integer fzZtai;

	/**
	* 分账费率
	*/
	private BigDecimal fzYyflv;

	/**
	* 分账开始时间
	*/
	private LocalDateTime fzBegintime;

	/**
	* 分账结束时间
	*/
	private LocalDateTime fzEndtime;
	private String tMcheng;

	/**
	* 渠道id 代替原来 qdao
	*/
	private String chId;

	/**
	* 原来渠道名称
	*/
	private String qdao;

	/**
	* 4月份后标志
	*/
	private Integer bzhi;

	/**
	* app名称

	*/
	private String appname;

	/**
	* 运营费分俑方式：0：比例 1:单价 2: 阶梯
	*/
	private Integer fyYyfLxing;

	/**
	* 运营费分俑比例/单价 20% 25%  0.001
	*/
	private BigDecimal fyYyfflv;

	/**
	* 处方分俑方式：0：比例 1:单价 2: 阶梯
	*/
	private Integer fyCfLxing;

	/**
	* 处方分俑比例/单价 20% 25%
	*/
	private BigDecimal fyCfflv;

	/**
	* 审方分俑比例/单价 20% 25%
	*/
	private BigDecimal fySfflv;


}
