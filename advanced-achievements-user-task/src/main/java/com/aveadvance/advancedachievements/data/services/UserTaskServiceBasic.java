package com.aveadvance.advancedachievements.data.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aveadvance.advancedachievements.data.dao.UserTaskDao;
import com.aveadvance.advancedachievements.data.entities.Priority;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;
import com.aveadvance.advancedachievements.data.entities.UserTaskState;
import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.services.UserTaskService;

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
	private UserTaskTimerService userTaskTimerService;

	@Override
	@Transactional(readOnly=true)
	public Optional<UserTask> retrieve(long workspaceId, long id) {
		Optional<UserTask> userTask = userTaskDao.retrieve(id);
		if (userTask.isPresent() && userTask.get().getWorkspace().getId() == workspaceId) {
			return userTask;
		}
		return Optional.empty();
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional
	public boolean create(long workspaceId, String title, String description, Priority priority) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserAccount owner = userAccountService.retrieve(auth.getName()).get();
		workspaceService.retrieve(workspaceId).ifPresent(workspace -> {
			LocalDateTime now = LocalDateTime.now();
			UserTask userTask = new UserTask(workspace, title, description, priority, owner
					, UserTaskState.TO_DO, now);
			userTaskDao.create(userTask);
			userTaskTimerService.create(userTask, now, now);
		});
		return true;
	}
	
	@Secured("ROLE_USER")
	@Transactional
	private boolean create(Workspace workspace, String title, String description, Priority priority, UserTaskCategory category) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserAccount owner = userAccountService.retrieve(auth.getName()).get();
		LocalDateTime now = LocalDateTime.now();
		UserTask userTask = new UserTask(workspace, title, description, priority, owner
				,category , UserTaskState.TO_DO, now);
		userTaskDao.create(userTask);
		userTaskTimerService.create(userTask, now, now);
		return true;
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional
	public boolean create(long workspaceId, String title, String description, Priority priority, long userTaskCategoryId) {
		Optional<UserTaskCategory> userTaskCategory = userTaskCategoryService.retrieve(workspaceId, userTaskCategoryId);

		userTaskCategory.ifPresent(category -> {
			workspaceService.retrieve(workspaceId).ifPresent(workspace -> {
				create(workspace, title, description, priority, category);
			});
		});
		
		return true;
	}

	@Override
	@Transactional(readOnly=true)
	@Secured({"ROLE_USER"})
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
	@Transactional(readOnly=true)
	@Secured({"ROLE_USER"})
	public List<UserTask> retrieve(String email, long workspaceId, UserTaskState state) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserAccount owner = userAccountService.retrieve(auth.getName()).get();
		return userTaskDao.retrieve(owner, workspaceId, state);
	}
	
	@Override
	@Transactional
	@Secured({"ROLE_USER"})
	public void delete(long workspaceId, long id) {
		userTaskTimerService.delete(id);
		userTaskDao.delete(id);
	}

	@Override
	@Transactional
	public void update(long workspaceId, long id, String title, String description, Priority priority) {
		workspaceService.retrieve(workspaceId).ifPresent(workspace -> {
			retrieve(workspace.getId(), id).ifPresent(task -> {
				if (task.getWorkspace().equals(workspace)) {
					userTaskDao.update(new UserTask(id, workspace, title, description
							, priority, task.getOwner(), task.getCategory().orElse(null), task.getState(), task.getCreationDate()));
				}
			});
		});
	}

	@Override
	@Transactional
	public void completeTask(long workspaceId, long id) {
		workspaceService.retrieve(workspaceId).ifPresent(workspace -> {
			retrieve(workspace.getId(), id).ifPresent(task -> {
				if (task.getWorkspace().equals(workspace)) {
					UserTaskState state = UserTaskState.TO_DO;
					LocalDateTime completionDate = null;
					if (!task.getState().equals(UserTaskState.ACHIEVED)) {
						state = UserTaskState.ACHIEVED;
						completionDate = LocalDateTime.now();
					}
					userTaskDao.update(new UserTask(task.getId(), task.getWorkspace(), task.getTitle(), task.getDescription()
							, task.getPriority(), task.getOwner(), task.getCategory().orElse(null), state, task.getCreationDate(), completionDate));
				}
			});
		});
	}
	
}
