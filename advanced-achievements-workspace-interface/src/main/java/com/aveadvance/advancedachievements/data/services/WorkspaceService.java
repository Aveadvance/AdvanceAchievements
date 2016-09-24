package com.aveadvance.advancedachievements.data.services;

import java.util.List;
import java.util.Optional;

import com.aveadvance.advancedachievements.data.entities.Workspace;

public interface WorkspaceService {

	List<Workspace> retrieveAll();

	void create(Workspace workspace);

	Optional<Workspace> retrieve(long workspaceId);

}
