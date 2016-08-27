package com.advanceachievements.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name="user_task_categories")
public class UserTaskCategory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_task_category_seq")
	@SequenceGenerator(name="user_task_category_seq", sequenceName="user_task_category_seq")
	private long id;
	
	@NotNull
	@NotBlank
	@Column(name="name")
	private String name;
	
	public UserTaskCategory() {}

	public UserTaskCategory(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
