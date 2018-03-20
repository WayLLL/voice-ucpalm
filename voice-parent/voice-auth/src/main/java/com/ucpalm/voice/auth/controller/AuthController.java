package com.ucpalm.voice.auth.controller;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;
import com.ucpalm.voice.auth.common.ResponceBean;
import com.ucpalm.voice.auth.util.ConfigUtils;
import com.ucpalm.voice.common.gloable.EnumType.BusiErrorCode;
import com.ucpalm.voice.common.util.EncryptUtil;
import com.ucpalm.voice.common.util.JsonUtil;
import com.ucpalm.voice.common.util.StringUtil;
import com.voice.db.dao.CommonMapper;
import com.voice.db.entity.AuthModel;

@RestController
public class AuthController {
	
	private Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private CommonMapper commonDao; 
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/voice/voiceAuth")
	public ResponceBean voiceNotifyApi(@RequestBody String paramsStr) {
		
		AuthModel authBean = JsonUtil.fromJson(paramsStr, new TypeToken<AuthModel>() {
		}.getType());
		String userId = authBean.getUserID();
		//检测用户信息是否存在
		Map<String, Object> userInfo = commonDao.getUser(userId);
		if (userInfo == null || userInfo.isEmpty()) {
			return ResponceBean.fail(BusiErrorCode.B_100003.getErrCode(), BusiErrorCode.B_100003.getErrMsg());
		}
		//解密签名
		String publicKey = userInfo.get("publicKey").toString();
		String privateKey = userInfo.get("privateKey").toString();
		String decoderStr = "";
		AuthModel decoderAuth = null;
		try {
			 decoderStr = EncryptUtil.RSADecoder(authBean.getSign(), publicKey, privateKey);
			 logger.info("签名解密结果 :{}", decoderStr);
			 decoderAuth = JsonUtil.fromJson(decoderStr, new TypeToken<AuthModel>() {
				}.getType());
		}catch (Exception e) {
			logger.info("解密失败 失败原因:{}", e.getMessage());
			return ResponceBean.fail(BusiErrorCode.B_100009.getErrCode(), BusiErrorCode.B_100009.getErrMsg());
		}
		// 账户校验：账户是否存在并可用、token是否正确
		if (!"0".equals(userInfo.get("status"))) { // 账号被禁用
			logger.warn("主账户[" + userId + "]状态[" + userInfo.get("status") + "]已禁用！");
			return ResponceBean.fail(BusiErrorCode.B_100004.getErrCode(), BusiErrorCode.B_100004.getErrMsg());
		}
		if (StringUtil.isBlank(userInfo.get("token"))) {
			logger.warn("主账户Token未设置！");
			return ResponceBean.fail(BusiErrorCode.B_100002.getErrCode(), BusiErrorCode.B_100002.getErrMsg());
		}

		if (StringUtils.isBlank(userId)) {
			logger.warn("请求参数中主账户ID为空！");
			return ResponceBean.fail(BusiErrorCode.B_100001.getErrCode(), BusiErrorCode.B_100001.getErrMsg());
		}

		String regex = "^[a-zA-Z0-9]+$";
		boolean is = userId.matches(regex);
		if (!is) {
			logger.warn("主账号[" + userId + "]存在非法字符");
			return ResponceBean.fail(BusiErrorCode.B_100016.getErrCode(), BusiErrorCode.B_100016.getErrMsg());
		}
		//校验签名参数是否一致
		if(!userId.equals(authBean.getUserID())) {
			logger.warn("主账号[" + userId + "] 签名中的账号ID跟请求传递账户ID不一致, 签名中userId 为"+authBean.getUserID());
			return ResponceBean.fail(BusiErrorCode.B_100010.getErrCode(), BusiErrorCode.B_100010.getErrMsg());
		}
		if(userInfo.get("token").equals(authBean.getToken())) {
			logger.warn("主账号[" + userId + "] 签名中Token值校验不正确");
			return ResponceBean.fail(BusiErrorCode.B_100011.getErrCode(), BusiErrorCode.B_100011.getErrMsg());
		}
		
		Long reqTime = decoderAuth.getTimestamp();
		Long timeOut = ConfigUtils.getProperty("auth.request.有效时间", 300L, Long.class);
		Long nowTime = new Date().getTime();
		if ((reqTime + timeOut * 1000) < nowTime || (reqTime - timeOut * 1000) > nowTime) {
			logger.warn("主账号[" + userId + "]参数解码后时间戳过期");
			return ResponceBean.fail(BusiErrorCode.B_100018.getErrCode(), BusiErrorCode.B_100018.getErrMsg());
		}
		// 应用验证：业务是否存在、可用
		Map<String, Object> appInfo = commonDao.getApplication(userId, authBean.getProductType());
		if (appInfo == null || appInfo.isEmpty()) {
			logger.warn("主账户[" + userId + "]的业务[" + authBean.getProductType() + "]未开通！");
			return ResponceBean.fail(BusiErrorCode.B_100006.getErrCode(), BusiErrorCode.B_100006.getErrMsg());
		}
		if (!"0".equals(appInfo.get("status"))) {
			logger.warn("主账户[" + userId + "]的业务[" + authBean.getProductType() + "]状态[" + userInfo.get("status") + "]已禁用！");
			return ResponceBean.fail(BusiErrorCode.B_100007.getErrCode(), BusiErrorCode.B_100007.getErrMsg());
		}

		// 号码黑名单
		if (StringUtil.isNotEmpty(authBean.getCaller())) {
			String mobile = authBean.getCaller();
			int count = commonDao.getBlackMobile(mobile);
			if (count > 0) {
				logger.warn("号码[" + authBean.getCaller() + "]为受保护的号码");
				return ResponceBean.fail(BusiErrorCode.B_100038.getErrCode(), BusiErrorCode.B_100038.getErrMsg());
			}
		}
		if (StringUtil.isNotEmpty(authBean.getCallee())) {
			String mobile = authBean.getCallee();
			int count = commonDao.getBlackMobile(mobile);
			if (count > 0) {
				logger.warn("号码[" + authBean.getCallee() + "]为受保护的号码");
				return ResponceBean.fail(BusiErrorCode.B_100038.getErrCode(), BusiErrorCode.B_100038.getErrMsg());
			}
		}
		// 是否是服务器白名单
		if (StringUtil.isNotEmpty(authBean.getIpWhiteList())) {
			String whiteList = (String) appInfo.get("ipWhiteList");
			boolean flag = false;
			if (StringUtil.isNotEmpty(whiteList)) {
				String[] ipList = whiteList.split(";");
				for (String ip : ipList) {
					if (ip.equals(authBean.getIpWhiteList())) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					logger.warn("ip[" + authBean.getIpWhiteList() + "]不在服务器白名单中！");
					return ResponceBean.fail(BusiErrorCode.B_100008.getErrCode(), BusiErrorCode.B_100008.getErrMsg());
				}
			}
		}
		// 预付费模式下账户余额是否充足
		String userType = (String) userInfo.get("userType"); // 0/预付费用户，1/后付费用户
		if (!"1".equals(userType) && "0".equals(authBean.getNeedBalance())) { // 预付费
			Map<String, Object> balanceMap = commonDao.getBalance(userId);
			long balance = 0L;
			if ("1".equals(balanceMap.get("creditType"))) {
				balance = (long) balanceMap.get("balance") + (long) balanceMap.get("creditMoney");
			} else {
				balance = (long) balanceMap.get("balance");
			}
			if (balance <= 0L) {
				logger.warn("主账户[" + userId + "]的余额[" + balance + "]不足！");
				return ResponceBean.fail(BusiErrorCode.B_100019.getErrCode(), BusiErrorCode.B_100019.getErrMsg());
			}
			logger.debug("余额校验通过");
		} else {
			logger.info("后付费模式不用校验余额");
		}
		return ResponceBean.fail(BusiErrorCode.B_000000.getErrCode(), BusiErrorCode.B_000000.getErrMsg());
	}

}
