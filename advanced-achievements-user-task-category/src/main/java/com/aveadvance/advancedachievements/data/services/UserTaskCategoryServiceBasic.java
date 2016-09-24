package com.aveadvance.advancedachievements.data.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aveadvance.advancedachievements.data.dao.UserTaskCategoryDao;
import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;
import com.aveadvance.advancedachievements.data.entities.Workspace;

@Service
public class UserTaskCategoryServiceBasic implements UserTaskCategoryService {
	
	@Autowired
	private UserTaskCategoryDao userTaskCategoryDao;
	
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
	public Optional<UserTaskCategory> retrieve(long id) {
		return userTaskCategoryDao.retrieve(id);
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
			retrieve(id).ifPresent(userTaskCategory -> {
				if (userTaskCategory.getWorkspace().equals(workspace)) {
					userTaskCategoryDao.update(new UserTaskCategory(workspace, id, name));
				}
			});
		});
	}

	@Override
	@Transactional
	public void delete(long workspaceId, long id) {
		Optional<UserTaskCategory> userTaskCategory = userTaskCategoryDao.retrieve(id);
		userTaskCategory.ifPresent(category -> {
			if (workspaceId == category.getWorkspace().getId()) {
				userTaskCategoryDao.delete(id);
			}
		});
	}

}
