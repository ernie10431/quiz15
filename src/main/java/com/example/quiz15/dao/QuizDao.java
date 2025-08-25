package com.example.quiz15.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz15.entity.Quiz;

import jakarta.transaction.Transactional;

@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer>{

	@Transactional
	@Modifying
	@Query(value = "insert into quiz(name,description,start_time,end_time,is_published) values " //
			+ " (?1,?2,?3,?4,?5); ", nativeQuery = true)
	public void insert( String name, String description, LocalDate startTime,//
			LocalDate endTime, boolean published);
	
	@Query(value = "select max(id) from quiz", nativeQuery = true)
	public int getLatestQuizId();
	
	@Query(value = "select count(id) from quiz where id = ?1", nativeQuery = true)
	public int getCountByQuizId(int quizId);
	
	/**
	 * 回傳型態要設 int 主要用來判斷資料是否有成功更新，<br>
	 * int = 1 代表成功，int = 0 代表失敗
	 */
	@Transactional
	@Modifying
	@Query(value = "update quiz set name = ?2, description = ?3, start_time = ?4, "//
			+ " end_time = ?5, is_published = ?6 where id = ?1 ;", nativeQuery = true)
	public int update(int id,String name, String description, LocalDate startTime,//
			LocalDate endTime, boolean published);
	
	@Query(value = "selet * from quiz;", nativeQuery = true)
	public List<Quiz> getAll();
	
	@Query(value = "selet * from quiz where name like %?1% and start_time >= ?2 "//
			+ " and end_time <= ?3;", nativeQuery = true)
	public List<Quiz> getAll(String name, LocalDate starDate,LocalDate endDate);
	
	// is_published = true 可以換成 is_published is true，等號跟 is 互換只能用在 boolean
	@Query(value = "selet * from quiz where name like %?1% and start_time >= ?2 "//
			+ " and end_time <= ?3 and is_published is true;", nativeQuery = true)
	public List<Quiz> getAllPublished(String name, LocalDate starDate,//
			LocalDate endDate);
	
	@Transactional
	@Modifying
	@Query(value = "delete from quiz where id = ?1;", nativeQuery = true)
	public void deleteById(int Id);
}
