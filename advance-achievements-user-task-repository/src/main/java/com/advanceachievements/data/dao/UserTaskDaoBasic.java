package com.advanceachievements.data.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.advanceachievements.data.entities.UserTask;

@Repository
public class UserTaskDaoBasic extends AbstractDao<UserTask, Long> implements UserTaskDao {

	public UserTaskDaoBasic(@Value(value="com.advanceachievements.data.entities.UserTask") Class<UserTask> entityClass) {
		super(entityClass);
	}

}
