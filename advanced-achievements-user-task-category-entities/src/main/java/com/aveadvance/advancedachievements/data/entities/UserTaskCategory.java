package com.aveadvance.advancedachievements.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	@SequenceGenerator(name="user_task_category_seq", sequenceName="user_task_category_seq", initialValue=1, allocationSize=1)
	private long id;
	
	@NotNull
	@NotBlank
	@Column(name="name")
	private String name;
	
	@NotNull
	@OneToOne
	@JoinColumn(name="workspace_id", referencedColumnName="id")
	private Workspace workspace;
	
	public UserTaskCategory() {}

	public UserTaskCategory(String name, Workspace workspace) {
		this.name = name;
		this.workspace = workspace;
	}

	public UserTaskCategory(Workspace workspace, long id, String name) {
		this.id = id;
		this.name = name;
		this.workspace = workspace;
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
}
