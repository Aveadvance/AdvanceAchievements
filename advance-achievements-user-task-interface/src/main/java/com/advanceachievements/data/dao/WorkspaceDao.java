package com.advanceachievements.data.dao;

import java.util.List;

import com.advanceachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.Workspace;

public interface WorkspaceDao extends Repo<Workspace, Long> {

	List<Workspace> retrieveAll(UserAccount userAccount);

}
