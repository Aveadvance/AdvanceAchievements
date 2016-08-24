package com.advanceachievements.data.services;

import java.util.Optional;

import com.advanceachievements.data.entities.UserTask;
import com.advanceachievements.data.entities.UserTaskDto;

public interface UserTaskService {

	Optional<UserTask> retrieve(long id);

	boolean create(UserTaskDto userTaskDto);

}
