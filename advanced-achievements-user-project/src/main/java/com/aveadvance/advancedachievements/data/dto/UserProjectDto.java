package com.aveadvance.advancedachievements.data.dto;

import javax.validation.constraints.NotNull;

public class UserProjectDto {
	
	private long id;
	
	@NotNull
	private String name;
	
	private String description;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
