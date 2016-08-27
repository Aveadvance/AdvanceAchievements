package com.advanceachievements.data.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.NonUniqueResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.advanceachievements.data.dao.UserTaskCategoryDao;
import com.advanceachievements.data.entities.UserTaskCategory;
import com.advanceachievements.data.entities.Workspace;

@Service
public class UserTaskCategoryServiceBasic implements UserTaskCategoryService {
	
	@Autowired
	private UserTaskCategoryDao userTaskCategoryDao;
	
	@Autowired
	private WorkspaceService workspaceService;

	@Override
	@Secured("ROLE_USER")
	public long create(long workspaceId, String name) {
		List<Workspace> workspaces = workspaceService.retrieveAll();
		workspaces = workspaces.parallelStream()
				.filter((workspace) -> workspace.getId() == workspaceId).limit(2).collect(Collectors.toList());
		if (workspaces.size() > 1) throw new NonUniqueResultException();
		UserTaskCategory userTaskCategory = new UserTaskCategory(name, workspaces.get(0));
		userTaskCategoryDao.create(userTaskCategory);
		return userTaskCategory.getId();
	}

	@Override
	public Optional<UserTaskCategory> retrieve(long id) {
		return userTaskCategoryDao.retrieve(id);
	}

	@Override
	public List<UserTaskCategory> retrieveAll(long workspaceId) {
		Workspace workspace = workspaceService.retrieve(workspaceId).get();
		return userTaskCategoryDao.retrieveAll(workspace);
	}

	@Override
	public void update(UserTaskCategory userTaskCategory) {
		userTaskCategoryDao.update(userTaskCategory);
	}

}
