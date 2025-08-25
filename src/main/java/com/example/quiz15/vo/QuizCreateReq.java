package com.example.quiz15.vo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.quiz15.constants.ConstantsMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// 一個 QuizCreateReq 表示一張問卷
// 不帶問卷的編號是因為問卷編號是自動生成的流水號
public class QuizCreateReq {

	@NotBlank(message = ConstantsMessage.QUIZ_NAME_ERROR)
	private String name;

	@NotBlank(message = ConstantsMessage.QUIZ_DESCRIPTION_ERROR)
	private String description;

	@NotNull(message = ConstantsMessage.QUIZ_START_DATE_ERROR)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startTime;

	@NotNull(message = ConstantsMessage.QUIZ_END_DATE_ERROR)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endTime;

	private boolean publish;

	// 同一張問卷可能有多個問題
	@Valid // 嵌套驗證: QuestionVo 也有使用 Validation 驗證，所以要加上 @Valid 使其生效
	@NotEmpty(message = ConstantsMessage.QUESTION_VO_ERROR)
	private List<QuestionVo> questionList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDate startTime) {
		this.startTime = startTime;
	}

	public LocalDate getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDate endTime) {
		this.endTime = endTime;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	public List<QuestionVo> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<QuestionVo> questionList) {
		this.questionList = questionList;
	}

	
}
