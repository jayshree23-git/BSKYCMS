package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.service.CpdRegistrationPreviewService;
import com.project.bsky.util.CommonFileUpload;

@Service
public class CpdRegistrationPreviewServiceImpl implements CpdRegistrationPreviewService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;
	
	 SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");


	@Override
	public String previewDetails(Integer cpdUserId) throws Exception {
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray jsonArray3= new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject2;
		JSONObject jsonObject3;
		JSONObject jsonObject4;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		ResultSet snoDetailsObj2 = null;
		ResultSet snoDetailsObj3 = null;
		ResultSet snoDetailsObj4 = null;
		JSONObject details = new JSONObject();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_REGISTRATION_VIEW")
					.registerStoredProcedureParameter("P_CPD_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_basicdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_addressdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_qualificationdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_experiencedetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_SPECIALITY", void.class, ParameterMode.REF_CURSOR);
				
			storedProcedureQuery.setParameter("P_CPD_USERID", cpdUserId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_basicdetails");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_addressdetails");
			snoDetailsObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_qualificationdetails");
			snoDetailsObj3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_experiencedetails");
			snoDetailsObj4 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_SPECIALITY");
			
			if (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("basicDetailsId", snoDetailsObj.getString(1)!=null ? snoDetailsObj.getString(1):"");
				jsonObject.put("cpdUserId", snoDetailsObj.getString(2)!=null ? snoDetailsObj.getString(2):"");
				jsonObject.put("aplicantName", snoDetailsObj.getString(3)!=null ? snoDetailsObj.getString(3):"");
				jsonObject.put("postName", snoDetailsObj.getString(4)!=null ? snoDetailsObj.getString(4):"");
				jsonObject.put("dateOfBirth", snoDetailsObj.getString(5));
//				Date toDate2 = inputFormat.parse(snoDetailsObj.getString(5)); // Assuming snoDetailsObj3.getString(6) returns a String representation of date in "yyyy-MM-dd HH:mm:ss" format
//	            System.out.println("date 1"+snoDetailsObj.getString(5));
//				String formattedDate2 = outputFormat.format(toDate2);
//	            jsonObject.put("dateOfBirth", formattedDate2);
				jsonObject.put("age", snoDetailsObj.getString(6)!=null ? snoDetailsObj.getString(6):"");
				jsonObject.put("fatherName", snoDetailsObj.getString(7));
				jsonObject.put("email", snoDetailsObj.getString(8));
				jsonObject.put("lanuagesReadWrite", snoDetailsObj.getString(9));
				jsonObject.put("identityProofNo", snoDetailsObj.getString(10));
				jsonObject.put("dlNo", snoDetailsObj.getString(11));
				jsonObject.put("identyType", snoDetailsObj.getString(12));
				jsonObject.put("photograph", snoDetailsObj.getString(13)!=null ? snoDetailsObj.getString(13):"");
				jsonObject.put("signatureDoc", snoDetailsObj.getString(14)!=null ? snoDetailsObj.getString(14):"");
				jsonObject.put("applicationSubmitDate", snoDetailsObj.getString(15)!=null ? snoDetailsObj.getString(15):"");
				jsonObject.put("identityDoc", snoDetailsObj.getString(16));
				jsonObject.put("phonenumber", snoDetailsObj.getString(17));
				jsonObject.put("gender", snoDetailsObj.getString(18));
				details.put("basicDetailsData", jsonObject);
			}
			if(snoDetailsObj1.next()) {
				jsonObject1 = new JSONObject();
				jsonObject1.put("cpdAddressId", snoDetailsObj1.getString(1));
				jsonObject1.put("cpdUserId", snoDetailsObj1.getString(2));
				jsonObject1.put("psntCounrty", snoDetailsObj1.getString(3));
				jsonObject1.put("stateName1", snoDetailsObj1.getString(4));
				jsonObject1.put("districtName1", snoDetailsObj1.getString(5));
				jsonObject1.put("psntMobileNo", snoDetailsObj1.getString(6));
				jsonObject1.put("psntAlterMobileNo", snoDetailsObj1.getString(7));
				jsonObject1.put("psntPostalAds", snoDetailsObj1.getString(8));
				jsonObject1.put("psntLandMark", snoDetailsObj1.getString(9));
				jsonObject1.put("psntPinCode", snoDetailsObj1.getString(10));
				jsonObject1.put("pmntCountry", snoDetailsObj1.getString(11));
				jsonObject1.put("stateName2", snoDetailsObj1.getString(12));
				jsonObject1.put("pmntCountry", snoDetailsObj1.getString(13));
				jsonObject1.put("pmntDistrictName2", snoDetailsObj1.getString(14));
				jsonObject1.put("pmntMobileNo", snoDetailsObj1.getString(15));
				jsonObject1.put("pmntAltMobileNo", snoDetailsObj1.getString(16));
				jsonObject1.put("pmntPostalAdrs", snoDetailsObj1.getString(17));
				jsonObject1.put("pmntLandMark", snoDetailsObj1.getString(18));
				jsonObject1.put("pmntPinCode", snoDetailsObj1.getString(19));
				jsonObject1.put("cratedOn", snoDetailsObj1.getString(20));
				jsonObject1.put("updatedOn", snoDetailsObj1.getString(21));
				jsonObject1.put("statusFlag", snoDetailsObj1.getString(22));
				details.put("addressDetailsData", jsonObject1);
			}
			while (snoDetailsObj2.next()) {
				jsonObject2 = new JSONObject();
				jsonObject2.put("cpdQualificationId", snoDetailsObj2.getString(1));
				jsonObject2.put("cpdUserId", snoDetailsObj2.getString(2));
				jsonObject2.put("qualificationId", snoDetailsObj2.getString(3));
				jsonObject2.put("qualicationName", snoDetailsObj2.getString(4));
				jsonObject2.put("universityName", snoDetailsObj2.getString(5));
				jsonObject2.put("totalMarks", snoDetailsObj2.getString(6));
				jsonObject2.put("marksObtained", snoDetailsObj2.getString(7));
				jsonObject2.put("passingYear", snoDetailsObj2.getString(8));
//				System.out.println("unparseable date"+snoDetailsObj2.getString(8));
//				Date toDate3 = inputFormat.parse(snoDetailsObj2.getString(8)); // Assuming snoDetailsObj3.getString(6) returns a String representation of date in "yyyy-MM-dd HH:mm:ss" format
//	            String formattedDate3 = outputFormat.format(toDate3);
//	            jsonObject2.put("passingYear", formattedDate3);
				jsonObject2.put("markPercentage", snoDetailsObj2.getString(9));
				jsonObject2.put("couseDuration", snoDetailsObj2.getString(10));
				jsonObject2.put("createdOn", snoDetailsObj2.getString(11));
				jsonObject2.put("updatedOn", snoDetailsObj2.getString(12));
				jsonObject2.put("statusFlag", snoDetailsObj2.getString(13));
				jsonObject2.put("qualificationDoc", snoDetailsObj2.getString(14)!=null ? snoDetailsObj2.getString(14):"NA"  );
				jsonArray1.put(jsonObject2);
			}
			while (snoDetailsObj3.next()) {
				jsonObject3 = new JSONObject();
				jsonObject3.put("cpdExperienceId", snoDetailsObj3.getString(1));
				jsonObject3.put("cpduserId", snoDetailsObj3.getString(2));
				jsonObject3.put("nameOfEmployee", snoDetailsObj3.getString(3));
				jsonObject3.put("postHeld", snoDetailsObj3.getString(4));
				jsonObject3.put("fromDate",snoDetailsObj3.getString(5));
//				Date toDate1 = inputFormat.parse(snoDetailsObj3.getString(5)); // Assuming snoDetailsObj3.getString(6) returns a String representation of date in "yyyy-MM-dd HH:mm:ss" format
//	            String formattedDate1 = outputFormat.format(toDate1);
//	            jsonObject3.put("fromDate", formattedDate1);
				jsonObject3.put("toDate", snoDetailsObj3.getString(6));
//				Date toDate = inputFormat.parse(snoDetailsObj3.getString(6)); // Assuming snoDetailsObj3.getString(6) returns a String representation of date in "yyyy-MM-dd HH:mm:ss" format
//	            String formattedDate = outputFormat.format(toDate);
//	            jsonObject3.put("toDate", formattedDate);
				jsonObject3.put("totalExpericen", snoDetailsObj3.getString(7));
				jsonObject3.put("createdOn", snoDetailsObj3.getString(8));
				jsonObject3.put("updatedOn", snoDetailsObj3.getString(9)!=null ? snoDetailsObj3.getLong(9) :"");
				jsonObject3.put("statuFlag", snoDetailsObj3.getString(10));
				jsonObject3.put("experienceDoc", snoDetailsObj3.getString(11)!=null ? snoDetailsObj3.getString(11):"NA");
				jsonArray2.put(jsonObject3);
			}
			while (snoDetailsObj4.next()) {
				jsonObject4 = new JSONObject();
				jsonObject4.put("specialityCode", snoDetailsObj4.getString(1));
				jsonObject4.put("specialityName", snoDetailsObj4.getString(2));
				jsonObject4.put("spcicalityDoc", snoDetailsObj4.getString(3)!=null ? snoDetailsObj4.getString(3):"NA");
				jsonArray3.put(jsonObject4);
			}
			
			details.put("professionalQuaDataList", jsonArray1);
			details.put("experienceDataList", jsonArray2);
			details.put("spcialityDetailList", jsonArray3);
			System.out.println(jsonArray1);
			System.out.println(jsonArray2);
			System.out.println(jsonArray3);
		} catch (Exception e) {
			logger.error("Error in previewDetails method of CpdRegistrationPreviewServiceImpl class:", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
				if (snoDetailsObj2 != null) {
					snoDetailsObj2.close();
				}
				if (snoDetailsObj3 != null) {
					snoDetailsObj3.close();
				}
			} catch (Exception e2) {
				logger.error("Error in previewDetails method of CpdRegistrationPreviewServiceImpl class:", e2);
			}

		} 
		
		return details.toString();
	}


	@Override
	public void commonDownloadMethod(String fileName, String prifix, String userid, HttpServletResponse response) throws IOException {
		
		CommonFileUpload.commonDownloadMethodForCPD(fileName,prifix,userid,response);
	}



}
