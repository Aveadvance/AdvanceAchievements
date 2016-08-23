package com.advanceachievements.data.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.advanceachievements.data.entities.UserAccount;

@Repository
public class UserAccountDaoBasic implements UserAccountDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void create(UserAccount userAccount) {
		entityManager.persist(userAccount);
	}

	@Override
	public UserAccount retrieve(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(UserAccount userAccount) {
		entityManager.merge(userAccount);
	}

	@Override
	public void delete(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<UserAccount> retrieve(String email) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserAccount> criteriaQuery = criteriaBuilder.createQuery(UserAccount.class);
		Root<UserAccount> root = criteriaQuery.from(UserAccount.class);
		criteriaQuery.select(root);
		
		ParameterExpression<String> params = criteriaBuilder.parameter(String.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get("email"), params));
		
		TypedQuery<UserAccount> query = entityManager.createQuery(criteriaQuery);
		query.setParameter(params, email);
		
		Optional<UserAccount> userAccount;
		try {
			userAccount = Optional.ofNullable(query.getSingleResult());
		} catch (NoResultException ex) {
			userAccount = Optional.empty();
		}
		
		return userAccount;
	}

}
