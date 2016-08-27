package com.advanceachievements.data.services;

import java.util.Optional;

import com.advanceachievements.data.entities.UserTaskCategory;

public interface UserTaskCategoryService {

	public long create(String name);

	public Optional<UserTaskCategory> retrieve(long id);

}
