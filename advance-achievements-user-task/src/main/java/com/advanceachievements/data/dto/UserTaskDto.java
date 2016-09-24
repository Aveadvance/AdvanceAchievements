package com.advanceachievements.data.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.aveadvance.advancedachievements.data.entities.Priority;

public class UserTaskDto {
	
	private long id;
	
	@NotNull
	@NotBlank
	private String title;
	
	private String description;
	
	@NotNull
	private Priority priority;
	
	private long userTaskCategoryId;

	public UserTaskDto() {}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public long getUserTaskCategoryId() {
		return userTaskCategoryId;
	}

	public void setUserTaskCategoryId(long userTaskCategoryId) {
		this.userTaskCategoryId = userTaskCategoryId;
	}

}
