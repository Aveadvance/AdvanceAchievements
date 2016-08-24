package com.advanceachievements.data.dao;

import java.util.Optional;

public interface Repo<E, I> {
	
	public void create(E entity);
	
	public Optional<E> retrieve(I id);
	
	public void update(E entity);
	
	public void delete(I id);

}
