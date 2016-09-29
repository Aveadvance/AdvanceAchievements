package com.aveadvance.advancedachievements.data.dao;

import java.util.List;

import com.aveadvance.advancedachievements.data.dao.Repo;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.Workspace;

public interface WorkspaceDao extends Repo<Workspace, Long> {

	List<Workspace> retrieveAll(UserAccount userAccount);

}
