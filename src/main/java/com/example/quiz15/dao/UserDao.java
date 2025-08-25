package com.example.quiz15.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.example.quiz15.entity.User;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserDao extends JpaRepository<User, String> {

	@Query(value ="Select count(email) from user where email = ?1", nativeQuery = true)
	public int getCountByEmail(String email);
	
	@Transactional
	@Modifying
	@Query(value ="insert into user(name, phone, email, age, password) values "
			+ " (?1, ?2, ?3, ?4, ?5) ", nativeQuery = true)
	public int addInfo(String name,String phone,String email,int age,String password);

	@Query(value = "select * from user where email = ?1;", nativeQuery = true)
	public User login(String account);
}

