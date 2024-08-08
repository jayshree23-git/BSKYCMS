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

import org.springframework.stereotype.Service;

import com.project.bsky.bean.DischargedetailsHistorybean;
import com.project.bsky.service.DischargedetailshistoryService;
import com.project.bsky.util.CommonFileUpload;
@Service
public class DischargedetailshistoryServiceImpl implements DischargedetailshistoryService{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Object> getClaimdetails(String transactiondetailsid) throws Exception {
		List<Object> claimRejectedList = new ArrayList<Object>();
		ResultSet rejetedlist = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_DISCHARG_LIST_HISTORY")
					.registerStoredProcedureParameter("p_id", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class,ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("p_id",transactiondetailsid.trim());
			storedProcedure.execute();
			rejetedlist =(ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			
			while(rejetedlist.next()) {
				DischargedetailsHistorybean dischargedata=new DischargedetailsHistorybean();
				dischargedata.setUrn(rejetedlist.getString(1));
//				dischargedata.setActualdateofadmission(DateFormat.DateString(rejetedlist.getString(2)));
				String actualdateofdischarge=rejetedlist.getString(2);	
				String s1 = actualdateofdischarge.substring(0, 2);
				String s2 = actualdateofdischarge.substring(2, 4);
				String s3 = actualdateofdischarge.substring(4, 8);
				String s4 = s1 + "/" + s2 + "/" + s3;
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(s4);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				String d = sdf.format(date1);
				dischargedata.setActualdateofadmission(d);
//				dischargedata.setDateofdischarge(DateFormat.DateString(rejetedlist.getString(3)));
				String dateofdishcharge=rejetedlist.getString(3);	
				String s11 = dateofdishcharge.substring(0, 2);
				String s21 = dateofdishcharge.substring(2, 4);
				String s31 = dateofdishcharge.substring(4, 8);
				String s41 = s11 + "/" + s21 + "/" + s31;
				Date date11 = new SimpleDateFormat("dd/MM/yyyy").parse(s41);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1 = sdf1.format(date11);
				dischargedata.setActualdateofdischarge(d1);
				dischargedata.setStatename(rejetedlist.getString(4));
				dischargedata.setDistrictname(rejetedlist.getString(5));
				dischargedata.setBlockname(rejetedlist.getString(6));
				dischargedata.setVillagename(rejetedlist.getString(7));
				dischargedata.setHospitalname(rejetedlist.getString(8));
				dischargedata.setPatientname(rejetedlist.getString(9));
//				dischargedata.setGender(rejetedlist.getString(10));
				String genderString = rejetedlist.getString(10);
				if(genderString.equals("M")) {
					dischargedata.setGender("Male");
				}else if(genderString.equals("F")){
					dischargedata.setGender("Female");
				}
				dischargedata.setAge(rejetedlist.getString(11));
				dischargedata.setProcedurename(rejetedlist.getString(12));
				dischargedata.setPackagename(rejetedlist.getString(13));
//				dischargedata.setNoofdays(rejetedlist.getLong(14));
				long noofStringdayscalculationString = CommonFileUpload.calculateNoOfDays(rejetedlist.getString(20),rejetedlist.getString(21));
				dischargedata.setNoofdays(noofStringdayscalculationString);
				dischargedata.setInvoiceno(rejetedlist.getLong(15));
				dischargedata.setTotalamountclaimed(rejetedlist.getString(16));
				dischargedata.setTotalamountblocked(rejetedlist.getString(17));
				dischargedata.setFamilyheadname(rejetedlist.getString(18));
				dischargedata.setVerifiername(rejetedlist.getString(19));
//				dischargedata.setDateofadmission(DateFormat.DateString(rejetedlist.getString(20)));
				String setDateofadmission=rejetedlist.getString(20);
				String s111 = setDateofadmission.substring(0, 2);
				String s211 = setDateofadmission.substring(2, 4);
				String s311 = setDateofadmission.substring(4, 8);
				String s411 = s111 + "/" + s211 + "/" + s311;
				Date date111 = new SimpleDateFormat("dd/MM/yyyy").parse(s411);
				SimpleDateFormat sdf11 = new SimpleDateFormat("dd-MMM-yyyy");
				String d111 = sdf11.format(date111);
				dischargedata.setDateofadmission(d111);
//				dischargedata.setDateofdischarge(DateFormat.DateString(rejetedlist.getString(21)));
				String setDateofdischarge =rejetedlist.getString(21); 
				String s1111 = setDateofdischarge.substring(0, 2);
				String s2111 = setDateofdischarge.substring(2, 4);
				String s3111 = setDateofdischarge.substring(4, 8);
				String s4111 = s1111 + "/" + s2111 + "/" + s3111;
				Date date1111 = new SimpleDateFormat("dd/MM/yyyy").parse(s4111);
				SimpleDateFormat sdf111 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1111 = sdf111.format(date1111);
				dischargedata.setDateofdischarge(d1111);
//				dischargedata.setMortality(rejetedlist.getString(22));
				String moratlirtString= rejetedlist.getString(22);
				if (moratlirtString.equals("N")) {
					dischargedata.setMortality("No");
				}else if(moratlirtString.equals("Y") || moratlirtString.equals("1") || moratlirtString.equals("2") || moratlirtString.equals("3")) {
					dischargedata.setMortality("Yes");
				}
				dischargedata.setReferralcode(rejetedlist.getString(23));
				dischargedata.setAuthorizedcode(rejetedlist.getString(24));
				dischargedata.setNabh(rejetedlist.getString(25));
				dischargedata.setImplant_data(rejetedlist.getString(26));
				dischargedata.setPATIENTPHONENO(rejetedlist.getLong(27));
				dischargedata.setHospitalcode(rejetedlist.getString(28));
				dischargedata.setCurrenttotalamount(rejetedlist.getString(29));
				dischargedata.setDischargeslip(rejetedlist.getString(30));
				dischargedata.setAddtional_doc(rejetedlist.getString(31));
				dischargedata.setAdditional_doc1(rejetedlist.getString(32));
				dischargedata.setAdditional_doc2(rejetedlist.getString(33));
				dischargedata.setPatient_photo(rejetedlist.getString(34));
				dischargedata.setIntra_surgery_photo(rejetedlist.getString(35));
				dischargedata.setSpecimen_removal_photo(rejetedlist.getString(36));
				dischargedata.setPresurgeryphoto(rejetedlist.getString(37));
				dischargedata.setPostsurgeryphoto(rejetedlist.getString(38));
				dischargedata.setClaim_no(rejetedlist.getString(39));
				dischargedata.setCaseno(rejetedlist.getString(40));
				dischargedata.setClaimbilldate(rejetedlist.getString(41));
				dischargedata.setTmscaseno(rejetedlist.getString(42));
				claimRejectedList.add(dischargedata);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (rejetedlist != null)
					rejetedlist.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				throw e2;
			}
		}
		return claimRejectedList;
	}
	
}
