package com.advanceachievements.data.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advanceachievements.data.dao.UserTaskCategoryDao;
import com.advanceachievements.data.entities.UserTaskCategory;

@Service
public class UserTaskCategoryServiceBasic implements UserTaskCategoryService {
	
	@Autowired
	private UserTaskCategoryDao userTaskCategoryDao;

	@Override
	public long create(String name) {
		UserTaskCategory userTaskCategory = new UserTaskCategory(name);
		userTaskCategoryDao.create(userTaskCategory);
		return userTaskCategory.getId();
	}

	@Override
	public Optional<UserTaskCategory> retrieve(long id) {
		return userTaskCategoryDao.retrieve(id);
	}

}
