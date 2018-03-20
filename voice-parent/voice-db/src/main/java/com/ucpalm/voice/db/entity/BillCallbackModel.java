package com.voice.db.entity;

/**
 * 
 * @author xupiao 2017年8月16日
 *
 */
public class BillCallbackModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4709667519492306256L;

	private String event;
	private String userId;
	private String productType;
	private String callId;
	private String caller;
	private String called;
	private String beginTime;
	private String endTime;
	private String callStatus;
	private String callType;
	private String recordType;
	private String callTime;
	private String deductionUnit;
	private String cdr_type;
	private Long callPayMoney;
	private Long recordPayMoney;
	private Long payMoney;
	private String callStatusB;
	private String beginTimeB;
	private String endTimeB;
	private String callTimeB;
	private String callerDisplay;
	private String calledDisplay;
	private String realityNumber;
	private String recordUrl;
	private String callTypeB;
	private String message;
	private String userData;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getCalled() {
		return called;
	}

	public void setCalled(String called) {
		this.called = called;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public String getDeductionUnit() {
		return deductionUnit;
	}

	public void setDeductionUnit(String deductionUnit) {
		this.deductionUnit = deductionUnit;
	}

	public String getCdr_type() {
		return cdr_type;
	}

	public void setCdr_type(String cdr_type) {
		this.cdr_type = cdr_type;
	}

	public Long getCallPayMoney() {
		return callPayMoney;
	}

	public void setCallPayMoney(Long callPayMoney) {
		this.callPayMoney = callPayMoney;
	}

	public Long getRecordPayMoney() {
		return recordPayMoney;
	}

	public void setRecordPayMoney(Long recordPayMoney) {
		this.recordPayMoney = recordPayMoney;
	}

	public Long getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Long payMoney) {
		this.payMoney = payMoney;
	}

	public String getCallStatusB() {
		return callStatusB;
	}

	public void setCallStatusB(String callStatusB) {
		this.callStatusB = callStatusB;
	}

	public String getBeginTimeB() {
		return beginTimeB;
	}

	public void setBeginTimeB(String beginTimeB) {
		this.beginTimeB = beginTimeB;
	}

	public String getEndTimeB() {
		return endTimeB;
	}

	public void setEndTimeB(String endTimeB) {
		this.endTimeB = endTimeB;
	}

	public String getCallTimeB() {
		return callTimeB;
	}

	public void setCallTimeB(String callTimeB) {
		this.callTimeB = callTimeB;
	}

	public String getCallerDisplay() {
		return callerDisplay;
	}

	public void setCallerDisplay(String callerDisplay) {
		this.callerDisplay = callerDisplay;
	}

	public String getCalledDisplay() {
		return calledDisplay;
	}

	public void setCalledDisplay(String calledDisplay) {
		this.calledDisplay = calledDisplay;
	}

	public String getRealityNumber() {
		return realityNumber;
	}

	public void setRealityNumber(String realityNumber) {
		this.realityNumber = realityNumber;
	}

	public String getRecordUrl() {
		return recordUrl;
	}

	public void setRecordUrl(String recordUrl) {
		this.recordUrl = recordUrl;
	}

	public String getCallTypeB() {
		return callTypeB;
	}

	public void setCallTypeB(String callTypeB) {
		this.callTypeB = callTypeB;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}
}
