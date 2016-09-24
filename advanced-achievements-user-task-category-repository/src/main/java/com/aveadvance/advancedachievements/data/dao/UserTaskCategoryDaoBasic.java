package com.aveadvance.advancedachievements.data.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.advanceachievements.data.dao.AbstractDao;
import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;
import com.aveadvance.advancedachievements.data.entities.Workspace;

@Repository
public class UserTaskCategoryDaoBasic extends AbstractDao<UserTaskCategory, Long> implements UserTaskCategoryDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	public UserTaskCategoryDaoBasic(@Value(value="com.aveadvance.advancedachievements.data.entities.UserTaskCategory") Class<UserTaskCategory> entityClass) {
		super(entityClass);
	}

	@Override
	public List<UserTaskCategory> retrieveAll(Workspace workspace) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserTaskCategory> criteriaQuery = criteriaBuilder.createQuery(UserTaskCategory.class);
		Root<UserTaskCategory> root = criteriaQuery.from(UserTaskCategory.class);
		criteriaQuery.select(root);
		ParameterExpression<Workspace> workspaceParam = criteriaBuilder.parameter(Workspace.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get("workspace"), workspaceParam));
		TypedQuery<UserTaskCategory> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setParameter(workspaceParam, workspace);
		return typedQuery.getResultList();
	}

}
