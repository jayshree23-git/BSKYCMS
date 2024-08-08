package com.project.bsky.serviceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.project.bsky.service.SNAFloatGenerationService;
import com.project.bsky.util.DaysBetweenDates;

@Service
public class SNAFloatGenerationServiceImpl implements SNAFloatGenerationService{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public String viewSnaFloatList(Date fromDate, Date toDate, String finacialno,Integer userId) {
		List<Object[]> floatDetailsList;
		JSONArray jsonArray = new JSONArray();
		
		JSONObject jsonObject;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_VIEW_FLOAT_GEN")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fiancialno", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);


			storedProcedureQuery.setParameter("p_userId", userId);
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_fiancialno", finacialno);
			
			storedProcedureQuery.execute();
			floatDetailsList = storedProcedureQuery.getResultList();

			for (Iterator<Object[]> iterator = floatDetailsList.iterator(); iterator.hasNext();) {
				Object[] floatDetails = iterator.next();

				
				jsonObject = new JSONObject();
				jsonObject.put("floatId", floatDetails[0]);
				jsonObject.put("floatNo", floatDetails[1]);
				jsonObject.put("amount", floatDetails[2]);
				jsonObject.put("createdDate", floatDetails[3]);
				jsonObject.put("payStatus", floatDetails[4]);
				jsonObject.put("pendingStatus", floatDetails[5]);
				jsonObject.put("createdName", floatDetails[6]);
			
				
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return jsonArray.toString();
	}

	
	@Override
	public String viewIndivisualFloatDetails(String floatNo) {
		List<Object[]> claimRaiseDetailsList;
		JSONArray jsonArray = new JSONArray();
		
		JSONObject jsonObject;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_FLOAT_GEN_ACT")
					.registerStoredProcedureParameter("p_floatNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimed_list", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_floatNo", floatNo);
			
			storedProcedureQuery.execute();
			claimRaiseDetailsList = storedProcedureQuery.getResultList();

			for (Iterator<Object[]> iterator = claimRaiseDetailsList.iterator(); iterator.hasNext();) {
				Object[] claimRaiseDetails = iterator.next();

				////System.out.println("ClaimRaiseDetails : " + Arrays.toString(claimRaiseDetails));
				jsonObject = new JSONObject();
				jsonObject.put("claimId", claimRaiseDetails[0]);
				jsonObject.put("transactionID", claimRaiseDetails[1]);
				jsonObject.put("URN", claimRaiseDetails[2]);
				jsonObject.put("patientName", claimRaiseDetails[3]);
				jsonObject.put("packageCode", claimRaiseDetails[4]);
				jsonObject.put("packageId", claimRaiseDetails[5]);
				jsonObject.put("packageName", claimRaiseDetails[6]);
				//jsonObject.put("currentTotalAmount", claimRaiseDetails[7]);
				//jsonObject.put("dateOfDischarge", CommonFileUpload.convertTimestampToString(claimRaiseDetails[8]));
				jsonObject.put("dateOfDischarge", claimRaiseDetails[7]);
				jsonObject.put("transClaimId", claimRaiseDetails[8]);
				jsonObject.put("invoiceNo", claimRaiseDetails[9]);
				jsonObject.put("allotedDate", claimRaiseDetails[10]);
				
//				String s = claimRaiseDetails[11].toString();
//				String s1 = s.substring(0, 2);
//				String s2 = s.substring(2, 4);
//				String s3 = s.substring(4, 8);
//				String s4 = s1 + "/" + s2 + "/" + s3;
//				Date ddDate = new SimpleDateFormat("dd/MM/yyyy").parse(s4);
//				////System.out.println("Date From Procedure : " + s4);
//				int days = DaysBetweenDates.daysCountBetweenDates(ddDate);
//				
//				jsonObject.put("remainingDate", String.valueOf(days) + " days left");
				//String sdate = claimRaiseDetails[11].toString();
				
			//	String days = DaysBetweenDates.getDateTimeDifference(sdate);
				
				//jsonObject.put("remainingDate", days);

				jsonObject.put("authorizedcode", claimRaiseDetails[11].toString().substring(2));

				jsonObject.put("hospitalcode", claimRaiseDetails[12]);
				
				jsonObject.put("actualDate", claimRaiseDetails[13]);
				jsonObject.put("claimNo", claimRaiseDetails[14]);
				jsonObject.put("approvedAmount", claimRaiseDetails[15]);
				
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return jsonArray.toString();
	}

}
