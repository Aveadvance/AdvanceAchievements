package com.advanceachievements.data.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name="user_accounts")
public class UserAccount {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_accounts_seq")
	@SequenceGenerator(name="user_accounts_seq", sequenceName="user_accounts_seq", allocationSize=1)
	private long id;

	@NotNull
	@NotBlank
	@Email(regexp=".*\\@.*\\..*")
	@Column(name="email", unique=true)
	private String email;

	@NotNull
	@NotBlank
	@Size(min=5, max=50)
	@Column(name="password")
	private String password;
	
	@NotNull
	@Column(name="creation_date")
	private LocalDateTime creationDate;
	
	@NotNull
	@ElementCollection
	@Enumerated(EnumType.STRING)
	@Column(name="authority")
	@CollectionTable(name="user_account_authorities", joinColumns=@JoinColumn(name="id")
		, uniqueConstraints = @UniqueConstraint(columnNames={"id", "authority"}))
	private Set<Authority> authorities = new HashSet<>();
	
	public UserAccount() {}

	public UserAccount(String email, String password) {
		this.email = email;
		this.password = password;
		this.authorities.add(Authority.ROLE_USER);
		this.creationDate = LocalDateTime.now();
	}

	public UserAccount(String email, String password, Set<Authority> authorities) {
		this.email = email;
		this.password = password;
		this.authorities = new HashSet<>(authorities);
		this.creationDate = LocalDateTime.now();
	}

	public long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

}
