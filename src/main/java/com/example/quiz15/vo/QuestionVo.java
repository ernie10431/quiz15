package com.example.quiz15.vo;

import java.util.List;

import com.example.quiz15.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

// 一個 QuestionVo 代表一題
// 不帶問卷的編號是因為問卷編號是自動生成的流水號，所以暫時無法確定其問卷編號是多少
public class QuestionVo {
	
	// 不帶編號是因為，流水號暫時不確定編號是多少
	

	@Min(value = 1,message = ConstantsMessage.QUESTION_ID_ERROR)
	private int questionId;

	@NotBlank(message = ConstantsMessage.QUESTION_ERROR )
	private String question;

	@NotBlank(message = ConstantsMessage.QUESTION_TYPE_ERROR)
	private String type;

	private boolean required;

	// 不檢查，因為簡答題不會有選項
	private List<String> options;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	
	
}
