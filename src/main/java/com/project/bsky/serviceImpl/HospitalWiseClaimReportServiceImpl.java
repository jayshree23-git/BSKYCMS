package com.project.bsky.serviceImpl;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.project.bsky.bean.HospitalWiseClaimreportDetails;
import com.project.bsky.bean.Hospitalwisedischargecount;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.service.HospitalWiseClaimReportService;

@Service
public class HospitalWiseClaimReportServiceImpl implements HospitalWiseClaimReportService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private HospitalInformationRepository hospitalInformationRepository;
	
	@Autowired
	private Logger logger;
	
	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();
	
	@Override
	public List<HospitalInformation> getDetails() {
		List<HospitalInformation> hospdetails=new ArrayList<HospitalInformation>();
		List<Object[]> hospdetail=hospitalInformationRepository.gethospital();
		for(Object[] x:hospdetail) {
			HospitalInformation hospitalInformation=new HospitalInformation();
			hospitalInformation.setHospitalName(x[0]+" ("+x[1]+")");
			hospitalInformation.setHospitalCode(x[1].toString());
			hospdetails.add(hospitalInformation);
		}
		return hospdetails;
	}

	@Override
	public List<Object> search(Long userId, Date fromDate, Date toDate, String hospitalId) {
		ResultSet hospitalwisecount = null;
		List<Object> hospitallist = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_WISE_DISCHARGE_COUNT_RPT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_HOSPITALCODE",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_HOSPITALCODE", hospitalId);
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.execute();
			
			hospitalwisecount = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			////System.out.println("HospitalInfo Object is: "+hospitalwisecount);
			
			while (hospitalwisecount.next()) {
				Hospitalwisedischargecount hospitalwisedischargecount=new Hospitalwisedischargecount();
				hospitalwisedischargecount.setHospitalCode(hospitalwisecount.getString(1));
				hospitalwisedischargecount.setHospitalName(hospitalwisecount.getString(2));
				hospitalwisedischargecount.setTotalDischarge(hospitalwisecount.getString(3));
				hospitalwisedischargecount.setClaimsubmitted(hospitalwisecount.getString(4));
				////System.out.println(hospitalwisedischargecount);
				hospitallist.add(hospitalwisedischargecount);
				}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}finally {
			try {
				if (hospitalwisecount != null) {
					hospitalwisecount.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return hospitallist;
	}
	@Override
	public List<Object> details(Long userId, Date fromDate, Date toDate, String hospitalId, String serchby) {
		ResultSet hospitalwisedetails = null;
		List<Object> hospitallistdetails = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_WISE_DISCHARGE_COUNT_RPT_DETAILS")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_HOSPITALCODE",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("p_event_name",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);
			
			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_HOSPITALCODE", hospitalId);
			storedProcedureQuery.setParameter("p_event_name", serchby);
			storedProcedureQuery.execute();
			
			hospitalwisedetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			////System.out.println("HospitalInfo Object is: "+hospitalwisedetails);
			
			while (hospitalwisedetails.next()) {
				HospitalWiseClaimreportDetails hospitalWiseClaimreportDetails=new HospitalWiseClaimreportDetails();
				hospitalWiseClaimreportDetails.setClaimId(hospitalwisedetails.getString(1));
				hospitalWiseClaimreportDetails.setClaimNo(hospitalwisedetails.getString(2));
				hospitalWiseClaimreportDetails.setUrn(hospitalwisedetails.getString(3));
				hospitalWiseClaimreportDetails.setPackageName(hospitalwisedetails.getString(4));
				hospitalWiseClaimreportDetails.setHospitalName(hospitalwisedetails.getString(5));
				String s=hospitalwisedetails.getString(6);
				String s1 = s.substring(0,2);
				String s2 = s.substring(2,4);
				String s3 = s.substring(4,8);
				String s4 = s1 + "/" + s2 + "/" + s3;
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(s4);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				String actualadmission = sdf.format(date1);
				hospitalWiseClaimreportDetails.setActualDateOfadmission(actualadmission);
				
				String str=hospitalwisedetails.getString(7);
				String str1 = str.substring(0,2);
				String str2 = str.substring(2,4);
				String str3 = str.substring(4,8);
				String str4 = str1 + "/" + str2 + "/" + str3;
				Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(str4);
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
				String actualdateofdis = sdf2.format(date2);
				hospitalWiseClaimreportDetails.setActualDateOfDischarge(actualdateofdis);
				hospitalWiseClaimreportDetails.setInvoiceno(hospitalwisedetails.getString(8));
				hospitalWiseClaimreportDetails.setPackagecode(hospitalwisedetails.getString(9));
//				hospitalWiseClaimreportDetails.setActualDateOfadmission((hospitalwisedetails.getString(6)));
//				hospitalWiseClaimreportDetails.setActualDateOfDischarge((hospitalwisedetails.getString(7)));
//				hospitalWiseClaimreportDetails.setDateOfAdmission((hospitalwisedetails.getString(8)));
//				hospitalWiseClaimreportDetails.setDateOfDischarge((hospitalwisedetails.getString(9)));
				hospitalWiseClaimreportDetails.setAuthorizedCode(hospitalwisedetails.getString(10));
				hospitalWiseClaimreportDetails.setHospitalCode(hospitalwisedetails.getString(11));
				hospitalWiseClaimreportDetails.setClaimsubmitted(hospitalwisedetails.getString(12));
				hospitalWiseClaimreportDetails.setClaimstatus(hospitalwisedetails.getString(13));
				////System.out.println(hospitalWiseClaimreportDetails);
				hospitallistdetails.add(hospitalWiseClaimreportDetails);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}finally {
			try {
				if (hospitalwisedetails != null) {
					hospitalwisedetails.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return hospitallistdetails;
	}
}
