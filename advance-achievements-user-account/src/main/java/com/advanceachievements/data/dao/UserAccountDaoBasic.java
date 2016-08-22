package com.advanceachievements.data.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.advanceachievements.data.entities.UserAccount;

@Repository
public class UserAccountDaoBasic implements UserAccountDao {
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Override
	public void create(UserAccount userAccount) {
		EntityManager em = entityManagerFactory.createEntityManager();
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(userAccount);
		tx.commit();
	}

	@Override
	public UserAccount retrieve(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(UserAccount t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UserAccount retrieve(String email) {
		EntityManager em = entityManagerFactory.createEntityManager();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<UserAccount> criteriaQuery = criteriaBuilder.createQuery(UserAccount.class);
		Root<UserAccount> root = criteriaQuery.from(UserAccount.class);
		criteriaQuery.select(root);
		
		ParameterExpression<String> params = criteriaBuilder.parameter(String.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get("email"), params));
		
		TypedQuery<UserAccount> query = em.createQuery(criteriaQuery);
		query.setParameter(params, email);
		
		UserAccount userAccount = query.getSingleResult();
		
		return userAccount;
	}

}
