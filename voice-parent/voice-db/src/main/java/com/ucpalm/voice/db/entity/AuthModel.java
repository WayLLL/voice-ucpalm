package com.ucpalm.voice.db.entity;

/**
 * 
 * @author xupiao 2017年8月16日
 *
 */
public class AuthModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4709667519492306256L;

	private String sign;
	
	private String token;
	private Long timestamp ;
	private String userID;
	private String productType;
	private String ipWhiteList;
	private String phoneNum;
	private String callID;
	private String needBalance; // 是否需要校验余额，0：是，1：否
	private String userData;
	private String caller;
	private String callee;

	

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getCallee() {
		return callee;
	}

	public void setCallee(String callee) {
		this.callee = callee;
	}

	public String getNeedBalance() {
		return needBalance;
	}

	public void setNeedBalance(String needBalance) {
		this.needBalance = needBalance;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getIpWhiteList() {
		return ipWhiteList;
	}

	public void setIpWhiteList(String ipWhiteList) {
		this.ipWhiteList = ipWhiteList;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getCallID() {
		return callID;
	}

	public void setCallID(String callID) {
		this.callID = callID;
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}
}
