package com.aveadvance.advancedachievements.data.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
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

@Entity
@Table(name="user_projects")
public class UserProject {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_project_seq")
	@SequenceGenerator(name="user_project_seq", sequenceName="user_project_seq", initialValue=1, allocationSize=1)
	private long id;
	
	@NotNull
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;

	@NotNull
	@OneToOne
	@JoinColumn(name="workspace_id", referencedColumnName="id")
	private Workspace workspace;
	
	@NotNull
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="project_workspace_id", referencedColumnName="id")
	private Workspace projectWorkspace;
	
	@NotNull
	@Column(name="creation_date")
	private LocalDateTime creationDate;
	
	public UserProject() {
		
	}
	
	public UserProject(String name, String description, Workspace workspace, Workspace projectWorkspace) {
		this(0, name, description, workspace, projectWorkspace, LocalDateTime.now());
	}
	
	public UserProject(long id, String name, String description, Workspace workspace
			, Workspace projectWorkspace, LocalDateTime creationDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.workspace = workspace;
		this.projectWorkspace = projectWorkspace;
		this.creationDate = creationDate;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public Workspace getProjectWorkspace() {
		return projectWorkspace;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
}
