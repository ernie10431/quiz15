package com.example.quiz15.entity;

import java.io.Serializable;


// 因為 Question 有兩個 PK 所以要另外管理
@SuppressWarnings("serial")
public class QuestionId implements Serializable{

	private int quizId;
	
	private int questionId;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	
}
