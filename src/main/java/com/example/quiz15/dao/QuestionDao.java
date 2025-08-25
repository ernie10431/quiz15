package com.example.quiz15.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz15.entity.Question;
import com.example.quiz15.entity.QuestionId;

import jakarta.transaction.Transactional;

@Repository
public interface QuestionDao extends JpaRepository<Question, QuestionId>{

	@Transactional
	@Modifying
	@Query(value = "insert into question(quiz_id,question_id, "//
			+ " question,type,required,options) values " //
			+ " (?1,?2,?3,?4,?5,?6); ", nativeQuery = true)
	public void insert(int quizId,int questionId, String question, String type,//
			boolean isRequired, String options);
	
	@Transactional
	@Modifying
	@Query(value = "delete from question where quiz_id = ?1 ;", nativeQuery = true)
	public void deleteByQuizId(int quizId);
}
