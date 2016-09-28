package com.aveadvance.advancedachievements.data.services;

import java.util.List;
import java.util.Optional;

import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.entities.WorkspaceType;

public interface WorkspaceService {

	List<Workspace> retrieveAll();

	void create(WorkspaceType type);

	Optional<Workspace> retrieve(long workspaceId);

}
