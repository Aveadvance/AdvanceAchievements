package com.aveadvance.advancedachievements.data.services;

import java.util.List;
import java.util.Optional;

import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;

public interface UserTaskCategoryService {

	public void create(long workspaceId, String name);

	public Optional<UserTaskCategory> retrieve(long workspaceId, long id);

	public List<UserTaskCategory> retrieveAll(long workspaceId);

	public void update(long workspaceId, long id, String name);

	public void delete(long workspaceId, long id);

}
