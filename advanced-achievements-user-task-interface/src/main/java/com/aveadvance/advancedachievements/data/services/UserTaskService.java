package com.aveadvance.advancedachievements.data.services;

import java.util.List;
import java.util.Optional;

import com.aveadvance.advancedachievements.data.entities.Priority;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskState;

public interface UserTaskService {

	Optional<UserTask> retrieve(long workspaceId, long id);

	List<UserTask> retrieve(String email, long workspaceId);
	
	List<UserTask> retrieve(String email, long workspaceId, UserTaskState state);

	boolean create(long workspaceId, String title, String description, Priority priority);

	boolean create(long workspaceId, String title, String description, Priority priority, long userTaskCategoryId);
	
	void delete(long workspaceId, long id);

	void update(long workspaceId, long id, String title, String description, Priority priority);

	void completeTask(long workspaceId, long id);

}
