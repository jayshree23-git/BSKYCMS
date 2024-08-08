package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.project.bsky.bean.ApproveRemark;
import com.project.bsky.bean.CPDPreauthActionBean;
import com.project.bsky.bean.ICDDetailsBean;
import com.project.bsky.bean.PreAuthBean;
import com.project.bsky.bean.PreAuthDetails;
import com.project.bsky.bean.PreAuthGroupBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.PreAuthService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.JwtUtil;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@SuppressWarnings("deprecation")
@Service
public class PreAuthServiceImpl implements PreAuthService {
	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");

	@Value("${file.path.PreAuthDoc:}")
	private String file;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Environment env;

	@Autowired
	private Logger logger;

	@Autowired
	private JwtUtil util;

	@SuppressWarnings("unused")
	@Override
	public List<Object> getPreAuthorizationData(CPDPreauthActionBean requestBean) {
		List<Object> PreauthList = new ArrayList<Object>();
		ResultSet claimListObj = null;
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid()!= null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_APPROVE_PREAUTH")
					.registerStoredProcedureParameter("p_userId", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_formDate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_toDate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_distCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_TXNPACKAGEDETAILID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMEID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMESUBCATEGORYID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_userId", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_formDate", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_toDate", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_stateCode", requestBean.getStateCode1());
			storedProcedureQuery.setParameter("p_distCode", requestBean.getDistCode1());
			storedProcedureQuery.setParameter("p_hospitalCode", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_flag", requestBean.getFlag());
			storedProcedureQuery.setParameter("p_action", requestBean.getAction());
			storedProcedureQuery.setParameter("p_TXNPACKAGEDETAILID", requestBean.getTxnPackageDetailId());
			storedProcedureQuery.setParameter("P_SCHEMEID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("P_SCHEMESUBCATEGORYID", schemecatId);
			storedProcedureQuery.execute();

			claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			if (requestBean.getAction().equals("C")) {
				while (claimListObj.next()) {
					PreAuthBean res = new PreAuthBean();
					if (claimListObj != null) {
						res.setStatusFlag(false);
					} else {
						res.setStatusFlag(true);
					}
					res.setFRESH(claimListObj.getLong(1));
					res.setQUERYCOMPLIED(claimListObj.getLong(2));
					res.setQUERY(claimListObj.getLong(3));
					res.setAPPROVE(claimListObj.getLong(4));
					res.setREJECT(claimListObj.getLong(5));
					res.setAUTOAPPROVE(claimListObj.getLong(6));
					res.setAUTOREJECT(claimListObj.getLong(7));
					res.setEXPIRED(claimListObj.getLong(8));
					res.setCANCELLED(claimListObj.getLong(9));
					PreauthList.add(res);
				}
			}

			if (requestBean.getAction().equals("A")) {
				while (claimListObj.next()) {
					PreAuthBean resBean = new PreAuthBean();
					if (claimListObj != null) {
						resBean.setStatusFlag(false);
					} else {
						resBean.setStatusFlag(true);
					}
					resBean.setID(claimListObj.getInt(1));
					resBean.setURNNO(claimListObj.getString(2));
					resBean.setMEMBERNAME(claimListObj.getString(3));
					resBean.setHOSPITALCODE(claimListObj.getString(4));
					resBean.setHospitalName(claimListObj.getString(5));
					resBean.setHOSPITALUPLOADDATE(claimListObj.getString(6));
					resBean.setNOOFDAYS(claimListObj.getString(7));
					resBean.setPROCEDURECODE(claimListObj.getString(8));
					resBean.setPACKAGEHEADERNAME(claimListObj.getString(9));
					resBean.setDESCRIPTION(claimListObj.getString(10));
					resBean.setADDTIONAL_DOC1(claimListObj.getString(11));
					resBean.setTOTALPACKAGECOST(claimListObj.getLong(12));
					resBean.setSTATUS(claimListObj.getString(13));
					resBean.setQUERYCOUNT(claimListObj.getInt(14));
					resBean.setTXNPACKAGEDETAILID(claimListObj.getLong(15));
					resBean.setMOREDOCSDESCRIPTION(claimListObj.getString(16));
					resBean.setMOREDOCSDESCRIPTION2(claimListObj.getString(17));
					resBean.setAPPROVEDAMOUNT(claimListObj.getLong(18));
					resBean.setAMOUNT(claimListObj.getLong(19));
					resBean.setAUTHORITYCODE(claimListObj.getString(20));
					resBean.setHOSPITALAUTHCODE(claimListObj.getString(21));
					resBean.setSTATECODE(claimListObj.getString(22));
					resBean.setDISTRICTCODE(claimListObj.getString(23));
					resBean.setPOLICYSTARTDATE(claimListObj.getTimestamp(24));
					resBean.setPOLICYENDDATE(claimListObj.getTimestamp(25));
					resBean.setNEEDMOREDOCS(claimListObj.getString(26));
					resBean.setIMPLANTDATA(claimListObj.getString(27));
					resBean.setREFERRALCODE(claimListObj.getString(28));
					resBean.setADDTIONAL_DOC2(claimListObj.getString(29));
					resBean.setADDTIONAL_DOC3(claimListObj.getString(30));
					resBean.setREPLYSECOND(claimListObj.getString(31));
					resBean.setREPLYTHIRD(claimListObj.getString(32));
					resBean.setNEEDMOREDOCS1DATE(claimListObj.getString(33));
					resBean.setNEEDMOREDOCS2DATE(claimListObj.getString(34));
					resBean.setFAMILYEFUND(claimListObj.getLong(35));
					resBean.setFEMALEFUND(claimListObj.getLong(36));
					resBean.setINSUFFICIENTAMOUNT(claimListObj.getLong(37));
					resBean.setSDATE(claimListObj.getString(38));
					resBean.setPACKAGECOST(claimListObj.getLong(39));
					resBean.setWARDNAME(claimListObj.getString(40));
					resBean.setPROCEDURENAME(claimListObj.getString(41));
					resBean.setSnouserid(claimListObj.getString(42));
					resBean.setGender(claimListObj.getString(43));
					PreauthList.add(resBean);
				}
			}

			if (requestBean.getAction().equals("B")) {
				while (claimListObj.next()) {
					PreAuthBean resBean = new PreAuthBean();
					if (claimListObj != null) {
						resBean.setStatusFlag(false);
					} else {
						resBean.setStatusFlag(true);
					}
					resBean.setID(claimListObj.getInt(1));
					resBean.setURNNO(claimListObj.getString(2));
					resBean.setMEMBERNAME(claimListObj.getString(3));
					resBean.setHOSPITALCODE(claimListObj.getString(4));
					resBean.setHospitalName(claimListObj.getString(5));
					resBean.setHOSPITALUPLOADDATE(claimListObj.getString(6));
					resBean.setNOOFDAYS(claimListObj.getString(7));
					resBean.setPROCEDURECODE(claimListObj.getString(8));
					resBean.setPACKAGEHEADERNAME(claimListObj.getString(9));
					resBean.setDESCRIPTION(claimListObj.getString(10));
					resBean.setADDTIONAL_DOC1(claimListObj.getString(11));
					resBean.setTOTALPACKAGECOST(claimListObj.getLong(12));
					resBean.setSTATUS(claimListObj.getString(13));
					resBean.setQUERYCOUNT(claimListObj.getInt(14));
					resBean.setTXNPACKAGEDETAILID(claimListObj.getLong(15));
					resBean.setMOREDOCSDESCRIPTION(claimListObj.getString(16));
					resBean.setMOREDOCSDESCRIPTION2(claimListObj.getString(17));
					resBean.setAPPROVEDAMOUNT(claimListObj.getLong(18));
					resBean.setAMOUNT(claimListObj.getLong(19));
					resBean.setAUTHORITYCODE(claimListObj.getString(20));
					resBean.setHOSPITALAUTHCODE(claimListObj.getString(21));
					resBean.setSTATECODE(claimListObj.getString(22));
					resBean.setDISTRICTCODE(claimListObj.getString(23));
					resBean.setPOLICYSTARTDATE(claimListObj.getTimestamp(24));
					resBean.setPOLICYENDDATE(claimListObj.getTimestamp(25));
					resBean.setNEEDMOREDOCS(claimListObj.getString(26));
					resBean.setIMPLANTDATA(claimListObj.getString(27));
					resBean.setREFERRALCODE(claimListObj.getString(28));
					resBean.setADDTIONAL_DOC2(claimListObj.getString(29));
					resBean.setADDTIONAL_DOC3(claimListObj.getString(30));
					resBean.setREPLYSECOND(claimListObj.getString(31));
					resBean.setREPLYTHIRD(claimListObj.getString(32));
					resBean.setNEEDMOREDOCS1DATE(claimListObj.getString(33));
					resBean.setNEEDMOREDOCS2DATE(claimListObj.getString(34));
					resBean.setFAMILYEFUND(claimListObj.getLong(35));
					resBean.setFEMALEFUND(claimListObj.getLong(36));
					resBean.setINSUFFICIENTAMOUNT(claimListObj.getLong(37));
					resBean.setSDATE(claimListObj.getString(38));
					resBean.setPACKAGECOST(claimListObj.getLong(39));
					resBean.setWARDNAME(claimListObj.getString(40));
					resBean.setPROCEDURENAME(claimListObj.getString(41));
					resBean.setAPPROVEDDATE(claimListObj.getDate(42));
					resBean.setSNAREMARKS(claimListObj.getString(43));
					resBean.setSNADESCRIPTION(claimListObj.getString(44));
					resBean.setLASTACTIONDATE(claimListObj.getString(45));
					PreauthList.add(resBean);
				}

			}

			if (requestBean.getAction().equals("D")) {
				while (claimListObj.next()) {
					PreAuthBean resBeanHed = new PreAuthBean();
					if (claimListObj != null) {
						resBeanHed.setStatusFlag(false);
					} else {
						resBeanHed.setStatusFlag(true);
					}
					resBeanHed.setHEDNAME(claimListObj.getString(1));
					resBeanHed.setHEDUNIT(claimListObj.getLong(2));
					resBeanHed.setHEDPRICEPERUNIT(claimListObj.getLong(3));
					resBeanHed.setHEDPRICE(claimListObj.getLong(4));
					PreauthList.add(resBeanHed);
				}
			}
			if (requestBean.getAction().equals("E")) {
				while (claimListObj.next()) {
					PreAuthBean resBeanIm = new PreAuthBean();
					if (claimListObj != null) {
						resBeanIm.setStatusFlag(false);
					} else {
						resBeanIm.setStatusFlag(true);
					}
					resBeanIm.setIMPLANTNAME(claimListObj.getString(1));
					resBeanIm.setUNIT(claimListObj.getString(2));
					resBeanIm.setUNITPERPRICE(claimListObj.getLong(3));
					resBeanIm.setAMOUNT(claimListObj.getLong(4));
					PreauthList.add(resBeanIm);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (claimListObj != null) {
					claimListObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return PreauthList;
	}

	@Override
	public Response updatePreAuthorizationData(String userId, Integer action, PreAuthGroupBean preAuthGroupBean) {

		ApproveRemark[] approveRemarkList = new ApproveRemark[preAuthGroupBean.getGroup().size()];
		for (int i = 0; i < preAuthGroupBean.getGroup().size(); i++) {
			ApproveRemark remark = new ApproveRemark();
			remark.setAprv_id(preAuthGroupBean.getGroup().get(i).getID());
			remark.setRemarks(preAuthGroupBean.getGroup().get(i).getSNAREMARKS());
			remark.setTxnDetailsId(preAuthGroupBean.getGroup().get(i).getTXNPACKAGEDETAILID());
			remark.setAprvedAmount(preAuthGroupBean.getGroup().get(i).getAPPROVEDAMOUNT());
			approveRemarkList[i] = remark;
		}

		Response response = new Response();
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, user, pass);
			StructDescriptor structDescriptor = new StructDescriptor("APPROVEREMARK", con);
			STRUCT[] structs = new STRUCT[preAuthGroupBean.getGroup().size()];
			for (int i = 0; i < preAuthGroupBean.getGroup().size(); ++i) {
				PreAuthBean str = (PreAuthBean) preAuthGroupBean.getGroup().get(i);
				Object[] objects = new Object[] { str.getID(), str.getSNAREMARKS(), str.getTXNPACKAGEDETAILID(),
						str.getAPPROVEDAMOUNT() };

				STRUCT struct = new STRUCT(structDescriptor, con, objects);
				structs[i] = struct;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("PREAUTH_ID", con);
			ARRAY array_to_pass = new ARRAY(des, con, structs);
			CallableStatement st = con.prepareCall("call USP_APPROVE_PREAUTH_BY_SNA(?,?,?,?)");
			st.setObject(1, userId);
			st.setArray(2, array_to_pass);
			st.setInt(3, action);
			st.registerOutParameter(4, Types.INTEGER);
			st.execute();
			if (preAuthGroupBean.getAction() == 1) {
				response.setMessage("Approved Successfully");
			} else if (preAuthGroupBean.getAction() == 2) {
				response.setMessage("Rejected Successfully");
			} else if (preAuthGroupBean.getAction() == 3) {
				response.setMessage("Query Raised Successfully");
			} else {
				response.setMessage("You have already query raise for 2 times");
			}
			response.setStatus("Success");
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happened");
			response.setStatus("Failed");
			return response;
		}
	}

	@Override
	public void downLoadPreauthFile(String fileName, String year, String hCode, HttpServletResponse response) {
		String folderName = null;
		try {
			if (fileName.startsWith(bskyResourcesBundel.getString("file.Patient.prefix"))) {
				folderName = bskyResourcesBundel.getString("folder.PatientPic");
			} else if (fileName.startsWith(bskyResourcesBundel.getString("file.SpecialtyClinicalDoc.prefix"))) {
				folderName = bskyResourcesBundel.getString("folder.SpecialtyClinicalDoc.name");
			} else if (fileName.startsWith(bskyResourcesBundel.getString("file.SpecialtyPatientPhoto.prefix"))) {
				folderName = bskyResourcesBundel.getString("folder.SpecialtyPatientPhoto.name");
			} else if (fileName.startsWith(bskyResourcesBundel.getString("file.SpecialtyQueryDoc.prefix"))) {
				folderName = bskyResourcesBundel.getString("folder.SpecialtyQueryDoc.name");
			} else {
				folderName = bskyResourcesBundel.getString("folder.Preauthfile");
			}
			CommonFileUpload.downLoadFileForOverride(fileName, response, folderName, year, hCode);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public String getPreAuthDetails(Long txnId, String urn) throws Exception {
		JSONObject finalResponse = new JSONObject();
		JSONObject blockInfoObject = null;
		JSONObject headInfoObject = null;
		JSONObject implantInfoObject = null;
		JSONObject balanceInfoObject = null;
		JSONObject hospitalRemarkObject = null;
		JSONObject snaRemarkObject = null;
		JSONObject actionhistoryObject = null;
		JSONObject ongoingObject = null;
		JSONObject latestdocObject = null;
		JSONObject allPreauthObject = null;
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONArray blockInfoArray = new JSONArray();
		JSONArray headInfoArray = new JSONArray();
		JSONArray implantInfoArray = new JSONArray();
		JSONArray balanceInfoArray = new JSONArray();
		JSONArray hospitalRemarkArray = new JSONArray();
		JSONArray snaRemarkArray = new JSONArray();
		JSONArray actionhistoryArray = new JSONArray();
		JSONArray ongoingArray = new JSONArray();
		JSONArray latestdocArray = new JSONArray();
		JSONArray allPreauthArray = new JSONArray();
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		ResultSet blockInfo = null;
		ResultSet headInfo = null;
		ResultSet implantInfo = null;
		ResultSet balanceInfo = null;
		ResultSet hospitalRemark = null;
		ResultSet snaRemark = null;
		ResultSet actionhistory = null;
		ResultSet ongoing = null;
		ResultSet latestdoc = null;
		ResultSet allPreauthReq = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_PREAUTH_DETAILS")
					.registerStoredProcedureParameter("p_TXNPACKAGEDETAILID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_blockinfo", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_hedinfo", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_implantinfo", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_balanceinfo", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_hospitalremark", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_snaremark", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_actionhistory", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_ongoing", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_latestdoc", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_allpreauthreq", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_TXNPACKAGEDETAILID", txnId);
			storedProcedureQuery.setParameter("p_URN", urn);
			storedProcedureQuery.execute();
			blockInfo = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_blockinfo");
			headInfo = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_hedinfo");
			implantInfo = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_implantinfo");
			balanceInfo = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_balanceinfo");
			hospitalRemark = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_hospitalremark");
			snaRemark = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_snaremark");
			actionhistory = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_actionhistory");
			ongoing = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_ongoing");
			latestdoc = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_latestdoc");
			allPreauthReq = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_allpreauthreq");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");
			while (blockInfo.next()) {
				blockInfoObject = new JSONObject();
				blockInfoObject.put("id", blockInfo.getLong(1));
				blockInfoObject.put("hospitalName", blockInfo.getString(2));
				blockInfoObject.put("hospitalCode", blockInfo.getString(3));
				blockInfoObject.put("hospitalCatName", blockInfo.getString(4));
				blockInfoObject.put("urn", blockInfo.getString(5));
				blockInfoObject.put("patientName", blockInfo.getString(6));
				blockInfoObject.put("patientContactNumber", blockInfo.getString(7));
				blockInfoObject.put("verifierName", blockInfo.getString(8));
				blockInfoObject.put("verifyMode", blockInfo.getString(9));
				blockInfoObject.put("isOtpVerify", blockInfo.getString(10));
				blockInfoObject.put("procedureCode", blockInfo.getString(11));
				blockInfoObject.put("procedureName", blockInfo.getString(12));
				blockInfoObject.put("packageCode", blockInfo.getString(13));
				blockInfoObject.put("packageName", blockInfo.getString(14));
				blockInfoObject.put("packageCost", blockInfo.getString(15));
				blockInfoObject.put("wardName", blockInfo.getString(16));
				blockInfoObject.put("noOfDays", blockInfo.getLong(17));
				blockInfoObject.put("caseNo", blockInfo.getString(18));
				blockInfoObject.put("requestedDate", blockInfo.getString(19));
				blockInfoObject.put("patientPhoto", blockInfo.getString(20));
				blockInfoObject.put("queryCount", blockInfo.getString(21));
				blockInfoObject.put("packagesubcategorycode", blockInfo.getString(22));
				blockInfoObject.put("memberid", blockInfo.getString(23));
				blockInfoObject.put("gender", blockInfo.getString(24));
				blockInfoArray.put(blockInfoObject);
			}
			while (headInfo.next()) {
				headInfoObject = new JSONObject();
				headInfoObject.put("hedName", headInfo.getString(1));
				headInfoObject.put("hedUnit", headInfo.getString(2));
				headInfoObject.put("hedpricePerUnit", headInfo.getString(3));
				headInfoObject.put("hedPrice", headInfo.getString(4));
				headInfoArray.put(headInfoObject);
			}
			while (implantInfo.next()) {
				implantInfoObject = new JSONObject();
				implantInfoObject.put("implantName", implantInfo.getString(1));
				implantInfoObject.put("unit", implantInfo.getString(2));
				implantInfoObject.put("unitPerPrice", implantInfo.getString(3));
				implantInfoObject.put("amount", implantInfo.getString(4));
				implantInfoArray.put(implantInfoObject);
			}
			while (balanceInfo.next()) {
				balanceInfoObject = new JSONObject();
				balanceInfoObject.put("totalPackageCost", balanceInfo.getString(1));
				balanceInfoObject.put("approvedAmount", balanceInfo.getString(2));
				balanceInfoObject.put("amountBlocked", balanceInfo.getString(3));
				balanceInfoObject.put("familyFund", balanceInfo.getString(4));
				balanceInfoObject.put("femaleFund", balanceInfo.getString(5));
				balanceInfoObject.put("insuffientFund", balanceInfo.getString(6));
				balanceInfoArray.put(balanceInfoObject);
			}
			while (hospitalRemark.next()) {
				hospitalRemarkObject = new JSONObject();
				hospitalRemarkObject.put("hospitalRemark", hospitalRemark.getString(1));
				hospitalRemarkObject.put("document", hospitalRemark.getString(2));
				hospitalRemarkObject.put("sDate", hospitalRemark.getString(3));
				hospitalRemarkArray.put(hospitalRemarkObject);
			}
			while (snaRemark.next()) {
				snaRemarkObject = new JSONObject();
				snaRemarkObject.put("snaRemark", snaRemark.getString(1));
				snaRemarkObject.put("description", snaRemark.getString(2));
				snaRemarkArray.put(snaRemarkObject);
			}
			while (actionhistory.next()) {
				actionhistoryObject = new JSONObject();
				actionhistoryObject.put("actionBy", actionhistory.getString(1));
				actionhistoryObject.put("actionTakenOn", actionhistory.getString(2));
				actionhistoryObject.put("actionType", actionhistory.getString(3));
				actionhistoryObject.put("actionAmount", actionhistory.getString(4));
				actionhistoryObject.put("remark", actionhistory.getString(5));
				actionhistoryObject.put("description", actionhistory.getString(6));
				actionhistoryObject.put("document", actionhistory.getString(7));
				actionhistoryObject.put("sDate", actionhistory.getString(8));
				actionhistoryArray.put(actionhistoryObject);
			}
			while (ongoing.next()) {
				ongoingObject = new JSONObject();
				ongoingObject.put("urn", ongoing.getLong(1));
				ongoingObject.put("caseNo", ongoing.getString(2));
				ongoingObject.put("patientName", ongoing.getString(3));
				ongoingObject.put("packageHeaderCode", ongoing.getString(4));
				ongoingObject.put("packageHeaderName", ongoing.getString(5));
				ongoingObject.put("procedureCode", ongoing.getString(6));
				ongoingObject.put("admissionDate", ongoing.getString(7));
				ongoingObject.put("verifyMode", ongoing.getString(8));
				ongoingObject.put("procedureName", ongoing.getString(9));
				ongoingObject.put("currentStatus", ongoing.getString(10));
				ongoingObject.put("blockedAmount", ongoing.getString(11));
				ongoingObject.put("currentStatusDate", ongoing.getString(12));
				ongoingArray.put(ongoingObject);
			}
			while (latestdoc.next()) {
				latestdocObject = new JSONObject();
				latestdocObject.put("uploadDate", latestdoc.getString(1));
				latestdocObject.put("description", latestdoc.getString(2));
				latestdocObject.put("document", latestdoc.getString(3));
				latestdocObject.put("snaRemarkDate", latestdoc.getString(4));
				latestdocObject.put("snaRemark", latestdoc.getString(5));
				latestdocObject.put("snaDescription", latestdoc.getString(6));
				latestdocObject.put("sDate", latestdoc.getString(7));
				latestdocArray.put(latestdocObject);
			}
			while (allPreauthReq.next()) {
				allPreauthObject = new JSONObject();
				allPreauthObject.put("caseNo", allPreauthReq.getString(1));
				allPreauthObject.put("patientName", allPreauthReq.getString(2));
				allPreauthObject.put("procedureCode", allPreauthReq.getString(3));
				allPreauthObject.put("packageId", allPreauthReq.getString(4));
				allPreauthObject.put("packageName", allPreauthReq.getString(5));
				allPreauthObject.put("requestAmount", allPreauthReq.getString(6));
				allPreauthObject.put("approvedAmount", allPreauthReq.getString(7));
				allPreauthObject.put("requestDate", allPreauthReq.getString(8));
				allPreauthObject.put("status", allPreauthReq.getString(9));
				allPreauthObject.put("snaRemark", allPreauthReq.getString(10));
				allPreauthObject.put("actionDate", allPreauthReq.getString(11));
				allPreauthObject.put("prerequired", allPreauthReq.getString(12));
				allPreauthObject.put("amountBlocked", allPreauthReq.getString(13));
				allPreauthObject.put("admissionType", allPreauthReq.getString(14));
				allPreauthObject.put("verificationMode", allPreauthReq.getString(15));
				allPreauthObject.put("remarkByHos", allPreauthReq.getString(16));
				allPreauthObject.put("doctorName", allPreauthReq.getString(17));
				allPreauthObject.put("doc1", allPreauthReq.getString(18));
				allPreauthObject.put("doc2", allPreauthReq.getString(19));
				allPreauthObject.put("doc3", allPreauthReq.getString(20));
				allPreauthObject.put("snaDescription", allPreauthReq.getString(21));
				allPreauthObject.put("sDate", allPreauthReq.getString(22));
				allPreauthObject.put("blockDate", allPreauthReq.getString(23));
				allPreauthObject.put("unblockDate", allPreauthReq.getString(24));
				allPreauthObject.put("packageDetailsId", allPreauthReq.getString(25));
				allPreauthObject.put("blockinguserdate", allPreauthReq.getString(26));
				allPreauthArray.put(allPreauthObject);
			}
			while (ictDetails.next()) {
				ictDetailsObject = new JSONObject();
				ictDetailsObject.put("icdInfoId", ictDetails.getLong(1));
				ictDetailsObject.put("txnPackageDetailsId", ictDetails.getLong(2));
				ictDetailsObject.put("icdMode", ictDetails.getString(3));
				ictDetailsObject.put("icdCode", ictDetails.getString(4));
				ictDetailsObject.put("icdName", ictDetails.getString(5));
				ictDetailsObject.put("icdModeTxt", ictDetails.getString(6));
				ictDetailsObject.put("byGroupId", ictDetails.getLong(7));
				ictDetailsArray.put(ictDetailsObject);
			}
			while (ictSubDetails.next()) {
				ictSubDetailsObject = new JSONObject();
				ictSubDetailsObject.put("icdDtlsId", ictSubDetails.getLong(1));
				ictSubDetailsObject.put("icdInfoId", ictSubDetails.getLong(2));
				ictSubDetailsObject.put("icdSubCode", ictSubDetails.getString(3));
				ictSubDetailsObject.put("icdSubName", ictSubDetails.getString(4));
				ictSubDetailsArray.put(ictSubDetailsObject);
			}
			finalResponse.put("blockInfoArray", blockInfoArray);
			finalResponse.put("headInfoArray", headInfoArray);
			finalResponse.put("implantInfoArray", implantInfoArray);
			finalResponse.put("balanceInfoArray", balanceInfoArray);
			finalResponse.put("hospitalRemarkArray", hospitalRemarkArray);
			finalResponse.put("snaRemarkArray", snaRemarkArray);
			finalResponse.put("actionHistoryArray", actionhistoryArray);
			finalResponse.put("ongoingArray", ongoingArray);
			finalResponse.put("latestdocArray", latestdocArray);
			finalResponse.put("allPreauthArray", allPreauthArray);
			finalResponse.put("ictDetailsArray", ictDetailsArray);
			finalResponse.put("ictSubDetailsArray", ictSubDetailsArray);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (blockInfo != null)
				blockInfo.close();
			if (headInfo != null)
				headInfo.close();
			if (implantInfo != null)
				implantInfo.close();
			if (balanceInfo != null)
				balanceInfo.close();
			if (hospitalRemark != null)
				hospitalRemark.close();
			if (snaRemark != null)
				snaRemark.close();
			if (actionhistory != null)
				actionhistory.close();
			if (ongoing != null)
				ongoing.close();
			if (latestdoc != null)
				latestdoc.close();
			if (ictDetails != null)
				ictDetails.close();
			if (ictSubDetails != null)
				ictSubDetails.close();

		}
		return finalResponse.toString();

	}

	@Override
	public Response updatetPreAuthorizationDeatails(PreAuthDetails preAuthDetails) throws Exception {
		Response response = new Response();
		Integer claimsnoInteger = null;
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : preAuthDetails.getIcdFinalData()) {
				subListData.add(details.getSubList());
				details.setSubList(null);
				icdData.add(details);
			}
			detailsICD = ow.writeValueAsString(icdData);
			subDetailsICD = ow.writeValueAsString(subListData);
		} catch (Exception e) {
			throw e;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_APPROVE_PREAUTH_BY_SNA_TMS")
					.registerStoredProcedureParameter("P_ACTION", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TXNPACKAGEDETAILID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNAREMARKSID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNAREMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNADESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_APPROVEAMOUNT", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_out", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_ACTION", preAuthDetails.getActionType());
			storedProcedureQuery.setParameter("P_USERID", preAuthDetails.getUserId());
			storedProcedureQuery.setParameter("P_TXNPACKAGEDETAILID", preAuthDetails.getTxnPackageDetailId());
			storedProcedureQuery.setParameter("P_ID", preAuthDetails.getId());
			storedProcedureQuery.setParameter("P_SNAREMARKSID", preAuthDetails.getActionRemarksId());
			storedProcedureQuery.setParameter("P_SNAREMARKS", preAuthDetails.getRemarks());
			storedProcedureQuery.setParameter("P_SNADESCRIPTION", preAuthDetails.getDescription());
			storedProcedureQuery.setParameter("P_APPROVEAMOUNT", preAuthDetails.getAmount());
			storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", preAuthDetails.getIcdFlag());
			storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
			storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);

			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("p_out");
			// //System.out.println(claimsnoInteger);
			if (claimsnoInteger == 1 && preAuthDetails.getActionType() == 1) {
				response.setStatus("Success");
				response.setMessage("Approved Successfully");
			} else if (claimsnoInteger == 1 && preAuthDetails.getActionType() == 2) {
				response.setStatus("Success");
				response.setMessage("Rejected Successfully");
			} else if (claimsnoInteger == 1 && preAuthDetails.getActionType() == 3) {
				response.setStatus("Success");
				response.setMessage("Queried Successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}

		return response;
	}

	@Override
	public String getPreAuthCaseDetails(Long txnId) throws Exception {
		JSONObject finalResponse = new JSONObject();
		JSONObject packageDetailsObject = null;
		JSONObject headInfoObject = null;
		JSONObject implantInfoObject = null;
		JSONObject wardInfoObject = null;
		JSONObject vitalInfoObject = null;
		JSONArray packageDetailsArray = new JSONArray();
		JSONArray headInfoArray = new JSONArray();
		JSONArray implantInfoArray = new JSONArray();
		JSONArray wardInfoArray = new JSONArray();
		JSONArray vitalInfoArray = new JSONArray();
		ResultSet packageDetails = null;
		ResultSet headInfo = null;
		ResultSet implantInfo = null;
		ResultSet wardInfo = null;
		ResultSet vitalInfo = null;

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_PACKAGE_DETAILS")
					.registerStoredProcedureParameter("p_TXNPACKAGEDETAILID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagedetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_hedinfo", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_implantinfo", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_wardinfo", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_vitalinfo", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_TXNPACKAGEDETAILID", txnId);
			storedProcedureQuery.execute();
			packageDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_packagedetails");
			headInfo = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_hedinfo");
			implantInfo = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_implantinfo");
			wardInfo = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_wardinfo");
			vitalInfo = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_vitalinfo");

			while (packageDetails.next()) {
				packageDetailsObject = new JSONObject();
				packageDetailsObject.put("caseNo", packageDetails.getString(1));
				packageDetailsObject.put("patientName", packageDetails.getString(2));
				packageDetailsObject.put("procedureCode", packageDetails.getString(3));
				packageDetailsObject.put("packageName", packageDetails.getString(4));
				packageDetailsObject.put("packageCode", packageDetails.getString(5));
				packageDetailsObject.put("procedureName", packageDetails.getString(6));
				packageDetailsObject.put("preAuthStatus", packageDetails.getString(7));
				packageDetailsObject.put("admissionType", packageDetails.getString(8));
				packageDetailsObject.put("verificationCode", packageDetails.getString(9));
				packageDetailsObject.put("remarkByHos", packageDetails.getString(10));
				packageDetailsObject.put("doctorName", packageDetails.getString(11));
				packageDetailsObject.put("blockDate", packageDetails.getString(12));
				packageDetailsObject.put("unBlockDate", packageDetails.getString(13));
				packageDetailsObject.put("amountBlocked", packageDetails.getString(14));
				packageDetailsObject.put("insufficientAmount", packageDetails.getString(15));
				packageDetailsObject.put("totalPackageCost", packageDetails.getString(16));
				packageDetailsObject.put("snaRemark", packageDetails.getString(17));
				packageDetailsObject.put("snaDescription", packageDetails.getString(18));
				packageDetailsObject.put("doc1", packageDetails.getString(19));
				packageDetailsObject.put("doc2", packageDetails.getString(20));
				packageDetailsObject.put("doc3", packageDetails.getString(21));
				packageDetailsObject.put("wardName", packageDetails.getString(22));
				packageDetailsObject.put("sDate", packageDetails.getString(23));
				packageDetailsObject.put("status", packageDetails.getString(24));
				packageDetailsObject.put("patientPhoto", packageDetails.getString(25));
				packageDetailsObject.put("moreDocsDesc", packageDetails.getString(26));
				packageDetailsObject.put("moreDocsDesc2", packageDetails.getString(27));
				packageDetailsObject.put("noOfDays", packageDetails.getString(28));
				packageDetailsObject.put("overrideCode", packageDetails.getString(29));
				packageDetailsObject.put("verifyMemberName", packageDetails.getString(30));
				packageDetailsObject.put("referalCode", packageDetails.getString(31));
				packageDetailsObject.put("expireDate", packageDetails.getString(32));
				packageDetailsObject.put("unblockingDesc", packageDetails.getString(33));
				packageDetailsObject.put("unblockInvoice", packageDetails.getString(34));
				packageDetailsObject.put("unblockOverrideCode", packageDetails.getString(35));
				packageDetailsObject.put("unblockverificationMode", packageDetails.getString(36));
				packageDetailsObject.put("unblockVerifyMemebrName", packageDetails.getString(37));
				packageDetailsObject.put("requestAmount", packageDetails.getString(38));
				packageDetailsObject.put("approvedAmount", packageDetails.getString(39));
				packageDetailsObject.put("approvedDate", packageDetails.getString(40));
				packageDetailsObject.put("query1Date", packageDetails.getString(41));
				packageDetailsObject.put("query2Date", packageDetails.getString(42));
				packageDetailsObject.put("queryReplayByHos1", packageDetails.getString(43));
				packageDetailsObject.put("queryReplayByHos2", packageDetails.getString(44));
				packageDetailsObject.put("queryReplayByHosDate1", packageDetails.getString(45));
				packageDetailsObject.put("queryReplayByHosDate2", packageDetails.getString(46));
				packageDetailsObject.put("hospitalCode", packageDetails.getString(47));
				packageDetailsObject.put("requestDesc", packageDetails.getString(48));
				packageDetailsObject.put("requestDate", packageDetails.getString(49));
				packageDetailsArray.put(packageDetailsObject);
			}
			while (headInfo.next()) {
				headInfoObject = new JSONObject();
				headInfoObject.put("hedName", headInfo.getString(1));
				headInfoObject.put("hedUnit", headInfo.getString(2));
				headInfoObject.put("hedpricePerUnit", headInfo.getString(3));
				headInfoObject.put("hedPrice", headInfo.getString(4));
				headInfoObject.put("preAuthStatus", headInfo.getString(5));
				headInfoArray.put(headInfoObject);
			}
			while (implantInfo.next()) {
				implantInfoObject = new JSONObject();
				implantInfoObject.put("implantName", implantInfo.getString(1));
				implantInfoObject.put("unit", implantInfo.getString(2));
				implantInfoObject.put("unitPerPrice", implantInfo.getString(3));
				implantInfoObject.put("amount", implantInfo.getString(4));
				implantInfoArray.put(implantInfoObject);
			}
			while (wardInfo.next()) {
				wardInfoObject = new JSONObject();
				wardInfoObject.put("wardName", wardInfo.getString(1));
				wardInfoObject.put("wardAmount", wardInfo.getString(2));
				wardInfoObject.put("wardBlockDate", wardInfo.getString(3));
				wardInfoObject.put("preAuthStatus", wardInfo.getString(4));
				wardInfoArray.put(wardInfoObject);
			}
			while (vitalInfo.next()) {
				vitalInfoObject = new JSONObject();
				vitalInfoObject.put("vitalSign", vitalInfo.getString(1));
				vitalInfoObject.put("vitalValue", vitalInfo.getString(2));
				vitalInfoArray.put(vitalInfoObject);
			}

			finalResponse.put("packageDetailsArray", packageDetailsArray);
			finalResponse.put("hedInfoArray", headInfoArray);
			finalResponse.put("implantInfoArray", implantInfoArray);
			finalResponse.put("wardInfoArray", wardInfoArray);
			finalResponse.put("vitalInfoArray", vitalInfoArray);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (packageDetails != null)
				packageDetails.close();
			if (headInfo != null)
				headInfo.close();
			if (implantInfo != null)
				implantInfo.close();
			if (wardInfo != null)
				wardInfo.close();
			if (vitalInfo != null)
				vitalInfo.close();
		}
		return finalResponse.toString();

	}

	@Override
	public Response updateSpecialtyRequest(PreAuthDetails preAuthDetails) throws Exception {
		Response response = new Response();
		Integer claimsnoInteger = null;
		String actionCode = null;
		if (preAuthDetails.getActionType() == 1) {
			actionCode = "A";
		} else if (preAuthDetails.getActionType() == 2) {
			actionCode = "B";
		} else if (preAuthDetails.getActionType() == 3) {
			actionCode = "C";
		}

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOS_SPECIALITY_REQ_ACTIONBYSNA")
					.registerStoredProcedureParameter("P_ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REQUESTID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARK", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_ACTIONCODE", actionCode);
			storedProcedureQuery.setParameter("P_REQUESTID", preAuthDetails.getRequestId());
			storedProcedureQuery.setParameter("P_REMARK", preAuthDetails.getActionRemarksId());
			storedProcedureQuery.setParameter("P_DESCRIPTION", preAuthDetails.getDescription());
			storedProcedureQuery.setParameter("P_USERID", util.getCurrentUser());

			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			// //System.out.println(claimsnoInteger);
			if (claimsnoInteger == 1 && preAuthDetails.getActionType() == 1) {
				response.setStatus("Success");
				response.setMessage("Approved Successfully");
			} else if (claimsnoInteger == 1 && preAuthDetails.getActionType() == 2) {
				response.setStatus("Success");
				response.setMessage("Rejected Successfully");
			} else if (claimsnoInteger == 1 && preAuthDetails.getActionType() == 3) {
				response.setStatus("Success");
				response.setMessage("Queried Successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}

		return response;
	}
}
