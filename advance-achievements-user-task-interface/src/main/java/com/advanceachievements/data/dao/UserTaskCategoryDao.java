package com.advanceachievements.data.dao;

import java.util.List;

import com.advanceachievements.data.entities.UserTaskCategory;
import com.advanceachievements.data.entities.Workspace;

public interface UserTaskCategoryDao extends Repo<UserTaskCategory, Long> {

	List<UserTaskCategory> retrieveAll(Workspace workspace);
	
}
