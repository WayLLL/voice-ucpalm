package com.voice.rest.controller.rest;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;
import com.ucpalm.voice.common.gloable.EnumType.BusiErrorCode;
import com.ucpalm.voice.common.util.HttpUtils;
import com.ucpalm.voice.common.util.JsonUtil;
import com.ucpalm.voice.common.util.StringUtil;
import com.voice.db.dao.CommonMapper;
import com.voice.db.entity.AuthModel;
import com.voice.db.entity.Voice4ZHModel;
import com.voice.db.entity.VoiceNotifyModel;
import com.voice.rest.common.ResponceBean;
import com.voice.rest.conf.ConfigUtils;
import com.voice.rest.util.CommonUtils;

/***
 * 语音通知接口
 * @author chendi
 *
 */
@RestController
public class VoiceNotifyController {

	private Logger logger = LoggerFactory.getLogger(VoiceNotifyController.class);
	
	@Autowired
	private CommonMapper commonDao;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("{version}/voice/notify/{userId}")
	public ResponceBean voiceNotifyApi(@PathVariable(name="version") String  version,@PathVariable(name="userId") String  userIdStr,@RequestBody String paramsStr) {
		final String callId = UUID.randomUUID().toString().replace("-", "");

		
		// 解析header字段获取Authorization字段
		HttpServletRequest httpRequest = HttpUtils.getRequest();

		// 获取signature字段
		String signature = httpRequest.getHeader("sign");

		// 获取主账户ID
		String userId = ObjectUtils.defaultIfNull(userIdStr, "");

		// 获取客户端的IP地址
		String clientIp = HttpUtils.getClientIp(httpRequest);

		VoiceNotifyModel voiceNotifyModel = JsonUtil.fromJson(paramsStr, new TypeToken<VoiceNotifyModel>() {
		}.getType());
		
		voiceNotifyModel.setUserId(userId);

		String userData = voiceNotifyModel.getUserData();
		String caller = voiceNotifyModel.getCaller();
		String callee = voiceNotifyModel.getCallee();
		Integer playTimes = voiceNotifyModel.getPlayTimes();
		String content = voiceNotifyModel.getContent();

		if (StringUtil.isNotEmpty(userData) && userData.length() > 128) {
			userData = userData.substring(0, 128);
			voiceNotifyModel.setUserData(userData.substring(0, 128));
		}

		// 主叫号码非空校验
		if (StringUtil.isBlank(caller)) {
			return ResponceBean.fail(BusiErrorCode.B_100020.getErrCode(), BusiErrorCode.B_100020.getErrMsg());
		}
		// 判断主叫号码
		caller = CommonUtils.getSimplePhone(caller);
		if (!CommonUtils.isPhoneNum(caller)) {
			return ResponceBean.fail(BusiErrorCode.B_100021.getErrCode(), BusiErrorCode.B_100021.getErrMsg());
		}
		// 被叫号码非空校验
		if (StringUtil.isBlank(callee)) {
			return ResponceBean.fail(BusiErrorCode.B_100020.getErrCode(), BusiErrorCode.B_100020.getErrMsg());
		}
		// 判断被叫号码
		callee = CommonUtils.getSimplePhone(callee);
		if (!CommonUtils.isPhoneNum(callee)) {
			return ResponceBean.fail(BusiErrorCode.B_100021.getErrCode(), BusiErrorCode.B_100021.getErrMsg());
		}
		if (null == playTimes || 0 == playTimes) {
			playTimes = 1;
		} else {
			if (playTimes > 4) {
				playTimes = 4;
			}
			if (playTimes < 1) {
				playTimes = 1;
			}
		}
		voiceNotifyModel.setPlayTimes(playTimes);
		if ("1".equals(voiceNotifyModel.getType())) {
			if (StringUtils.isEmpty(voiceNotifyModel.getTemplateId())) {
				return ResponceBean.fail(BusiErrorCode.B_100029.getErrCode(), BusiErrorCode.B_100029.getErrMsg());
			}
		}
		//语音通知内容不能为空
		if (StringUtils.isEmpty(content)) {
			return ResponceBean.fail(BusiErrorCode.B_100030.getErrCode(), BusiErrorCode.B_100030.getErrMsg());
		}
		//语音通知长度不能超过200个字节
		if (content.length() > 200) {
			return ResponceBean.fail(BusiErrorCode.B_100031.getErrCode(), BusiErrorCode.B_100031.getErrMsg());
		}
		// 请求公共鉴权组件
		AuthModel authModel = new AuthModel();
		authModel.setSign(signature);
		authModel.setCallID(callId);
		authModel.setIpWhiteList(clientIp);
		authModel.setPhoneNum(voiceNotifyModel.getCaller());
		authModel.setProductType("4"); // 语音通知
		authModel.setUserID(userId);
		authModel.setNeedBalance("0");
		authModel.setCaller(caller);
		authModel.setCallee(callee);

		String authStr = JsonUtil.toJsonStr(authModel);
		String authUrl = ConfigUtils.getProperty("caas_auth_url", String.class) + "/voiceAuth/caasCalls";
		logger.info("请求caas-auth组件安全鉴权包体信息authStr={},authUrl={}", authStr, authUrl);
		//鉴权组件返回的结果
		String authResult =  HttpUtils.sendPostJson(authUrl, authStr);
		//转换
		ResponceBean<String>  response = JsonUtil.fromJson(authResult, new TypeToken<ResponceBean>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		}.getType());
		//鉴权成功
		if(BusiErrorCode.B_000000.getErrCode().equals(response.getCode())) {
			
			String controlUrl = ConfigUtils.getProperty("caas_control_url", String.class) + "/control/voiceNotify4ZH";
			Voice4ZHModel vc = new Voice4ZHModel();

			vc.setAppid(ConfigUtils.getProperty("voiceCode_zh_appid", String.class));
			vc.setCalled(voiceNotifyModel.getCallee());
			vc.setCalling(voiceNotifyModel.getCaller());
			String templateContent = commonDao.getTemplateContent(voiceNotifyModel.getTemplateId(), userId);
			Map<String, Object> contentParams = JsonUtil.jsonStrToMap(content);
			/*GenericTokenParserUtil parser = new GenericTokenParserUtil("{", "}", new TokenHandler() {
				@Override
				public String handleToken(String content) {
					return String.valueOf(contentParams.get(content));
				}
			});
			templateContent = parser.parse(templateContent);*/
			vc.setExtkey2(templateContent);
			vc.setExtparam(callId);
			vc.setRepeat(String.valueOf(voiceNotifyModel.getPlayTimes()));
			vc.setServiceid(ConfigUtils.getProperty("voiceNotify_serviceId", String.class));
			vc.setTid(ConfigUtils.getProperty("voiceNotify_tid", String.class));
			vc.setUrl(ConfigUtils.getProperty("voiceNotify_callback_url", String.class));
			
			//RedisOpClient.set(RedisKeyConsts.getKey(RedisKeyConsts.VOICE_NOTIFY_SESSION, callId), JsonUtil.toJsonStr(voiceNotifyModel));
			return response;
		}else {
			return response;
		}
	}
}
