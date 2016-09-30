package com.aveadvance.advancedachievements.data.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aveadvance.advancedachievements.data.dao.UserProjectDao;
import com.aveadvance.advancedachievements.data.entities.UserProject;
import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.entities.WorkspaceType;
import com.aveadvance.advancedachievements.exceptions.ProjectNotEmptyException;

@Service
public class UserProjectServiceBasic implements UserProjectService {
	
	@Autowired
	private UserProjectDao userProjectDao;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private WorkspaceService workspaceService;
	
	@Autowired
	private UserTaskService userTaskService;
	
	@Autowired
	private UserTaskCategoryService userTaskCategoryService;

	@Override
	@Transactional(readOnly=true)
	public List<UserProject> retrieveAll(long workspaceId) {
		return userProjectDao.retrieveAll(workspaceId);
	}

	@Override
	@Secured({"ROLE_USER"})
	@Transactional
	public void create(long workspaceId, String name, String description) {
		workspaceService.retrieve(workspaceId).ifPresent(workspace -> {
			if (workspace.getType().equals(WorkspaceType.PRIVATE)) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				userAccountService.retrieve(auth.getName()).ifPresent(userAccount -> {
					if (workspace.getUserAccounts().contains(userAccount)) {
						userProjectDao.create(new UserProject(name, description, workspace
								, new Workspace(WorkspaceType.PROJECT, new HashSet<>(Arrays.asList(userAccount)))));
					}
				});
			}
		});
	}

	@Override
	@Secured({"ROLE_USER"})
	@Transactional
	public void delete(long workspaceId, long id) {
		userProjectDao.retrieve(id).ifPresent(project -> {
			if (project.getWorkspace().getId() == workspaceId) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (userTaskService.retrieve(auth.getName(), project.getProjectWorkspace().getId()).size() != 0
						|| userTaskCategoryService.retrieveAll(project.getProjectWorkspace().getId()).size() != 0)
						throw new ProjectNotEmptyException("Project is not empty. Delete categories and tasks first please.");
				userProjectDao.delete(id);
			}
		});
	}

	@Override
	@Transactional
	public void update(long id, String name, String description) {
		userProjectDao.retrieve(id).ifPresent(project -> {
			userProjectDao.update(new UserProject(project.getId(), name, description, project.getWorkspace()
					, project.getProjectWorkspace(), project.getCreationDate()));
		});
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<UserProject> retrieve(Long id) {
		return userProjectDao.retrieve(id);
	}

}
