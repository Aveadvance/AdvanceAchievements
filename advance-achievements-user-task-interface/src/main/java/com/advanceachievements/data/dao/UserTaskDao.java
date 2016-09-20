package com.advanceachievements.data.dao;

import java.util.List;

import com.advanceachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.UserTask;

public interface UserTaskDao extends Repo<UserTask, Long> {

	List<UserTask> retrieve(UserAccount userAccount);
	
}
