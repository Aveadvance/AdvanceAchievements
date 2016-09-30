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

@Entity
@Table(name="workspaces")
public class Workspace {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="workspace_seq")
	@SequenceGenerator(name="workspace_seq", sequenceName="workspace_seq", initialValue=1, allocationSize=1)
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

	public Set<UserAccount> getUserAccounts() {
		return userAccounts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
//		result = prime * result + ((userAccounts == null) ? 0 : userAccounts.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Workspace other = (Workspace) obj;
		if (id != other.id)
			return false;
		if (type != other.type)
			return false;
//		if (userAccounts == null) {
//			if (other.userAccounts != null)
//				return false;
//		} else if (!userAccounts.equals(other.userAccounts))
//			return false;
		return true;
	}
	
}
