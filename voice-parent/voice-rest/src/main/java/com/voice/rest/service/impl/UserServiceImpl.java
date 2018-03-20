package com.voice.rest.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucpalm.voice.db.dao.test.UserMapper;
import com.voice.rest.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userDao;
	
	
	public Map<String, Object> getUser(){
		return userDao.getUser();
	}
}
