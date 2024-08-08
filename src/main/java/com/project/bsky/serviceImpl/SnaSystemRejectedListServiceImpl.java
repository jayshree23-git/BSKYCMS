/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.ClaimsQueriedToHospitalBySNOBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SystemRejQueryCpdBean;
import com.project.bsky.service.SnaSystemRejectedListService;
import com.project.bsky.util.DaysBetweenDates;

/**
 * @author hrusikesh.mohanty
 *
 */
@SuppressWarnings("deprecation")
@Service
public class SnaSystemRejectedListServiceImpl implements SnaSystemRejectedListService {

	private final Logger logger;

	@Autowired
	public SnaSystemRejectedListServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<ClaimsQueriedToHospitalBySNOBean> getSnarejectedlistdata(String hospitalcoderejected, String fromDate,
			String toDate, String package1, String packageCodedata, String uRN,String schemeid, String schemecategoryid) {
		List<ClaimsQueriedToHospitalBySNOBean> Snasystemrejetcedlist = new ArrayList<ClaimsQueriedToHospitalBySNOBean>();
		if (package1.equals("")) {
			package1 = "";
		}
		Long schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemecatId = Long.parseLong(schemecategoryid);
		} else {
			schemecatId = null;
		}
		ResultSet snarejectedlist = null;
		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNASYS_REJECTED_LIST")
					.registerStoredProcedureParameter("p_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_actioncode", "V");
			storedProcedure.setParameter("p_hospitalcode", hospitalcoderejected.trim());
			storedProcedure.setParameter("p_from_date", fromDate);
			storedProcedure.setParameter("p_to_date", toDate);
			storedProcedure.setParameter("p_packagecode", package1.trim());
			storedProcedure.setParameter("p_packagename", packageCodedata.trim());
			storedProcedure.setParameter("p_urn", uRN.trim());
			storedProcedure.setParameter("P_SCHEME_ID", Long.parseLong(schemeid));
			storedProcedure.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedure.execute();
			snarejectedlist = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			while (snarejectedlist.next()) {
				ClaimsQueriedToHospitalBySNOBean resBean = new ClaimsQueriedToHospitalBySNOBean();
				resBean.setClaimID(snarejectedlist.getLong(1));
				resBean.setURN(snarejectedlist.getString(2));
				resBean.setClaimStatus(snarejectedlist.getString(3).toString());
				resBean.setPendingAt(snarejectedlist.getString(4).toString());
				resBean.setSnoapprovedamount(snarejectedlist.getString(6));
				resBean.setPatientName(snarejectedlist.getString(7).toString());
				resBean.setPackageCode(snarejectedlist.getString(8).toString().substring(4));
				resBean.setPackageName(snarejectedlist.getString(9).toString());
				String Dateofadmission = snarejectedlist.getString(10);
				String a = Dateofadmission.substring(0, 2);
				String b = Dateofadmission.substring(2, 4);
				String c = Dateofadmission.substring(4, 8);
				String d = a + "/" + b + "/" + c;
				Date f = new SimpleDateFormat("dd/MM/yyyy").parse(d);
				SimpleDateFormat g = new SimpleDateFormat("dd-MMM-yyyy");
				String h = g.format(f);
				resBean.setDateofadmission(h);
				resBean.setUpdateon(snarejectedlist.getString(11));
				String getdat = snarejectedlist.getString(11).substring(0, 10);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate localDate = LocalDate.parse(getdat, formatter);
				Date date1 = java.util.Date
						.from(localDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String strDate = sdf.format(date1);
				int days = DaysBetweenDates.DaysBetweenDatesCPD_SNA(sdf.parse(strDate));
				resBean.setGetDaysleft(String.valueOf(days) + "days left");
				resBean.setClaim_no(snarejectedlist.getString(12).toString());
				String actualdateofadmisssionString = snarejectedlist.getString(13);
				String q11 = actualdateofadmisssionString.substring(0, 2);
				String q21 = actualdateofadmisssionString.substring(2, 4);
				String q31 = actualdateofadmisssionString.substring(4, 8);
				String q41 = q11 + "/" + q21 + "/" + q31;
				Date condition1 = new SimpleDateFormat("dd/MM/yyyy").parse(q41);
				SimpleDateFormat ss1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d111 = ss1.format(condition1);
				resBean.setActualdateofadmission(d111);
				String actualdateofdischargeString = snarejectedlist.getString(14);
				String q111 = actualdateofdischargeString.substring(0, 2);
				String q211 = actualdateofdischargeString.substring(2, 4);
				String q311 = actualdateofdischargeString.substring(4, 8);
				String q411 = q111 + "/" + q211 + "/" + q311;
				Date condition11 = new SimpleDateFormat("dd/MM/yyyy").parse(q411);
				SimpleDateFormat ss11 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1111 = ss11.format(condition11);
				resBean.setActualdateofdischarge(d1111);
				resBean.setTransactiondetailsid(snarejectedlist.getString(15).toString());
				resBean.setNoncomplFlag(snarejectedlist.getString(16));
				if (snarejectedlist.getString(17) != null && snarejectedlist.getString(17) != "") {
					resBean.setBtnVisibleBy(snarejectedlist.getString(17));
					String getdate = snarejectedlist.getString(17);
					Date date5 = new Date(getdate);
					SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
					String strDate1 = formatter1.format(date5);
					int days1 = DaysBetweenDates.daysCountBetweenDates(formatter1.parse(strDate1));
					resBean.setDaysRemaining(String.valueOf(days1) + " days left");
					resBean.setDays(String.valueOf(days1));
				}
				resBean.setInvoiceno(snarejectedlist.getString(18));
				String dateofdischargeString = snarejectedlist.getString(19);
				String q1 = dateofdischargeString.substring(0, 2);
				String q2 = dateofdischargeString.substring(2, 4);
				String q3 = dateofdischargeString.substring(4, 8);
				String q4 = q1 + "/" + q2 + "/" + q3;
				Date condition = new SimpleDateFormat("dd/MM/yyyy").parse(q4);
				SimpleDateFormat ss = new SimpleDateFormat("dd-MMM-yyyy");
				String d11 = ss.format(condition);
				resBean.setDateofdischarge(d11);
				resBean.setCaseno(snarejectedlist.getString(20));
				Snasystemrejetcedlist.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getSnarejectedlistdata method of SnaSystemRejectedListServiceImpl :", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snarejectedlist != null) {
					snarejectedlist.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return Snasystemrejetcedlist;
	}

	@Override
	public Response saveRejectRequestSNA(SystemRejQueryCpdBean rejBean) throws ParseException {
		Response response = new Response();
		try {
			Integer claimsnoInteger = null;
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SYSREJ_DISCHARGE_SNA_ACT")
					.registerStoredProcedureParameter("p_transactiondetailsId", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statusflag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_assignedsna", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_description", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_createdby", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_createdon", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_by", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_status", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_transactiondetailsId", rejBean.getTransactiondetailsid());
			storedProcedureQuery.setParameter("p_urn", rejBean.getUrnNo().trim());
			storedProcedureQuery.setParameter("p_hospitalcode", rejBean.getHospitalcode());
			storedProcedureQuery.setParameter("p_statusflag", rejBean.getStatusflag());
			storedProcedureQuery.setParameter("p_assignedsna", Integer.parseInt("1914"));
			storedProcedureQuery.setParameter("p_description", rejBean.getDescription());
			storedProcedureQuery.setParameter("p_createdby", rejBean.getUserId());
			storedProcedureQuery.setParameter("p_createdon", new Date());
			storedProcedureQuery.setParameter("p_claim_by", rejBean.getClaimBy());
			storedProcedureQuery.setParameter("p_claim_status", rejBean.getClaimstatus());
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
			if (claimsnoInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Requested Successfully");
			}
		} catch (Exception e) {
			logger.error("Exception occured in saveRejectRequestSNA method of SnaSystemRejectedListServiceImpl :", e);
			e.printStackTrace();
		}
		return response;
	}

}
