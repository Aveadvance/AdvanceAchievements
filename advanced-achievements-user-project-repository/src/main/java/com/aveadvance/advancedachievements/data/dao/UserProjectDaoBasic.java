package com.aveadvance.advancedachievements.data.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.aveadvance.advancedachievements.data.entities.UserProject;

@Repository
public class UserProjectDaoBasic extends AbstractDao<UserProject, Long> implements UserProjectDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	public UserProjectDaoBasic() {
		super(UserProject.class);
	}

	@Override
	public List<UserProject> retrieveAll(long workspaceId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<UserProject> criteriaQuery = criteriaBuilder.createQuery(UserProject.class);
		
		Root<UserProject> root = criteriaQuery.from(UserProject.class);
		
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("workspace").get("id"), workspaceId));
		
		TypedQuery<UserProject> typedQuery = entityManager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}

}
