package com.ucpalm.voice.db.dao;

import java.util.Map;


import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
 * 通用dao
 * @author chendi
 *
 */
public interface CommonMapper<T> extends  Mapper<T>,MySqlMapper<T> {

	Map<String, Object>  getUser(@Param(value="userId") String userId);
	
	
	Map<String, Object>  getApplication(@Param(value="userId") String userId,@Param(value="productType") String productType);
	
	
	Map<String, Object>  getBalance(@Param(value="userId") String userId);
	
	Map<String, Object> getNumberRateUserId(@Param(value="userId") String userId,@Param(value="productType") String productType,@Param(value="phoneNumber") String phoneNumber);
	
	Map<String, Object>  getNumberRate(@Param(value="phoneNumber") String phoneNumber,@Param(value="productType") String productType);
	
	
	Long getNumberReSidueUnit(@Param(value="userId") String userId,@Param(value="productType") String productType,@Param(value="phoneNumber") String phoneNumber);
	
	
	Map<String, Object>  getNumInfomation(@Param(value="mobile") String mobile);
	
	Map<String, Object> getNumInfomation_new(@Param(value="mobile") String mobile);
	
	Map<String, Object>  getNumByUserIdAndProductType(@Param(value="userId") String userId,@Param(value="productType") String productType,@Param(value="phoneNumber") String phoneNumber);
	
	Map<String, Object>  getUsersAllPhoneNumber(@Param(value="userId") String userId,@Param(value="productType") String productType);
	
	Map<String, Object>  getAllBindByDstVirtaulNum(@Param(value="userId") String userId,@Param(value="dstVirtualNum") String dstVirtualNum);
	
	String  getBindOrderStatusUrl(@Param(value="subid")  String subid);
	
	String  getBindOrderBillUrl(@Param(value="subid")  String subid);
	
	String  getBindOrderRecordUrl(@Param(value="subid")  String subid);
	
	Map<String, Object> getBindOrder(@Param(value="subid")  String subid);
	
	Map<String, Object> getBindOrderByNumber(@Param(value="virtualNumber")  String virtualNumber);
	
	Map<String, Object> getCallBackUrl(@Param(value="userId") String userId,@Param(value="productType") String productType);
	
	String  getAppBillUrl(@Param(value="userId") String userId,@Param(value="productType") String productType);
	
	
	String  getNumberCityId(@Param(value="phoneNumber") String phoneNumber);
	
	
	String  getTemplateContent(@Param(value="id") String templateId,@Param(value="userId") String userId);
	
	Integer getBlackMobile(@Param(value="mobile") String mobile);
	
	Map<String, Object> selectAllRecordCallback();
	
	int insertDeduction();
	
	int updateBalance();
	
	int updateDeductionStatus();
}
