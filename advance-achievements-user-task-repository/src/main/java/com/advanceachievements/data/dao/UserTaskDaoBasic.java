package com.advanceachievements.data.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.advanceachievements.data.entities.UserAccount;
import com.advanceachievements.data.entities.UserTask;
import com.advanceachievements.data.services.UserAccountService;

@Repository
public class UserTaskDaoBasic extends AbstractDao<UserTask, Long> implements UserTaskDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UserAccountService userAccountService;

	public UserTaskDaoBasic(@Value(value="com.advanceachievements.data.entities.UserTask") Class<UserTask> entityClass) {
		super(entityClass);
	}

	@Override
	public List<UserTask> retrieve(String email) {
		UserAccount owner = userAccountService.retrieve(email).get();
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserTask> criteriaQuery = criteriaBuilder.createQuery(UserTask.class);
		Root<UserTask> root = criteriaQuery.from(UserTask.class);
		criteriaQuery.select(root);
		ParameterExpression<UserAccount> ownerUserAccountParam = criteriaBuilder.parameter(UserAccount.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get("owner"), ownerUserAccountParam));
		TypedQuery<UserTask> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setParameter(ownerUserAccountParam, owner);
		return typedQuery.getResultList();
	}

}
