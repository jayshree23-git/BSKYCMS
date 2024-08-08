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

import com.project.bsky.bean.DischargeListHospital;
import com.project.bsky.service.DischargeReportHospitalService;
@Service
public class DischargeReportHospitalServiceImpl  implements DischargeReportHospitalService{
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Override
	public List<Object> getdischargwdetails(Long userId, String searchBy, String fieldValue,String groupId) {
//		//System.out.println(userId + "\t" + searchBy + "\t" + fieldValue+ "\t"+groupId);
		List<Object> dichrgeedList = new ArrayList<Object>();
		ResultSet dichrgelist = null;
		String authrorizedcode=null;
		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_DISCHARGE_SERCH_URN")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_searchBy", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fieldValue", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_groupId",String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_P_MSGOUT",void.class, ParameterMode.REF_CURSOR);
			
			storedProcedure.setParameter("p_user_id", userId);
			storedProcedure.setParameter("p_searchBy", searchBy);
			storedProcedure.setParameter("p_fieldValue", fieldValue);
			storedProcedure.setParameter("P_groupId",groupId);
			storedProcedure.execute();
			dichrgelist = (ResultSet) storedProcedure.getOutputParameterValue("p_P_MSGOUT");
			while (dichrgelist.next()) {
				DischargeListHospital dislist =new DischargeListHospital();
				dislist.setUrn(dichrgelist.getString(1));
				dislist.setPatientname(dichrgelist.getString(2));
				dislist.setInvoiceno(dichrgelist.getString(3));
				dislist.setPackagecode(dichrgelist.getString(4));
				dislist.setPackagename(dichrgelist.getString(5));
//				dislist.setActualdateofdischarge(dichrgelist.getString(6));	
				String s=dichrgelist.getString(6);
				String s1 = s.substring(0, 2);
				String s2 = s.substring(2, 4);
				String s3 = s.substring(4, 8);
				String s4 = s1 + "/" + s2 + "/" + s3;
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(s4);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				String d = sdf.format(date1);
				dislist.setDischaregedate(d);
				dislist.setClaim_raised_by(dichrgelist.getString(7));
				dislist.setCurrenttotalamount(dichrgelist.getLong(8));
//				dislist.setClaimraisestatus(dichrgelist.getString(9));
				String disdataString=dichrgelist.getString(9);
				if(disdataString.equalsIgnoreCase("1"))
					dislist.setClaimraisestatus("Yes");
				else if (disdataString.equalsIgnoreCase("0")) {
					dislist.setClaimraisestatus("No");	
				}
				dislist.setHospitalname(dichrgelist.getString(10));
				dislist.setTransactiondetailsid(dichrgelist.getString(11));
//				dislist.setAuthorizedcode(dichrgelist.getString(12));
				authrorizedcode=dichrgelist.getString(12);
				if(authrorizedcode !=null) {
					dislist.setAuthorizedcode(authrorizedcode);
				}
				dislist.setHospitalcode(dichrgelist.getString(13));
//				dislist.setActualdateofdischarge(d)
				String actualdateString=dichrgelist.getString(14);
				String s11 = actualdateString.substring(0, 2);
				String s21 = actualdateString.substring(2, 4);
				String s31 = actualdateString.substring(4, 8);
				String s41 = s11 + "/" + s21 + "/" + s31;
				Date date11 = new SimpleDateFormat("dd/MM/yyyy").parse(s41);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1 = sdf1.format(date11);
				dislist.setActualdateofdischarge(d1);
				dislist.setCaseno(dichrgelist.getString(15));
				dislist.setClaimno(dichrgelist.getString(16)==null?"N/A":dichrgelist.getString(16));
				dislist.setHopitalclmcaseno(dichrgelist.getString(17)==null?"N/A":dichrgelist.getString(17));
				dislist.setClaimbilldate(dichrgelist.getString(18));
				dichrgeedList.add(dislist);
				////System.out.println(dislist);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (dichrgelist != null) {
					dichrgelist.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return dichrgeedList;
	}

//	@Override
//	public List<Object> getdischargwdetails(String urn, String groupId,String hospitalcode,String userID) {
//		List<Object> dichrgeedList = new ArrayList<Object>();
//		ResultSet dichrgelist = null;
//		String authrorizedcode=null;
//		try {
//
//			StoredProcedureQuery storedProcedure = this.entityManager
//					.createStoredProcedureQuery("USP_DISCHARGE_SERCH_URN")
//					.registerStoredProcedureParameter("p_urn",String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_groupId",String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_hospitalcode",String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_user_id",String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_P_MSGOUT",void.class, ParameterMode.REF_CURSOR);
//			
//			storedProcedure.setParameter("p_urn",urn.trim());
//			storedProcedure.setParameter("P_groupId",groupId.trim());
//			storedProcedure.setParameter("p_hospitalcode",hospitalcode.trim());
//			storedProcedure.setParameter("p_user_id",userID.trim());
//			storedProcedure.execute();
//			dichrgelist = (ResultSet) storedProcedure.getOutputParameterValue("p_P_MSGOUT");
//			while (dichrgelist.next()) {
//				DischargeListHospital dislist =new DischargeListHospital();
//				dislist.setUrn(dichrgelist.getString(1));
//				dislist.setPatientname(dichrgelist.getString(2));
//				dislist.setInvoiceno(dichrgelist.getString(3));
//				dislist.setPackagecode(dichrgelist.getString(4));
//				dislist.setPackagename(dichrgelist.getString(5));
////				dislist.setActualdateofdischarge(dichrgelist.getString(6));	
//				String s=dichrgelist.getString(6);
//				String s1 = s.substring(0, 2);
//				String s2 = s.substring(2, 4);
//				String s3 = s.substring(4, 8);
//				String s4 = s1 + "/" + s2 + "/" + s3;
//				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(s4);
//				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
//				String d = sdf.format(date1);
//				dislist.setDischaregedate(d);
//				dislist.setClaim_raised_by(dichrgelist.getString(7));
//				dislist.setCurrenttotalamount(dichrgelist.getLong(8));
////				dislist.setClaimraisestatus(dichrgelist.getString(9));
//				String disdataString=dichrgelist.getString(9);
//				if(disdataString.equalsIgnoreCase("1"))
//					dislist.setClaimraisestatus("Yes");
//				else if (disdataString.equalsIgnoreCase("0")) {
//					dislist.setClaimraisestatus("No");	
//				}
//				dislist.setHospitalname(dichrgelist.getString(10));
//				dislist.setTransactiondetailsid(dichrgelist.getString(11));
////				dislist.setAuthorizedcode(dichrgelist.getString(12));
//				authrorizedcode=dichrgelist.getString(12);
//				if(authrorizedcode !=null) {
//					dislist.setAuthorizedcode(authrorizedcode);
//				}
//				dislist.setHospitalcode(dichrgelist.getString(13));
////				dislist.setActualdateofdischarge(d)
//				String actualdateString=dichrgelist.getString(14);
//				String s11 = actualdateString.substring(0, 2);
//				String s21 = actualdateString.substring(2, 4);
//				String s31 = actualdateString.substring(4, 8);
//				String s41 = s11 + "/" + s21 + "/" + s31;
//				Date date11 = new SimpleDateFormat("dd/MM/yyyy").parse(s41);
//				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
//				String d1 = sdf1.format(date11);
//				dislist.setActualdateofdischarge(d1);
//				dichrgeedList.add(dislist);
//				////System.out.println(dislist);
//			}
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		} finally {
//			try {
//				if (dichrgelist != null) {
//					dichrgelist.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//
//		}
//		return dichrgeedList;
//	}

}
