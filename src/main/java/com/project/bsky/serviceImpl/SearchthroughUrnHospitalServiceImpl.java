/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.searchThroughUrnlistBean;
import com.project.bsky.service.SearchthroughUrnHospitalService;
import com.project.bsky.util.DateFormat;

/**
 * @author hrusikesh.mohanty
 *
 */
@Service
public class SearchthroughUrnHospitalServiceImpl implements SearchthroughUrnHospitalService {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

//	
//	@Override
//	public String getdetailsurn(String urn) throws Exception{
//		
//		JSONArray jsonArray = new JSONArray();
//		ResultSet snoDetailsObj = null;
//		JSONObject jsonObject;
//		JSONObject data = new JSONObject();
//
//
//		try {
//			StoredProcedureQuery storedProcedureQuery = this.entityManager
//					.createStoredProcedureQuery("SP_SEARCHDETAILS_URN")
//					.registerStoredProcedureParameter("urn", String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_P_MSGOUT", void.class,ParameterMode.REF_CURSOR);
//			
//			storedProcedureQuery.setParameter("urn", urn);
//			storedProcedureQuery.execute();
//			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
//			while (snoDetailsObj.next()) {
//				jsonObject = new JSONObject();
//				jsonObject.put("id",snoDetailsObj.getInt(1));
//				jsonObject.put("transactionid",snoDetailsObj.getLong(2));
//				jsonObject.put("URN",snoDetailsObj.getString(3));
//				jsonObject.put("age",snoDetailsObj.getString(4));
//				jsonObject.put("gender",snoDetailsObj.getString(5));
//				jsonObject.put("PatientName",snoDetailsObj.getString(6));
//				jsonObject.put("PackageCode",snoDetailsObj.getString(7));
//				jsonObject.put("PackageName",snoDetailsObj.getString(8));
//				jsonObject.put("CurrentTotalAmount",snoDetailsObj.getLong(9));
//				jsonObject.put("dateofadmission",snoDetailsObj.getString(10));
//				jsonObject.put("DateOfDischarge",snoDetailsObj.getString(11));
//				jsonObject.put("hospitalstateCode",snoDetailsObj.getInt(12));
//				jsonObject.put("actualdateofdischarge",snoDetailsObj.getString(13));
//				jsonObject.put("actualdateofadmission",snoDetailsObj.getString(14));
//				jsonObject.put("claimraisestatus",snoDetailsObj.getInt(15));
//				jsonArray.put(jsonObject);
//				data.put("data", jsonArray);
//				////System.out.println("okk value data"+jsonObject);
//			}	
//			}catch (Exception e) {
//				e.printStackTrace();
//				throw e;
//			}
//		return data.toString();
//	}
	@Override
	public List<Object> geturn(String urn) {
		List<Object> snoDetailsObj = new ArrayList<Object>();
		ResultSet urnlist = null;

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_SEARCHDETAILS_URN")
					.registerStoredProcedureParameter("P_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_urn", urn);
			storedProcedureQuery.execute();
			urnlist = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (urnlist.next()) {
				searchThroughUrnlistBean data = new searchThroughUrnlistBean();
				data.setID(urnlist.getInt(1));
				data.setTransactionid(urnlist.getInt(2));
				data.setURN(urnlist.getString(3));
				data.setAge(urnlist.getString(4));
				data.setGender(urnlist.getString(5));
				data.setPatientName(urnlist.getString(6));
				data.setPackageCode(urnlist.getString(7));
				data.setPackageName(urnlist.getString(8));
				data.setCurrentTotalAmount(urnlist.getLong(9));
//				data.setDateofadmission(urnlist.getString(10));
				String dateofadmissionString =urnlist.getString(10);
				String s11 = dateofadmissionString.substring(0, 2);
				String s21 = dateofadmissionString.substring(2, 4);
				String s31 = dateofadmissionString.substring(4, 8);
				String ssString1 = s11 + "/" + s21 + "/" + s31;
				Date date11 = new SimpleDateFormat("dd/MM/yyyy").parse(ssString1);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1 = sdf1.format(date11);
				data.setDateofadmission(d1);
//				data.setDateOfDischarge(urnlist.getString(11));
				String DateOfDischarge =urnlist.getString(11);
				String s1 = DateOfDischarge.substring(0, 2);
				String s2 = DateOfDischarge.substring(2, 4);
				String s3 = DateOfDischarge.substring(4, 8);
				String ss = s1 + "/" + s2 + "/" + s3;
				Date date = new SimpleDateFormat("dd/MM/yyyy").parse(ss);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				String d = sdf.format(date);
				data.setDateOfDischarge(d);
				data.setHospitalstateCode(urnlist.getLong(12));
				data.setActualdateofdischarge(DateFormat.FormatToDateString(urnlist.getString(13)));
				data.setActualdateofadmission(DateFormat.FormatToDateString(urnlist.getString(14)));
//				data.setClaimraisestatus(urnlist.getInt(15));
				String claimraisestatuS=urnlist.getString(15);
				if(claimraisestatuS.equals("0")) {
					data.setClaimraisestatus("NO");
				}else if(claimraisestatuS.equals("1")) {
					data.setClaimraisestatus("YES");
				}
				data.setHospitalname(urnlist.getString(16));
				data.setHospitalcode(urnlist.getString(17));
				data.setAuthorizedcode(urnlist.getString(18));
				data.setTransactiondetailsid(urnlist.getString(19));
				snoDetailsObj.add(data);
				////System.out.println(data);

			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return snoDetailsObj;
	}

}
