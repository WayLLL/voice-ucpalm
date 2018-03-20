package com.voice.rest.conf;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;

import com.ucpalm.voice.common.util.StringUtil;



/**
 * 配置类
 * 
 * @author xupiao 2017年6月12日
 *
 */
public class ConfigUtils {
	private static final Logger logger = LogManager.getLogger(ConfigUtils.class);

	/**
	 * 禁止实例化工具类 <br/>
	 */
	private ConfigUtils() {
	}

	/**
	 * Java系统环境配置属性键（通过-D配置） <br/>
	 */
	public static final String SYS_CONFIG = "classpath:/config/config.properties";

	public static final Map<String, Object> CONFIG_MAP = new HashMap<String, Object>();

	/**
	 * 配置文件中的全部配置项 <br/>
	 */
	private static Properties props;

	/**
	 * 
	 * @param expr
	 *            符合Ognl表达式规范:<code>a.b.c</code>; <code>b[0].d,b[1].d</code>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(String expr, Class<T> clazz) {
		String[] key = expr.split("\\.");
		Map<String, Object> tmp = CONFIG_MAP;
		if (key.length > 0) {
			for (int i = 0; i < key.length - 1; i++) {
				if (!tmp.containsKey(key[i])) {
					return null;
				}
				tmp = (Map<String, Object>) tmp.get(key[i]);
			}
			if (!tmp.containsKey(key[key.length - 1])) {
				return null;
			}
			if (clazz.isAssignableFrom(Integer.class)) {
				return (T) Integer.valueOf((String) tmp.get(key[key.length - 1]));
			} else if (clazz.isAssignableFrom(Long.class)) {
				return (T) Long.valueOf((String) tmp.get(key[key.length - 1]));
			} else {
				return (T) tmp.get(key[key.length - 1]);
			}
		}
		return null;
	}

	public static <T> T getProperty(String expr, T defaultValue, Class<T> clazz) {
		T o = getProperty(expr, clazz);
		if (StringUtil.isEmpty(o)) {
			return defaultValue;
		}
		return o;
	}
}
