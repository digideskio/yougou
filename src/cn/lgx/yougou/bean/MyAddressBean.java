package cn.lgx.yougou.bean;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class MyAddressBean extends BmobObject {
	
	private String uid;//�û�ID
	private String name;//�ջ�������
	private String phone;//�ջ��˵�ַ
	private String info;//�ջ��ַ����
	
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
