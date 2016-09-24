package com.advanceachievements.data.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractDao<E,I> implements Repo<E,I> {
	
	private final Class<E> entityClass;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public AbstractDao(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public void create(E entity) {
		entityManager.persist(entity);
	}

	@Override
	public Optional<E> retrieve(I id) {
		return Optional.ofNullable(entityManager.find(entityClass, id));
	}

	@Override
	public void update(E entity) {
		entityManager.merge(entity);
	}

	@Override
	public void delete(I id) {
		E entity = entityManager.getReference(entityClass, id);
		entityManager.remove(entity);
		//entityManager.flush();
	}

//	@Override
//	public Optional<UserAccount> retrieve(String email) {
//		
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<UserAccount> criteriaQuery = criteriaBuilder.createQuery(UserAccount.class);
//		Root<UserAccount> root = criteriaQuery.from(UserAccount.class);
//		criteriaQuery.select(root);
//		
//		ParameterExpression<String> params = criteriaBuilder.parameter(String.class);
//		criteriaQuery.where(criteriaBuilder.equal(root.get("email"), params));
//		
//		TypedQuery<UserAccount> query = entityManager.createQuery(criteriaQuery);
//		query.setParameter(params, email);
//		
//		Optional<UserAccount> userAccount;
//		try {
//			userAccount = Optional.ofNullable(query.getSingleResult());
//		} catch (NoResultException ex) {
//			userAccount = Optional.empty();
//		}
//		
//		return userAccount;
//	}
}
