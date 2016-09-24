package com.advanceachievements.data.services;

import java.util.List;
import java.util.Optional;

import com.aveadvance.advancedachievements.data.entities.Priority;
import com.aveadvance.advancedachievements.data.entities.UserTask;

public interface UserTaskService {

	Optional<UserTask> retrieve(long id);

	List<UserTask> retrieve(String email, long workspaceId);

	boolean create(long workspaceId, String title, String description, Priority priority);

	boolean create(long workspaceId, String title, String description, Priority priority, long userTaskCategoryId);
	
	void delete(long workspaceId, long id);

	void update(long workspaceId, long id, String title, String description, Priority priority);

}
