package cn.lgx.yougou.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

@SuppressWarnings("serial")
public class UserBean extends BmobUser {
	
	private String sex;//�û��Ա�
	private Integer age;//�û�����
	private String brithday;//�û�����
	private Double yue;//�û����
	private List<MyAddressBean> addresses;//�û��ջ��ַ����
	private List<String> lovegoodsId;//�û��ղ���Ʒ��ID����
	public List<String> getLovegoodsId() {
		
		if (lovegoodsId==null) {
			lovegoodsId = new ArrayList<>();
		}
		return lovegoodsId;
	}
	public void setLovegoodsId(List<String> lovegoodsId) {
		this.lovegoodsId = lovegoodsId;
	}
	public List<MyAddressBean> getAddresses() {
		if (addresses==null) {
			addresses = new ArrayList<>();
		}
		return addresses;
	}
	public void setAddresses(List<MyAddressBean> addresses) {
		this.addresses = addresses;
	}
	public Double getYue() {
		return yue;
	}
	public void setYue(double d) {
		this.yue = d;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getBrithday() {
		return brithday;
	}
	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}
	
	
	
}
