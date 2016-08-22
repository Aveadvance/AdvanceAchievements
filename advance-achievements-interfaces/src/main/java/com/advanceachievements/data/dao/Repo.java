package com.advanceachievements.data.dao;

public interface Repo<U, I> {
	
	public void create(U t);
	
	public U retrieve(I id);
	
	public void update(U t);
	
	public void delete(I id);

}
