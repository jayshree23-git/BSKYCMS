package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CardPolicyBean;
import com.project.bsky.model.CardPolicy;
import com.project.bsky.repository.CardPolicyRepository;
import com.project.bsky.service.CardPolicyService;

@Service
public class CardPolicyServiceImpl implements CardPolicyService {

	@Autowired
	private CardPolicyRepository cardPolicyRepository;

	@Autowired
	private Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public CardPolicy getCardPolicyDate() {
		List<Object[]> getPolicyDate = cardPolicyRepository.findPolicyDate();
		List<CardPolicy> policyList = new ArrayList<>();
		CardPolicy card = new CardPolicy();
		if (getPolicyDate.size() > 0) {
			for (Object[] obj : getPolicyDate) {
				card.setStartDate((Date) obj[0]);
				card.setEndDate((Date) obj[1]);
			}
		}
		return card;
	}

	@Override
	public JSONArray updatePolicy(CardPolicyBean requestBean) {
		JSONArray policyList = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("USP_CARD_POLICY_UPDATE")
					.registerStoredProcedureParameter("P_userId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_startDate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_endDate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_familyAmount", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_femaleAMount", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);
			query.setParameter("P_userId", requestBean.getUserId());
			query.setParameter("P_startDate", requestBean.getStartDate());
			query.setParameter("P_endDate", requestBean.getEndDate());
			query.setParameter("P_familyAmount", requestBean.getFamilyAmount());
			query.setParameter("P_femaleAMount", requestBean.getFemaleAmount());
			query.execute();
			rs = (ResultSet) query.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				JSONObject policyObj = new JSONObject();
				policyObj.put("BALANCEAMOUNT", rs.getInt(1));
				policyObj.put("FEMALEFUND", rs.getInt(2));
				policyObj.put("STARTDATE", rs.getDate(3));
				policyObj.put("ENDDATE", rs.getDate(4));
				policyObj.put("UPDATEDBY", rs.getInt(5));
				policyObj.put("UPDATEDON", rs.getString(6));
				policyObj.put("UPDATEDDATE", rs.getString(7));
				policyList.put(policyObj);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return policyList;
	}

}
