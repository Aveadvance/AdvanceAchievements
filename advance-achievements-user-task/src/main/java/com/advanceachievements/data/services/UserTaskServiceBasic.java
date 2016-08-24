package com.advanceachievements.data.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advanceachievements.data.dao.UserTaskDao;
import com.advanceachievements.data.entities.UserTask;
import com.advanceachievements.data.entities.UserTaskDto;

@Service
public class UserTaskServiceBasic implements UserTaskService {
	
	@Autowired
	private UserTaskDao userTaskDao;

	@Override
	@Transactional
	public Optional<UserTask> retrieve(long id) {
		return userTaskDao.retrieve(id);
	}

	@Override
	@Transactional
	public boolean create(UserTaskDto userTaskDto) {
		UserTask userTask = new UserTask(userTaskDto.getTitle(), userTaskDto.getDescription(), userTaskDto.getPriority());
		userTaskDao.create(userTask);
		return true;
	}
	
}
