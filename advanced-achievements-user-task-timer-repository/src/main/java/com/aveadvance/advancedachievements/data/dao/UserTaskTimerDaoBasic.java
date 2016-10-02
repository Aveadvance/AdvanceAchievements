package com.aveadvance.advancedachievements.data.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.aveadvance.advancedachievements.data.entities.UserTaskTimer;

@Repository
public class UserTaskTimerDaoBasic extends AbstractDao<UserTaskTimer, Long> implements UserTaskTimerDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	public UserTaskTimerDaoBasic() {
		super(UserTaskTimer.class);
	}

	@Override
	public List<UserTaskTimer> retrieve(String userName, long workspaceId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<UserTaskTimer> criteriaQuery = criteriaBuilder.createQuery(UserTaskTimer.class);
		
		Root<UserTaskTimer> root = criteriaQuery.from(UserTaskTimer.class);
		
		criteriaQuery.select(root);
		
		ParameterExpression<Long> workspaceIdParam = criteriaBuilder.parameter(Long.class);
		ParameterExpression<String> userNameParam = criteriaBuilder.parameter(String.class);
		
		criteriaQuery.where(criteriaBuilder
				.and(criteriaBuilder.equal(root.get("userTask").get("workspace").get("id"), workspaceIdParam)
				, criteriaBuilder.equal(root.get("userTask").get("owner").get("email"), userNameParam)));
		
		TypedQuery<UserTaskTimer> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setParameter(workspaceIdParam, workspaceId);
		typedQuery.setParameter(userNameParam, userName);
		
		return typedQuery.getResultList();
	}
	
	@Override
	public void delete(Long taskId) {
		Query query = entityManager.createQuery("DELETE FROM UserTaskTimer t WHERE t.userTask.id = :p");
		query.setParameter("p", taskId).executeUpdate();
	}

}
