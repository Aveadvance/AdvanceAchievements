package com.aveadvance.advancedachievements.data.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advanceachievements.data.services.UserTaskService;
import com.aveadvance.advancedachievements.data.dao.UserTaskCategoryDao;
import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;
import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.exceptions.CategoryNotEmptyException;

@Service
public class UserTaskCategoryServiceBasic implements UserTaskCategoryService {
	
	@Autowired
	private UserTaskCategoryDao userTaskCategoryDao;
	
	@Autowired
	private UserTaskService userTaskService;
	
	@Autowired
	private WorkspaceService workspaceService;

	@Override
	@Secured("ROLE_USER")
	@Transactional
	public long create(long workspaceId, String name) {
		List<Workspace> workspaces = workspaceService.retrieveAll();
//		workspaces = workspaces.parallelStream()
//				.filter((workspace) -> workspace.getId() == workspaceId).limit(2).collect(Collectors.toList());
//		if (workspaces.size() > 1) throw new NonUniqueResultException();
		UserTaskCategory userTaskCategory = new UserTaskCategory(name, workspaces.get(0));
		userTaskCategoryDao.create(userTaskCategory);
		return userTaskCategory.getId();
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<UserTaskCategory> retrieve(long workspaceId, long id) {
		Optional<UserTaskCategory>userTaskCategory = userTaskCategoryDao.retrieve(id);
		if (userTaskCategory.isPresent() && userTaskCategory.get().getWorkspace().getId() != workspaceId) {
			userTaskCategory = Optional.empty();
		}
		return userTaskCategory;
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserTaskCategory> retrieveAll(long workspaceId) {
		Workspace workspace = workspaceService.retrieve(workspaceId).get();
		return userTaskCategoryDao.retrieveAll(workspace);
	}

	@Override
	@Transactional
	public void update(long workspaceId, long id, String name) {
		workspaceService.retrieve(workspaceId).ifPresent(workspace -> {
			retrieve(workspace.getId(), id).ifPresent(userTaskCategory -> {
				userTaskCategoryDao.update(new UserTaskCategory(workspace, id, name));
			});
		});
	}

	@Override
	@Transactional
	@Secured({"ROLE_USER"})
	public void delete(long workspaceId, long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserTaskCategory> userTaskCategory = userTaskCategoryDao.retrieve(id);
		userTaskCategory.ifPresent(category -> {
			if (workspaceId == category.getWorkspace().getId()) {
				if (userTaskService.retrieve(auth.getName(), workspaceId)
				.parallelStream()
				.filter(task -> task.getCategory().equals(Optional.ofNullable(category))).count() > 0) {
					throw new CategoryNotEmptyException("Category is not empty! Delete the tasks first please.");
				}
				userTaskCategoryDao.delete(id);
			}
		});
	}

}
