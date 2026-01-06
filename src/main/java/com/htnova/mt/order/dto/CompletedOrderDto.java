package com.htnova.mt.order.dto;

import com.htnova.common.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class CompletedOrderDto extends BaseDto {
    

	/**
	* long订单号，数据库中请用bigint(20)存储此字段。
	*/
	private Long orderId;

	/**
	* set<integer>订单信息，8代表处方药
	*/
	private String orderTagList;

	/**
	* long订单展示id，与用户端、商家端订单详情中展示的订单号码一致。数据库中请用bigint(20)存储此字段。
	*/
	private Long wmOrderIdView;

	/**
	* stringapp方门店id
	*/
	private String appPoiCode;

	/**
	* string商家门店名称，即美团平台上当前订单所属门店的名称。
	*/
	private String wmPoiName;

	/**
	* string商家门店地址，即美团平台上当前订单所属门店的地址。
	*/
	private String wmPoiAddress;

	/**
	* string商家门店联系电话，即美团平台上当前订单所属门店的联系电话。
	*/
	private String wmPoiPhone;

	/**
	* string订单收货人地址，
	*/
	private String recipientAddress;

	/**
	* string订单收货人地址，按省、市、区、 街道、详细地址进行拆分。
	*/
	private String recipientAddressDetail;

	/**
	* string订单收货人联系电话
	*/
	private String recipientPhone;

	/**
	* list备用隐私号
	*/
	private String backupRecipientPhone;

	/**
	* string订单收货人姓名
	*/
	private String recipientName;

	/**
	* float门店配送费，单位是元。当前订单产生时该门店的配送费（商家自配送运费或美团配送运费），此字段数据为运费优惠前的原价。
	*/
	private Double shippingFee;

	/**
	* double订单的实际在线支付总价，单位是元。此字段数据为用户实际支付的订单总金额，含打包袋、配送费等。
	*/
	private Double total;

	/**
	* double订单的总原价
	*/
	private Double originalPrice;

	/**
	* string订单备注信息。
	*/
	private String caution;

	/**
	* string配送员联系电话，如为美团配送订单，当订单有配送员信息时，查询此字段有值；如为商家自配送订单，商家自行同步了配送员电话，查询则此字段有值。
	*/
	private String shipperPhone;

	/**
	* int订单状态，返回订单当前的状态。目前平台的订单状态参考值有：1-用户已提交订单；2-向商家推送订单；4-商家已确认；8-订单已完成；9-订单已取消。
	*/
	private String status;

	/**
	* long城市id，此字段信息为美团定义，商家无需使用。
	*/
	private Integer cityId;

	/**
	* int是否支持开发票：0-不支持，1-支持。
	*/
	private Integer hasInvoiced;

	/**
	* string发票抬头，为用户填写的开发票的抬头。
	*/
	private String invoiceTitle;

	/**
	* string纳税人识别号，此字段信息默认不推送，如商家支持订单开发票
	*/
	private String taxpayerId;

	/**
	* long订单创建时间，为10位秒级的时间戳，此字段为用户提交订单的时间。
	*/
	private String ctime;

	/**
	* long订单更新时间，为10位秒级的时间戳，此字段信息为当前订单最新订单/配送单状态更新的时间。
	*/
	private String utime;

	/**
	* long预计送达时间。
	*/
	private String deliveryTime;

	/**
	* int是否是第三方配送平台配送，0-否（含商家自配送和美团配送，目前无法区分，具体配送方式建议参考logisticsCode字段。）；1-是（第三方平台配送）。
	*/
	private Integer isThirdShipping;

	/**
	* int支付类型：1-货到付款，2-在线支付。目前订单只支持在线支付，此字段推送信息为2。
	*/
	private Integer payType;

	/**
	* int取货类型：0-普通(配送),1-用户到店自取。此字段的信息默认不推送
	*/
	private Integer pickType;

	/**
	* double订单收货地址的纬度，美团使用的是高德坐标系，也就是火星坐标系，商家如果使用的是百度坐标系需要自行转换，坐标需要乘以一百万。
	*/
	private String latitude;

	/**
	* double订单收货地址的经度，美团使用的是高德坐标系，也就是火星坐标系，商家如果使用的是百度坐标系需要自行转换，坐标需要乘以一百万。
	*/
	private String longitude;

	/**
	* int当日订单流水号，门店每日已支付订单的流水号从1开始。
	*/
	private Integer daySeq;

	/**
	* boolean订单用户是否收藏此门店：true-是， false-否
	*/
	private String isFavorites;

	/**
	* boolean订单用户是否第一次在此门店下单：true-是，false-否。
	*/
	private String isPoiFirstOrder;

	/**
	* boolean是否为预售单，true-是，false-否。如果为预售单，可以订阅estimateArrivalTime字段获取预计发货时间
	*/
	private String isPreSaleOrder;

	/**
	* int用餐人数（0：用户没有选择用餐人数；1-10：用户选择的用餐人数；-10：10人以上用餐；99：用户不需要餐具）。该信息默认不推送，商家如有需求可在开发者中心->基础设置->订单订阅字段 页面开启订阅字段“25 用餐人数”。此字段适用于餐饮类订单，闪购品类订单此字段信息默认为0。
	*/
	private Integer dinnersNumber;

	/**
	* string订单配送方式，该字段信息默认不推送，如0000-商家自配、1001-美团加盟、2002-快送、3001-混合送（即美团专送+快送）等。 
	*/
	private String logisticsCode;

	/**
	* string订单维度的商家对账信息，json格式数据。该信息默认不推送，商家如有需求可在开发者中心->基础设置->订单订阅字段 页面开启订阅字段“19 商家应收款详情”。
	*/
	private String poiReceiveDetail;

	/**
	* string订单商品详情，其值为由list
	*/
	private String detail;

	/**
	* string订单优惠信息，其值为由list
	*/
	private String extras;

	/**
	* string商品优惠详情，仅适用于美团闪购品类业务订单
	*/
	private String skuBenefitDetail;

	/**
	* string订单用户会员信息
	*/
	private String userMemberInfo;

	/**
	* double门店平均送货时长，单位是秒。
	*/
	private String avgSendTime;

	/**
	* int订单维度的打包袋金额，单位是分。
	*/
	private BigDecimal packageBagMoney;

	/**
	* int订单预计送达时间，为10位秒级的时间戳。
	*/
	private String estimateArrivalTime;

	/**
	* string订单维度的打包袋金额，单位是元。
	*/
	private String packageBagMoneyYuan;

	/**
	* string订单维度的商家对账信息，json格式数据。
	*/
	private String poiReceiveDetailYuan;

	/**
	* long订单重量（该信息默认不返回，可在开发者中心订阅），单位为克/g
	*/
	private Integer totalWeight;

	/**
	* int订单数据状态标记。-1：有数据降级0：无数据降级
	*/
	private Integer incmpCode;

	/**
	* set有降级的数据模块的集合，参考值：
	*/
	private String incmpModules;

	/**
	* string推送订单的预订人手机号，适用于鲜花绿植类商家的订单
	*/
	private String orderPhoneNumber;
	private String date;
	private Long timemillis;

	/**
	* 是否拣货完成（0-未完成，1-完成）
	*/
	private Integer pickingCompleted;

	/**
	* 预计到账收入
	*/
	private BigDecimal je;

	/**
	* 退款金额
	*/
	private BigDecimal tkje;

	/**
	* 处方金额
	*/
	private BigDecimal cfJe;

	/**
	* 运营费单价
	*/
	private BigDecimal yyflv;

	/**
	* 运营费金额
	*/
	private BigDecimal yyfJe;

	/**
	* 总收入
	*/
	private BigDecimal zji;

	/**
	* 分账金额
	*/
	private BigDecimal fzje;

	/**
	* 实扣金额
	*/
	private BigDecimal skje;

	/**
	* 退款后调整金额
	*/
	private BigDecimal tzje;

	/**
	* 渠道id
	*/
	private String chId;

	/**
	* 渠道处方分润
	*/
	private BigDecimal chFyCf;

	/**
	* 渠道运营费分润
	*/
	private BigDecimal chFyYyf;

	/**
	* 渠道分润金额
	*/
	private BigDecimal chZji;

	/**
	* 渠道调整金额
	*/
	private BigDecimal chTzje;

}
