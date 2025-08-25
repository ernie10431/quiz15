package com.example.quiz15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz15.service.ifs.QuizSrevice;
import com.example.quiz15.vo.BasicRes;
import com.example.quiz15.vo.QuizCreateReq;
import com.example.quiz15.vo.QuizUpdateReq;

import jakarta.validation.Valid;

@CrossOrigin //跨域請求
@RestController
public class QuestionServiceController {
	
	@Autowired
	private QuizSrevice quizService;

	@PostMapping("quiz/create")
	public ResponseEntity<String> create(@Valid @RequestBody QuizCreateReq req) throws Exception {
		BasicRes res = quizService.create(req);
		if(res.getCode() == 200) {
			return ResponseEntity.ok(res.getMessage());
		}
		return ResponseEntity.badRequest().body(res.getMessage());
	}
	
	@PostMapping("quiz/update")
	public BasicRes update(@Valid @RequestBody QuizUpdateReq req) throws Exception {
		return quizService.update(req);
	}
}
