package com.aveadvance.advancedachievements.data.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.advanceachievements.data.entities.UserAccount;

@Entity
@Table(name="user_tasks")
public class UserTask {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_task_seq")
	@SequenceGenerator(name="user_task_seq", sequenceName="user_task_seq")
	private long id;
	
	@NotNull
	@NotBlank
	@Column(name="title")
	private String title;
	
	@Column(name="description")
	private String description;
	
	@NotNull
	@Column(name="priority")
	@Enumerated(value=EnumType.STRING)
	private Priority priority;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="owner_user_account_id", referencedColumnName="id")
	private UserAccount owner;
	
	@NotNull
	@Column(name="state")
	@Enumerated(EnumType.STRING)
	private UserTaskState state;
	
	@NotNull
	@Column(name="creation_date")
	private LocalDateTime creationDate;
	
	public UserTask() {}

	public UserTask(String title, String description, Priority priority, UserAccount owner
			, UserTaskState state, LocalDateTime creationDate) {
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.owner = owner;
		this.state = state;
		this.creationDate = creationDate;
	}
	
	public long getId() {
		return id;
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

	public UserAccount getOwner() {
		return owner;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public UserTaskState getState() {
		return state;
	}

}
