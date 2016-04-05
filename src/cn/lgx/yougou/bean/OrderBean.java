package cn.lgx.yougou.bean;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class OrderBean extends BmobObject {
	
	private String orderinfo;//商品的详情
	private String size;//商品尺寸
	private String color;//商品颜色
	private Double price;//商品价格
	private String address;//收获地址
	private String goodsId;//商品ID
	private String userid;//用户ID
	private Boolean ispinglun;//是否已评价
	private Boolean isshouhuo;//是否已收货
	private Integer number;//购买数量
	private String fileUrl;//商品图片的Url
	
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Boolean getIsshouhuo() {
		return isshouhuo;
	}
	public void setIsshouhuo(Boolean isshouhuo) {
		this.isshouhuo = isshouhuo;
	}
	
	public Boolean getIspinglun() {
		return ispinglun;
	}
	public void setIspinglun(Boolean ispinglun) {
		this.ispinglun = ispinglun;
	}
	public String getOrderinfo() {
		return orderinfo;
	}
	public void setOrderinfo(String orderinfo) {
		this.orderinfo = orderinfo;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
	
	
}
