package com.aveadvance.advancedachievements.data.dto;

public class ResponseDto<E> {
	
	private String status;
	
	private E entity;

	public ResponseDto() {}

	public ResponseDto(String status, E entity) {
		this.status = status;
		this.entity = entity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public E getEntity() {
		return entity;
	}

	public void setEntity(E entity) {
		this.entity = entity;
	}

}
