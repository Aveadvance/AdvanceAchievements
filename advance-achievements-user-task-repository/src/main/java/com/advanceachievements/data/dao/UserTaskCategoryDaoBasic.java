package com.advanceachievements.data.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.advanceachievements.data.entities.UserTaskCategory;

@Repository
public class UserTaskCategoryDaoBasic extends AbstractDao<UserTaskCategory, Long> implements UserTaskCategoryDao {

	public UserTaskCategoryDaoBasic(@Value(value="com.advanceachievements.data.entities.UserTaskCategory") Class<UserTaskCategory> entityClass) {
		super(entityClass);
	}

}
