package com.advanceachievements.data.services;

import java.util.List;
import java.util.Optional;

import com.advanceachievements.data.entities.Priority;
import com.advanceachievements.data.entities.UserTask;

public interface UserTaskService {

	Optional<UserTask> retrieve(long id);

	boolean create(String title, String description, Priority priority);

	List<UserTask> retrieve(String email);

}
