package com.aveadvance.advancedachievements.data.entities;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name="user_tasks")
public class UserTask {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_task_seq")
	@SequenceGenerator(name="user_task_seq", sequenceName="user_task_seq", initialValue=1, allocationSize=1)
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
	@OneToOne
	@JoinColumn(name="workspace_id", referencedColumnName="id")
	private Workspace workspace;
	
	@OneToOne
	@JoinColumn(name="user_task_category_id", referencedColumnName="id")
	private UserTaskCategory category;
	
	@NotNull
	@Column(name="state")
	@Enumerated(EnumType.STRING)
	private UserTaskState state;
	
	@NotNull
	@Column(name="creation_date")
	private LocalDateTime creationDate;
	
	public UserTask() {}

	public UserTask(Workspace workspace, String title, String description, Priority priority, UserAccount owner
			, UserTaskState state, LocalDateTime creationDate) {
		this(0, workspace, title, description, priority, owner, null, state, creationDate);
	}

	public UserTask(Workspace workspace, String title, String description, Priority priority, UserAccount owner
			, UserTaskCategory category, UserTaskState state, LocalDateTime creationDate) {
		this(0, workspace, title, description, priority, owner, category, state, creationDate);
	}

	public UserTask(long id, Workspace workspace, String title, String description, Priority priority, UserAccount owner
			, UserTaskCategory category, UserTaskState state, LocalDateTime creationDate) {
		this.id = id;
		this.workspace = workspace;
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.owner = owner;
		this.category = category;
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
	
	public Workspace getWorkspace() {
		return workspace;
	}

	public Optional<UserTaskCategory> getCategory() {
		return Optional.ofNullable(category);
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public UserTaskState getState() {
		return state;
	}

}
