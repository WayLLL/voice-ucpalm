package com.voice.rest.common;

import java.io.Serializable;

/**
 * 响应实体类
 * @author chendi
 *
 * @param <T>
 */
public class ResponceBean<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private String message;
	
	private T  data;

	
	public ResponceBean() {
		// TODO Auto-generated constructor stub
	}
	

	public ResponceBean(String code, String message){
		this.code = code;
		this.message = message;
	}
	
	public ResponceBean(String code, String message,T data){
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public String  getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	/**
	 * 请求业务失败
	 * @param message
	 * @return
	 */
	public static ResponceBean fail(String code,String message) {
		return new ResponceBean<>(code, message);
	}
	
	/**
	 * 请求业务失败
	 * @param message
	 * @return
	 */
	public static ResponceBean success(String code,String message) {
		return new ResponceBean<>(code, message);
	}
	
}
