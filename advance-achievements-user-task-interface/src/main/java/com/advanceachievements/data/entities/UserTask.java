package com.advanceachievements.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="user_tasks")
public class UserTask {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_task_seq")
	@SequenceGenerator(name="user_task_seq", sequenceName="user_task_seq")
	private long id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="description")
	private String description;
	
	@Column(name="priority")
	@Enumerated(value=EnumType.STRING)
	private Priority priority;

	public UserTask(String title, String description, Priority priority) {
		this.title = title;
		this.description = description;
		this.priority = priority;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Priority getPriority() {
		return priority;
	}

}
