package com.voice.db.entity;

import java.util.List;

import com.voice.db.entity.huawei.CallStatusInfo;
import com.voice.db.entity.huawei.FeeInfo;


/**
 * 
 * @author xupiao 2017年11月2日
 *
 */
public class HuaweiClickCallBillModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1013072076113323L;

	private String eventType;
	private CallStatusInfo statusInfo;
	private List<FeeInfo> feeLst;

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public CallStatusInfo getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(CallStatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}

	public List<FeeInfo> getFeeLst() {
		return feeLst;
	}

	public void setFeeLst(List<FeeInfo> feeLst) {
		this.feeLst = feeLst;
	}

}
