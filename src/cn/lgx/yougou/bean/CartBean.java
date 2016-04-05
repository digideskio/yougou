package cn.lgx.yougou.bean;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class CartBean extends BmobObject {
	
	private String goodsinfo;//商品的详情
	private String fileUrl;//商品图片的Url
	private String size;//商品尺寸
	private String color;//商品颜色
	private Double price;//商品价格
	private String goodsId;//商品ID
	private String userid;//用户ID
	private Integer number;//购买数量
	private boolean isChoosed;//商品是否在购物车中被选中
	
	
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public boolean isChoosed() {
		return isChoosed;
	}
	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}
	public String getGoodsinfo() {
		return goodsinfo;
	}
	public void setGoodsinfo(String goodsinfo) {
		this.goodsinfo = goodsinfo;
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
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	
}
