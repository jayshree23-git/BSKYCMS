package com.project.bsky.serviceImpl;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.service.GroupDetailsService;

@Service
public class GroupDetailsServiceImpl implements GroupDetailsService {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Transactional
	public Integer getGroupDetails(String groupName, Integer isSubgrouped, String parentGroupId) {

		Integer count = 0;
		try {
			StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("USP_Group_create")
					.registerStoredProcedureParameter("Action", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("Groupname", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("IsSubgrouped", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("ParentSubgroupId", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("Createdby", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("IsActive", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			query.setParameter("Action", "I");
			query.setParameter("Groupname", groupName);
			query.setParameter("IsSubgrouped", isSubgrouped);
			query.setParameter("ParentSubgroupId", parentGroupId);
			query.setParameter("Createdby", 1);
			query.setParameter("IsActive", 0);
			count = (Integer) query.getOutputParameterValue("P_MSGOUT");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return count;
	}

}
