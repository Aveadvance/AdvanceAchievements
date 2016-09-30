package com.aveadvance.advancedachievements.data.services;

import java.util.List;
import java.util.Optional;

import com.aveadvance.advancedachievements.data.entities.UserProject;

public interface UserProjectService {

	List<UserProject> retrieveAll(long workspaceId);

	void create(long workspaceId, String name, String description);

	void delete(long workspaceId, long id);

	void update(long id, String name, String description);

	Optional<UserProject> retrieve(Long id);
}
