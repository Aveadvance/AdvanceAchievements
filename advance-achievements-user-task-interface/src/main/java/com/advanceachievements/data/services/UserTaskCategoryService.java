package com.advanceachievements.data.services;

import java.util.List;
import java.util.Optional;

import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;

public interface UserTaskCategoryService {

	public long create(long workspaceId, String name);

	public Optional<UserTaskCategory> retrieve(long id);

	public List<UserTaskCategory> retrieveAll(long id);

	public void update(UserTaskCategory userTaskCategory);

}
