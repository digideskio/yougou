package cn.lgx.yougou.bean;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class OrderBean extends BmobObject {
	
	private String orderinfo;//��Ʒ������
	private String size;//��Ʒ�ߴ�
	private String color;//��Ʒ��ɫ
	private Double price;//��Ʒ�۸�
	private String address;//�ջ��ַ
	private String goodsId;//��ƷID
	private String userid;//�û�ID
	private Boolean ispinglun;//�Ƿ�������
	private Boolean isshouhuo;//�Ƿ����ջ�
	private Integer number;//��������
	private String fileUrl;//��ƷͼƬ��Url
	
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
