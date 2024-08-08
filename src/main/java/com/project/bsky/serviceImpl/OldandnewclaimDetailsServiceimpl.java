package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Service;

import com.project.bsky.bean.Hospitaldetailsforoldclaimandnewclaim;
import com.project.bsky.service.OldandnewclaimDetailsService;
import com.project.bsky.util.DateFormat;

@Service
public class OldandnewclaimDetailsServiceimpl implements OldandnewclaimDetailsService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Map<String, Object> detailsoldclaimandnewclaimforview(String urnnumber, String claimid, String selctedvalue,
			String claimnumber, String transactiondetailsid, String authorizedcode, String hospitalcode) {
		Map<String, Object> alldata = new HashMap<String, Object>();
		List<Object> hospitaldetails = new ArrayList<Object>();
		List<Object> treatmentdetails = new ArrayList<Object>();
		List<Object> preauthlogdata = new ArrayList<Object>();
		List<Object> treatmenthistory = new ArrayList<Object>();
		List<Object> actiontakenhistory = new ArrayList<Object>();
		ResultSet hospitaldetailsrs = null;
		ResultSet treatmentdetailrs = null;
		ResultSet preauthlogrs = null;
		ResultSet treatmenthistoryrs = null;
		ResultSet actiontakenhistoryrs = null;

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_OLD_NEW_CLAIM_DTLS")
					.registerStoredProcedureParameter("P_DATA", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_claimnumber", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_authorizedCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_hospitalCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_transactiondetailsid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_HOSPITALDTLS", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_TREATMENTDTLS", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_PREAUTHLOG", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_TREATMENTHST", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_ACTIONTAKENHST", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_DATA", urnnumber.trim());
			storedProcedureQuery.setParameter("P_claimnumber", claimnumber.trim());
			storedProcedureQuery.setParameter("P_authorizedCode", authorizedcode.trim());
			storedProcedureQuery.setParameter("P_hospitalCode", hospitalcode.trim());
			storedProcedureQuery.setParameter("P_transactiondetailsid", transactiondetailsid.trim());
			storedProcedureQuery.setParameter("p_claimid", claimid.trim());
			hospitaldetailsrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_HOSPITALDTLS");
			treatmentdetailrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_TREATMENTDTLS");
			preauthlogrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_PREAUTHLOG");
			treatmenthistoryrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_TREATMENTHST");
			actiontakenhistoryrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_ACTIONTAKENHST");
			while (hospitaldetailsrs.next()) {
				Hospitaldetailsforoldclaimandnewclaim hospitaldata = new Hospitaldetailsforoldclaimandnewclaim();
				hospitaldata.setHospital_codehsptl(hospitaldetailsrs.getString(1));
				hospitaldata.setHospital_namehsptl(hospitaldetailsrs.getString(2));
				hospitaldata.setUrnhsptl(hospitaldetailsrs.getString(3));
				hospitaldata.setInvoicenohsptl(hospitaldetailsrs.getString(4));
				hospitaldata.setClaim_nohsptl(hospitaldetailsrs.getString(5));
				hospitaldata.setState(hospitaldetailsrs.getString(6));
				hospitaldata.setDisctrict(hospitaldetailsrs.getString(7));
				hospitaldata.setBlock(hospitaldetailsrs.getString(8));
				hospitaldata.setPanchayat(hospitaldetailsrs.getString(9));
				hospitaldata.setVillagename(hospitaldetailsrs.getString(10));
				hospitaldata.setPatientname(hospitaldetailsrs.getString(11));
				hospitaldata.setAge(hospitaldetailsrs.getString(12));
				hospitaldata.setGender(hospitaldetailsrs.getString(13));
				hospitaldata.setPatientphonenumber(hospitaldetailsrs.getString(14));
				hospitaldetails.add(hospitaldata);
			}
			while (treatmentdetailrs.next()) {
				Hospitaldetailsforoldclaimandnewclaim treatmentdtls = new Hospitaldetailsforoldclaimandnewclaim();
				treatmentdtls.setDateofadmissiontrtmntdtls(DateFormat.formatDateFun(treatmentdetailrs.getString(1)));
				treatmentdtls.setDateofdischargetrtmntdtls(DateFormat.formatDateFun(treatmentdetailrs.getString(2)));
				treatmentdtls
						.setActualdateofadmissiontrtmntdtls(DateFormat.formatDateFun(treatmentdetailrs.getString(3)));
				treatmentdtls
						.setActualdateofdischargetrtmntdtls(DateFormat.formatDateFun(treatmentdetailrs.getString(4)));
				treatmentdtls.setTotalamountclaimedtrtmntdtls(treatmentdetailrs.getString(5));
				treatmentdtls.setTotalamountblockedtrtmntdtls(treatmentdetailrs.getString(6));
				treatmentdtls.setAuthorizedcodetrtmntdtls(
						treatmentdetailrs.getString(7) != null ? treatmentdetailrs.getString(7) : "N/A");
				treatmentdtls.setProcedurenametrtmntdtls(
						treatmentdetailrs.getString(8) != null ? treatmentdetailrs.getString(8) : "N/A");
				treatmentdtls.setPackagecodetrtmntdtls(
						treatmentdetailrs.getString(9) != null ? treatmentdetailrs.getString(9) : "N/A");
				treatmentdtls.setPackagenametrtmntdtls(
						treatmentdetailrs.getString(10) != null ? treatmentdetailrs.getString(10) : "N/A");
				treatmentdtls.setCasenotrtmntdtls(
						treatmentdetailrs.getString(11) != null ? treatmentdetailrs.getString(11) : "N/A");
				treatmentdtls.setClaim_case_notrtmntdtls(
						treatmentdetailrs.getString(12) != null ? treatmentdetailrs.getString(12) : "N/A");
				treatmentdtls.setClaim_bill_notrtmntdtls(
						treatmentdetailrs.getString(13) != null ? treatmentdetailrs.getString(13) : "N/A");
				treatmentdtls.setHospitalmortalitytrtmntdtls(
						treatmentdetailrs.getString(14) != null ? treatmentdetailrs.getString(14) : "N/A");
				treatmentdtls.setReferralcodetrtmntdtls(
						treatmentdetailrs.getString(15) != null ? treatmentdetailrs.getString(15) : "N/A");
				treatmentdtls.setImplant_datatrtmntdtls(
						treatmentdetailrs.getString(16) != null ? treatmentdetailrs.getString(16) : "N/A");
				treatmentdtls.setNabhtrtmntdtls(
						treatmentdetailrs.getString(17) != null ? treatmentdetailrs.getString(17) : "N/A");
				treatmentdtls.setVERIFICATIONMODEtrtmntdtls(
						treatmentdetailrs.getString(18) != null ? treatmentdetailrs.getString(18) : "N/A");
				treatmentdtls.setISPATIENTOTPVERIFIEDtrtmntdtls(
						treatmentdetailrs.getString(19) != null ? treatmentdetailrs.getString(19) : "N/A");
				treatmentdtls.setCpdmortalitytrtmnts(
						treatmentdetailrs.getString(20) != null ? treatmentdetailrs.getString(20) : "N/A");
				treatmentdetails.add(treatmentdtls);
			}

			while (preauthlogrs.next()) {
				Hospitaldetailsforoldclaimandnewclaim preauthdtlsdtls = new Hospitaldetailsforoldclaimandnewclaim();
				preauthdtlsdtls.setUrnnopreauth(preauthlogrs.getString(1));
				preauthdtlsdtls.setMembernamepreauth(preauthlogrs.getString(2));
				preauthdtlsdtls.setSNAREMARKSpreauth(preauthlogrs.getString(3));
				preauthdtlsdtls.setApprovedamountpreauth(preauthlogrs.getString(4));
				preauthdtlsdtls.setApproveddatepreauth(preauthlogrs.getString(5));
				preauthdtlsdtls.setPdfpathpreauth(preauthlogrs.getString(6));
				;
				preauthdtlsdtls.setHospitalcodepreauth(preauthlogrs.getString(7));
				preauthdtlsdtls.setAuthoritycodepreauth(preauthlogrs.getString(8));
				preauthdtlsdtls.setAmountpreauth(preauthlogrs.getString(9));
				preauthdtlsdtls.setAddtional_doc1preauth(preauthlogrs.getString(10));
				preauthdtlsdtls.setAddtional_doc2preauth(preauthlogrs.getString(11));
				preauthdtlsdtls.setAddtional_doc3preauth(preauthlogrs.getString(12));
				preauthdtlsdtls.setHospitaluploaddatepreauth(preauthlogrs.getString(13));
				preauthdtlsdtls.setMoredocsdescriptionpreauth(preauthlogrs.getString(14));
				preauthlogdata.add(preauthdtlsdtls);
			}
			while (treatmenthistoryrs.next()) {
				Hospitaldetailsforoldclaimandnewclaim treatmenthistorydata = new Hospitaldetailsforoldclaimandnewclaim();
				treatmenthistorydata.setUrntrmnthst(treatmenthistoryrs.getString(1));
				treatmenthistorydata
						.setActualdateofadmissiontrmnthst(DateFormat.formatDateFun(treatmenthistoryrs.getString(2)));
				treatmenthistorydata
						.setActualdateofdischargetrmnthst(DateFormat.formatDateFun(treatmenthistoryrs.getString(3)));
				treatmenthistorydata.setHospitalnametrmnthst(treatmenthistoryrs.getString(4));
				treatmenthistorydata.setPatientnametrmnthst(treatmenthistoryrs.getString(5));
				treatmenthistorydata.setInvoicenotrmnthst(treatmenthistoryrs.getString(6));
				treatmenthistorydata.setCurrenttotalamounttrmnthst(treatmenthistoryrs.getString(7));
				treatmenthistorydata.setAdditional_doc1trmnthst(treatmenthistoryrs.getString(8));
				treatmenthistorydata.setAdditional_doc2trmnthst(treatmenthistoryrs.getString(9));
				treatmenthistorydata.setYeartrmnthst(treatmenthistoryrs.getString(10));
				treatmenthistorydata.setHospitalcodetrmnthst(treatmenthistoryrs.getString(11));
				treatmenthistorydata.setFILENAMEStrmnthst(treatmenthistoryrs.getString(12));
				treatmenthistorydata.setFILENAMES1trmnthst(treatmenthistoryrs.getString(13));
				treatmenthistorydata.setFILENAMES2trmnthst(treatmenthistoryrs.getString(14));
				treatmenthistorydata.setSNAAPPROVEDAMOUNTtrmnthst(treatmenthistoryrs.getString(15));
				treatmenthistorydata.setSNAAPPROVEDDATEtrmnthst(treatmenthistoryrs.getString(16));
				treatmenthistorydata.setSNAREMARKStrmnthst(treatmenthistoryrs.getString(17));
				treatmenthistorydata.setPackagecodetrmnthst(treatmenthistoryrs.getString(18));
				treatmenthistorydata.setPackagenametrmnthst(treatmenthistoryrs.getString(19));
				treatmenthistory.add(treatmenthistorydata);
			}
			while (actiontakenhistoryrs.next()) {
				Hospitaldetailsforoldclaimandnewclaim actiontakenhistorydata = new Hospitaldetailsforoldclaimandnewclaim();
				actiontakenhistorydata.setApprovedamountactiontakenhst(actiontakenhistoryrs.getString(1));
				actiontakenhistorydata.setAction_typeactiontakenhst(actiontakenhistoryrs.getString(2));
				actiontakenhistorydata.setFull_nameactiontakenhst(actiontakenhistoryrs.getString(3));
				actiontakenhistorydata.setRemark_idactiontakenhst(actiontakenhistoryrs.getString(4));
				actiontakenhistorydata.setActiononactiontakenhst(actiontakenhistoryrs.getString(5));
				actiontakenhistorydata.setDischarge_slipactiontakenhst(actiontakenhistoryrs.getString(6));
				actiontakenhistorydata.setAdditional_docactiontakenhst(actiontakenhistoryrs.getString(7));
				actiontakenhistorydata.setAdditionaldoc1actiontakenhst(actiontakenhistoryrs.getString(8));
				actiontakenhistorydata.setPresurgeryphotoactiontakenhst(actiontakenhistoryrs.getString(9));
				actiontakenhistorydata.setPostsurgeryphotoactiontakenhst(actiontakenhistoryrs.getString(10));
				actiontakenhistorydata.setRemarksactiontakenhst(actiontakenhistoryrs.getString(11));
				actiontakenhistorydata.setAdditionaldoc2actiontakenhst(actiontakenhistoryrs.getString(12));
				actiontakenhistorydata.setPatient_photoactiontakenhst(actiontakenhistoryrs.getString(13));
				actiontakenhistorydata.setSpecimen_removal_photoactiontakenhst(actiontakenhistoryrs.getString(14));
				actiontakenhistorydata.setIntra_surgery_photoactiontakenhst(actiontakenhistoryrs.getString(15));
				actiontakenhistorydata.setHospitalcodeactiontakenhst(actiontakenhistoryrs.getString(16));
				actiontakenhistorydata.setDateofadmissionactiontakenhst(actiontakenhistoryrs.getString(17));
				actiontakenhistory.add(actiontakenhistorydata);
			}
			alldata.put("HospitalDetails", hospitaldetails);
			alldata.put("Treatmendetails", treatmentdetails);
			alldata.put("preauthlog", preauthlogdata);
			alldata.put("treatmenthistory", treatmenthistory);
			alldata.put("Actiontakenhistory", actiontakenhistory);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (treatmentdetailrs != null)
					treatmentdetailrs.close();
				if (treatmentdetailrs != null)
					treatmentdetailrs.close();
				if (preauthlogrs != null)
					preauthlogrs.close();
				if (treatmenthistoryrs != null)
					treatmenthistoryrs.close();
				if (actiontakenhistoryrs != null)
					actiontakenhistoryrs.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		return alldata;
	}
}
