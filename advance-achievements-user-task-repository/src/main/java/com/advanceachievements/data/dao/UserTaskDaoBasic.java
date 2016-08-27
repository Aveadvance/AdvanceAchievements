package com.advanceachievements.data.dao;

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

import com.advanceachievements.data.entities.UserAccount;
import com.advanceachievements.data.entities.UserTask;

@Repository
public class UserTaskDaoBasic extends AbstractDao<UserTask, Long> implements UserTaskDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	public UserTaskDaoBasic(@Value(value="com.advanceachievements.data.entities.UserTask") Class<UserTask> entityClass) {
		super(entityClass);
	}

	@Override
	public List<UserTask> retrieve(UserAccount userAccount) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserTask> criteriaQuery = criteriaBuilder.createQuery(UserTask.class);
		Root<UserTask> root = criteriaQuery.from(UserTask.class);
		criteriaQuery.select(root);
		ParameterExpression<UserAccount> ownerUserAccountParam = criteriaBuilder.parameter(UserAccount.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get("owner"), ownerUserAccountParam));
		TypedQuery<UserTask> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setParameter(ownerUserAccountParam, userAccount);
		return typedQuery.getResultList();
	}

}
