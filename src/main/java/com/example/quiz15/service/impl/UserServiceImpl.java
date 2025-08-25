package com.example.quiz15.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.quiz15.constants.ResCodeMassage;
import com.example.quiz15.dao.UserDao;
import com.example.quiz15.entity.User;
import com.example.quiz15.service.ifs.UserService;
import com.example.quiz15.vo.AddInfoReq;
import com.example.quiz15.vo.BasicRes;
import com.example.quiz15.vo.LoginReq;

@Service
public class UserServiceImpl implements UserService {

	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private UserDao userDao;

	@Override
	public BasicRes addInfo(AddInfoReq req) {
		// 檢查參數在 User 類別中透過 Validation 驗證
		// 1.檢查帳號是否已存在
		// 透過 PK email 去取得 count 數，只會是 0 和 1
		int count = userDao.getCountByEmail(req.getEmail());
		if (count == 1) { // email 是 PK， 若 count = 1 表示帳號已存在
			return new BasicRes(ResCodeMassage.EMAIL_EXISTS.getCode(), ResCodeMassage.EMAIL_EXISTS.getMessage());
		}

		// 2.新增資訊
		try {

			userDao.addInfo(req.getName(), req.getPhone(), //
					req.getEmail(), req.getAge(), encoder.encode(req.getPassword()));
			return new BasicRes(ResCodeMassage.SUCCESS.getCode(), ResCodeMassage.SUCCESS.getMessage());
		} catch (Exception e) {
			return new BasicRes(ResCodeMassage.EMAIL_EXISTS.getCode(), ResCodeMassage.EMAIL_EXISTS.getMessage());
		}
	}

	@Override
	public BasicRes login(LoginReq req) {
		try {
			User user = userDao.login(req.getEmail());
			if (user == null) {
				return new BasicRes(ResCodeMassage.NOT_FOUND.getCode(), ResCodeMassage.NOT_FOUND.getMessage());
			}
			if (encoder.matches(req.getPassword(), user.getPassword())) {
				return new BasicRes(ResCodeMassage.SUCCESS.getCode(), ResCodeMassage.SUCCESS.getMessage());
			} else {
				return new BasicRes(ResCodeMassage.PASSWORD_ERROR.getCode(),
						ResCodeMassage.PASSWORD_ERROR.getMessage());
			}
		} catch (Exception e) {
			return new BasicRes(ResCodeMassage.NOT_FOUND.getCode(), ResCodeMassage.NOT_FOUND.getMessage());
		}

	}

}
