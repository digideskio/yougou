package cn.lgx.yougou.bean;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class PingLunBean extends BmobObject {
	
	private String userid;//评论者ID
	private String goodsid;//评论活动的Id
	private String content;//评论内容
	private String username;//评论用户的名字
	private float rating_num;//评论星级
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public float getRating_num() {
		return rating_num;
	}
	public void setRating_num(float rating_num) {
		this.rating_num = rating_num;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
