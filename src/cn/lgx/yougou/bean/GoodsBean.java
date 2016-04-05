package cn.lgx.yougou.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

@SuppressWarnings("serial")
public class GoodsBean extends BmobObject {
	
	
	private String number;//编号
	private String info;//描述
	private Integer salesNum;//销量
	private Double price;//价格
	private BmobFile goodspng;//图片
	private String brand;//品牌
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public Integer getSalesNum() {
		return salesNum;
	}
	public void setSalesNum(Integer salesNum) {
		this.salesNum = salesNum;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public BmobFile getGoodspng() {
		return goodspng;
	}
	public void setGoodspng(BmobFile goodspng) {
		this.goodspng = goodspng;
	}
	
	
	
}
