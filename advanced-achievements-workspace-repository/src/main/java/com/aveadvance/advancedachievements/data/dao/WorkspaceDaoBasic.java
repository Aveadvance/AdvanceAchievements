package com.aveadvance.advancedachievements.data.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.advanceachievements.data.dao.AbstractDao;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.Workspace;

@Repository
public class WorkspaceDaoBasic extends AbstractDao<Workspace, Long> implements WorkspaceDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	public WorkspaceDaoBasic(@Value("com.aveadvance.advancedachievements.data.entities.Workspace") Class<Workspace> entityClass) {
		super(entityClass);
	}

	@Override
	public List<Workspace> retrieveAll(UserAccount userAccount) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Workspace> criteriaQuery = criteriaBuilder.createQuery(Workspace.class);
		Root<Workspace> root = criteriaQuery.from(Workspace.class);
		criteriaQuery.select(root);
		Expression<Collection<UserAccount>> userAccounts = root.get("userAccounts");
		criteriaQuery.where(criteriaBuilder.isMember(userAccount, userAccounts));
		TypedQuery<Workspace> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

}
