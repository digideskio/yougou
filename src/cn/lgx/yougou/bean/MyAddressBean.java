package cn.lgx.yougou.bean;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class MyAddressBean extends BmobObject {
	
	private String uid;//用户ID
	private String name;//收货人名字
	private String phone;//收获人地址
	private String info;//收获地址详情
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
}
