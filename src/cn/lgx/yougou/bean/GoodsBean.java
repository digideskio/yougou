package cn.lgx.yougou.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

@SuppressWarnings("serial")
public class GoodsBean extends BmobObject {
	
	
	private String number;//���
	private String info;//����
	private Integer salesNum;//����
	private Double price;//�۸�
	private BmobFile goodspng;//ͼƬ
	private String brand;//Ʒ��
	
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
