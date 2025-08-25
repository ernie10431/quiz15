package com.example.quiz15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz15.service.ifs.QuizSrevice;
import com.example.quiz15.service.ifs.UserService;
import com.example.quiz15.vo.AddInfoReq;
import com.example.quiz15.vo.BasicRes;
import com.example.quiz15.vo.LoginReq;
import com.example.quiz15.vo.SearchReq;
import com.example.quiz15.vo.SearchRes;

import jakarta.validation.Valid;

/**
 * @CrossOrigin </br>
 * 可提供跨域資源共享的請求,雖然前後端都用自己同一台電腦</br>
 * 
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class QuizController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private QuizSrevice quizSrevice;
	
	
	
	@PostMapping(value = "user/addInfo") // 接口
	public BasicRes addInfo(@Valid @RequestBody AddInfoReq req) {
		return userService.addInfo(req);
	}
	
	@PostMapping(value = "user/login") // 接口
	public BasicRes login(@Valid @RequestBody LoginReq req) {
		return userService.login(req);
	}
	
	@PostMapping(value = "user/search") // 接口
	public SearchRes search(@Valid @RequestBody SearchReq req) {
		return quizSrevice.searach(req);
	}
	
//	@PostMapping(value = "user/getAllQuizs") // 接口
//	public SearchRes getAllQuizs(){
//		
//	}
	
}
