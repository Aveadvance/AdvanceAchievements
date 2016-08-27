package com.advanceachievements.data.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advanceachievements.data.dao.UserTaskDao;
import com.advanceachievements.data.entities.Priority;
import com.advanceachievements.data.entities.UserAccount;
import com.advanceachievements.data.entities.UserTask;
import com.advanceachievements.data.entities.UserTaskState;

@Service
public class UserTaskServiceBasic implements UserTaskService {
	
	@Autowired
	private UserTaskDao userTaskDao;
	
	@Autowired
	private UserAccountService userAccountService;

	@Override
	@Transactional(readOnly=true)
	public Optional<UserTask> retrieve(long id) {
		return userTaskDao.retrieve(id);
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional
	public boolean create(String title, String description, Priority priority) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserAccount owner = userAccountService.retrieve(auth.getName()).get();
		UserTask userTask = new UserTask(title, description, priority, owner
				, UserTaskState.TO_DO, LocalDateTime.now());
		userTaskDao.create(userTask);
		return true;
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserTask> retrieve(String email) {
		// TODO: Create caching of user tasks list request.
		return userTaskDao.retrieve(email);
	}
	
}
