package com.advanceachievements.data.services;

import java.util.List;
import java.util.Optional;

import com.aveadvance.advancedachievements.data.entities.Priority;
import com.aveadvance.advancedachievements.data.entities.UserTask;

public interface UserTaskService {

	Optional<UserTask> retrieve(long id);

	boolean create(String title, String description, Priority priority, long userTaskCategoryId);

	List<UserTask> retrieve(String email);

	boolean create(String title, String description, Priority priority);

}
