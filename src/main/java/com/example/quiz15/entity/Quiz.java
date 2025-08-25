package com.example.quiz15.entity;

import java.time.LocalDate;

import com.example.quiz15.constants.ConstantsMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "quiz")
public class Quiz {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	@NotBlank(message = "error!!")
	private String name;

	@Column(name = "description")
	@NotBlank(message = "error!!")
	private String description;

	@Column(name = "start_time")
	@NotNull(message = "error!!")
	private LocalDate startTime;

	@Column(name = "end_time")
	@NotNull(message = "error!!")
	private LocalDate endTime;

	@Column(name = "is_published")
	private boolean published;

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

}
