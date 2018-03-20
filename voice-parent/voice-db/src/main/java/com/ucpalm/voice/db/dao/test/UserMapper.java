package com.ucpalm.voice.db.dao.test;

import java.util.Map;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;




public interface UserMapper<T> extends Mapper<T> ,MySqlMapper<T>{
	/**
	 * 测试mybaitis功能是否正常
	 * @return
	 */
	public Map<String, Object> getUser();
	
}
