package com.project.bsky.serviceImpl;

import java.sql.ResultSet;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.service.RunCPDScheduleService;

@Service
public class RunCPDScheduleServiceImpl implements RunCPDScheduleService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public Integer getTotalCasetobeAssign() {
		Integer tobeAssigncpdObj = null;
		ResultSet rSet = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_RUN_CPD_SCHEDULE")
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.execute();
			rSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rSet.next()) {
				tobeAssigncpdObj = rSet.getInt(1);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rSet != null) {
					rSet.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return tobeAssigncpdObj;
	}

	// Freshclaim procedure will call
	@Override
	public void runcdpscheduleFreshClaim() {
		
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ASSIGN_CLAIM_TO_CPD_FRESH");
			storedProcedureQuery.execute();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {

			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	// dishonored procedure will call
	@Override
	public void runcpdScheduleDishonored() {

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ASSIGN_CLAIM_DISHONORED");
			storedProcedureQuery.execute();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {

			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}

	}

}
