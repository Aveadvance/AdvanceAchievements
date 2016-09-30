package com.aveadvance.advancedachievements.data.dao;

import java.util.List;

import com.aveadvance.advancedachievements.data.entities.UserProject;

public interface UserProjectDao extends Repo<UserProject, Long> {

	List<UserProject> retrieveAll(long workspaceId);

}
