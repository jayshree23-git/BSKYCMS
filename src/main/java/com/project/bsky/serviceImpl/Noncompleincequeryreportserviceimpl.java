/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
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

import org.springframework.stereotype.Service;

import com.project.bsky.bean.ClaimsQueriedToHospitalBySNOBean;
import com.project.bsky.service.Noncompleincequeryreportservice;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.DaysBetweenDates;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class Noncompleincequeryreportserviceimpl implements Noncompleincequeryreportservice {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<ClaimsQueriedToHospitalBySNOBean> getnoncompleincequerysno(String sno, String fromDate, String toDate,
			String package1, String packageName, String uRN) {
		{
			List<ClaimsQueriedToHospitalBySNOBean> Snasystemrejetcedlist = new ArrayList<ClaimsQueriedToHospitalBySNOBean>();

			if (package1.equals("")) {
				package1 = "";
			}
//			 else if(!package1.equals("")) {
//				 package1="0500"+package1;
//			 }
			ResultSet snarejectedlist = null;
			try {

				StoredProcedureQuery storedProcedure = this.entityManager
						.createStoredProcedureQuery("USP_NON_COMPLIANCE_QUERY_SNA")
						.registerStoredProcedureParameter("p_sno", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_packagecode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_packagename", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

				storedProcedure.setParameter("p_sno", sno);
				storedProcedure.setParameter("p_from_date", fromDate);
				storedProcedure.setParameter("p_to_date", toDate);
				storedProcedure.setParameter("p_packagecode", package1.trim());
				storedProcedure.setParameter("p_packagename", packageName.trim());
				storedProcedure.setParameter("p_urn", uRN.trim());
				storedProcedure.execute();
				snarejectedlist = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
				while (snarejectedlist.next()) {
					ClaimsQueriedToHospitalBySNOBean resBean = new ClaimsQueriedToHospitalBySNOBean();
					resBean.setClaimID(snarejectedlist.getLong(1));
					resBean.setURN(snarejectedlist.getString(2));
					resBean.setClaimStatus(snarejectedlist.getString(3).toString());
					resBean.setPendingAt(snarejectedlist.getString(4).toString());
//					resBean.setCreatedon(snarejectedlist.getString(5).toString());;
					resBean.setSnoapprovedamount(snarejectedlist.getString(6));
					resBean.setPatientName(snarejectedlist.getString(7).toString());
					resBean.setPackageCode(snarejectedlist.getString(8).toString());
					resBean.setPackageName(snarejectedlist.getString(9).toString());
					
					if(snarejectedlist.getString(10)!=null) {
						resBean.setDateofadmission(DateFormat.formatDateFun(snarejectedlist.getString(10)));
					}
					
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
					if(snarejectedlist.getString(13)!=null) {
						resBean.setActualdateofadmission(DateFormat.formatDateFun(snarejectedlist.getString(13)));
					}
					if(snarejectedlist.getString(14)!=null) {
						resBean.setActualdateofdischarge(DateFormat.formatDateFun(snarejectedlist.getString(14)));
					}
					resBean.setTransactiondetailsid(snarejectedlist.getString(15).toString());
					resBean.setNoncomplFlag(snarejectedlist.getString(16));
//					if (snarejectedlist.getString(17) != null && snarejectedlist.getString(17) != "") {
//						resBean.setBtnVisibleBy(snarejectedlist.getString(17));
//						String getdate = snarejectedlist.getString(17);
//						Date date5 = new Date(getdate);
//						SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
//						String strDate1 = formatter1.format(date5);
//						int days1 = DaysBetweenDates.daysCountBetweenDates(formatter1.parse(strDate1));
//						resBean.setDaysRemaining(String.valueOf(days1) + " days left");
//						resBean.setDays(String.valueOf(days1));
//					}
					
					resBean.setInvoiceno(snarejectedlist.getString(18));
					if(snarejectedlist.getString(19)!=null) {
						resBean.setDateofdischarge(DateFormat.formatDateFun(snarejectedlist.getString(19)));
					}
					resBean.setHospitalCode(snarejectedlist.getString(20));
					resBean.setHospitalName(snarejectedlist.getString(21));
					Snasystemrejetcedlist.add(resBean);

				}
			} catch (Exception e) {
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
	}

}
