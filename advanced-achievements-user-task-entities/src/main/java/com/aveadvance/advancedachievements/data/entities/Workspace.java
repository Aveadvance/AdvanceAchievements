package com.aveadvance.advancedachievements.data.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.advanceachievements.data.entities.UserAccount;

@Entity
@Table(name="workspaces")
public class Workspace {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="workspace_seq")
	@SequenceGenerator(name="workspace_seq", sequenceName="workspace_seq")
	private long id;
	
	@Enumerated(value=EnumType.STRING)
	@Column(name="type")
	private WorkspaceType type;
	
	@ManyToMany
	@JoinTable(name="user_account_workspace", joinColumns=@JoinColumn(name="user_account_id")
		, inverseJoinColumns=@JoinColumn(name="workspace_id"))
	private Set<UserAccount> userAccounts;
	
	public Workspace() {}
	
	public Workspace(WorkspaceType type, Set<UserAccount> userAccounts) {
		this.type = type;
		this.userAccounts = userAccounts;
	}

	public long getId() {
		return id;
	}

	public WorkspaceType getType() {
		return type;
	}
	
}
