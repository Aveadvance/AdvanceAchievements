package com.aveadvance.advancedachievements.data.dao;

import java.util.List;

import com.aveadvance.advancedachievements.data.entities.UserTaskTimer;

public interface UserTaskTimerDao extends Repo<UserTaskTimer, Long>  {

	List<UserTaskTimer> retrieve(String userName, long workspaceId);

}
