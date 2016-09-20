package com.aveadvance.advancedachievements.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
	
	@NotNull
	@OneToOne
	@JoinColumn(name="workspace_id", referencedColumnName="id")
	private Workspace workspace;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="user_task_category_id", referencedColumnName="id")
	private List<UserTask> userTasks = new ArrayList<>();
	
	public UserTaskCategory() {}

	public UserTaskCategory(String name, Workspace workspace) {
		this.name = name;
		this.workspace = workspace;
	}

	public UserTaskCategory(long id, String name, Workspace workspace, List<UserTask> userTask) {
		this.id = id;
		this.name = name;
		this.workspace = workspace;
		this.userTasks = userTask;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}
	
	public List<UserTask> getUserTasks() {
		return userTasks;
	}
}
