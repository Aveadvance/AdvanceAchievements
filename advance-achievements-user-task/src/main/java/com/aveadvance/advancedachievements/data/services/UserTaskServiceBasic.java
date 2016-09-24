package com.aveadvance.advancedachievements.data.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advanceachievements.data.dao.UserTaskDao;
import com.advanceachievements.data.services.UserTaskService;
import com.aveadvance.advancedachievements.data.entities.Priority;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;
import com.aveadvance.advancedachievements.data.entities.UserTaskState;
import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.services.UserAccountService;
import com.aveadvance.advancedachievements.data.services.UserTaskCategoryService;
import com.aveadvance.advancedachievements.data.services.WorkspaceService;
import com.aveadvance.advancedachievements.exceptions.CategoryNotEmptyException;

@Service
public class UserTaskServiceBasic implements UserTaskService {
	
	@Autowired
	private WorkspaceService workspaceService;
	
	@Autowired
	private UserTaskDao userTaskDao;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private UserTaskCategoryService userTaskCategoryService;
	
	@Autowired
	private UserTaskService userTaskService;

	@Override
	@Transactional(readOnly=true)
	public Optional<UserTask> retrieve(long id) {
		return userTaskDao.retrieve(id);
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional
	public boolean create(long workspaceId, String title, String description, Priority priority) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserAccount owner = userAccountService.retrieve(auth.getName()).get();
		workspaceService.retrieve(workspaceId).ifPresent(workspace -> {
			UserTask userTask = new UserTask(workspace, title, description, priority, owner
					, UserTaskState.TO_DO, LocalDateTime.now());
			userTaskDao.create(userTask);
		});
		return true;
	}
	
	@Secured("ROLE_USER")
	@Transactional
	private boolean create(Workspace workspace, String title, String description, Priority priority, UserTaskCategory category) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserAccount owner = userAccountService.retrieve(auth.getName()).get();
		UserTask userTask = new UserTask(workspace, title, description, priority, owner
				,category , UserTaskState.TO_DO, LocalDateTime.now());
		userTaskDao.create(userTask);
		return true;
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional
	public boolean create(long workspaceId, String title, String description, Priority priority, long userTaskCategoryId) {
		Optional<UserTaskCategory> userTaskCategory = userTaskCategoryService.retrieve(userTaskCategoryId);
		workspaceService.retrieve(workspaceId).ifPresent(workspace -> {
			userTaskCategory.ifPresent(category -> {
				if (category.getWorkspace().equals(workspace))
					create(workspace, title, description, priority, category);
			});
		});
		return true;
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserTask> retrieve(String email, long workspaceId) {
		// TODO: Create caching of user tasks list request.
		
		/*
		 * 
		 * 
		 * 
		 * TEST METHOD
		 * 
		 * 
		 * 
		 */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserAccount owner = userAccountService.retrieve(auth.getName()).get();
		return userTaskDao.retrieve(owner, workspaceId);
	}
	
	@Override
	@Transactional
	@Secured({"ROLE_USER"})
	public void delete(long workspaceId, long id) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!userTaskService
				.retrieve(auth.getName(), workspaceId)
				.parallelStream()
				.filter(task -> task.getCategory().isPresent()?task.getCategory().get().getId()==id:false)
				.collect(Collectors.toList())
				.isEmpty()) {
			throw new CategoryNotEmptyException();
		}
		
		userTaskDao.delete(id);
	}

	@Override
	public void update(long workspaceId, long id, String title, String description, Priority priority) {
		workspaceService.retrieve(workspaceId).ifPresent(workspace -> {
			userTaskService.retrieve(id).ifPresent(task -> {
				if (task.getWorkspace().equals(workspace)) {
					userTaskDao.update(new UserTask(id, workspace, title, description
							, priority, task.getOwner(), task.getCategory().orElse(null), task.getState(), task.getCreationDate()));
				}
			});
		});
	}
	
}
