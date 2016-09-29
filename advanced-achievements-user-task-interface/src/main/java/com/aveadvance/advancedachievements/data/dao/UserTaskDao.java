package com.aveadvance.advancedachievements.data.dao;

import java.util.List;

import com.aveadvance.advancedachievements.data.dao.Repo;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskState;

public interface UserTaskDao extends Repo<UserTask, Long> {

	List<UserTask> retrieve(UserAccount userAccount, long workspaceId);
	
	List<UserTask> retrieve(UserAccount userAccount, long workspaceId, UserTaskState state);
	
}
