package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.project.bsky.bean.FPOverrideListBean;
import com.project.bsky.bean.OverrideRemark;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.VerifyOverrideCodeBean;
import com.project.bsky.bean.overrideCodeBean;
import com.project.bsky.service.FPOverrideCodeService;
import com.project.bsky.util.AadhaarVaultUtils;
import com.project.bsky.util.CommonFileUpload;
import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@SuppressWarnings({ "deprecation", "unused" })
@Service
public class FPOverrideCodeServiceImpl implements FPOverrideCodeService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Environment env;

	private static ResourceBundle bskyResourcesBundel3 = ResourceBundle.getBundle("fileConfiguration");
	@Value("${file.path.OverRideCode:}")
	private String file;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> getOverrideCode(String userId, String formDate, String toDate, String action, String aprvStatus,
			String hospitalcode) {
		List<Object> count = new ArrayList<Object>();
		ResultSet pfOverrideDetailsObj = null;
		String status = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FP_OVERRIDE_CODE_LIST")
					.registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userId);
			storedProcedureQuery.setParameter("P_FROM_DATE", formDate);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate);
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.setParameter("P_FLAG", aprvStatus);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalcode);
			storedProcedureQuery.execute();
			pfOverrideDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (pfOverrideDetailsObj.next()) {
				overrideCodeBean rcBean = new overrideCodeBean();
				if (pfOverrideDetailsObj != null) {
					rcBean.setStatus(false);
				} else {
					rcBean.setStatus(true);
				}
				rcBean.setPatientName(pfOverrideDetailsObj.getString(1));
				rcBean.setUrn(pfOverrideDetailsObj.getString(2));
				rcBean.setHospitalCode(pfOverrideDetailsObj.getString(4));
				rcBean.setDescription(pfOverrideDetailsObj.getString(3));
				rcBean.setRequestedDate(pfOverrideDetailsObj.getString(7));
				rcBean.setFpOverrideCode(pfOverrideDetailsObj.getString(6));
				rcBean.setHospitalName(pfOverrideDetailsObj.getString(5));
				rcBean.setId(pfOverrideDetailsObj.getInt(8));
				rcBean.setGeneratedThrough(pfOverrideDetailsObj.getString(9));
				rcBean.setMemberId(pfOverrideDetailsObj.getString(10));
				rcBean.setNoOfDays(pfOverrideDetailsObj.getInt(11));
				rcBean.setPdfName(pfOverrideDetailsObj.getString(12));
				rcBean.setDescription(pfOverrideDetailsObj.getString(13));
				rcBean.setFULLNAMEENGLISH(pfOverrideDetailsObj.getString(14));
				rcBean.setAADHARNUMBER(pfOverrideDetailsObj.getString(15));
				status = pfOverrideDetailsObj.getString(16);
				rcBean.setCreatedOn(pfOverrideDetailsObj.getString(17));
				rcBean.setRemarks(pfOverrideDetailsObj.getString(18));
				rcBean.setApproveStatus(pfOverrideDetailsObj.getString(19));
				rcBean.setApproveDate(pfOverrideDetailsObj.getString(20));
				count.add(rcBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (pfOverrideDetailsObj != null) {
					pfOverrideDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return count;
	}

	@Override
	public Response approveOverrideCode(String userId, Integer action, FPOverrideListBean bean) {
		OverrideRemark[] overrideRemarkList = new OverrideRemark[bean.getOverrideCode().size()];
		for (int i = 0; i < bean.getOverrideCode().size(); i++) {
			OverrideRemark overrideRemark = new OverrideRemark();
			overrideRemark.setAprv_id(bean.getOverrideCode().get(i).getId());
			if (bean.getOverrideCode().get(i).getRemarks() != null) {
				overrideRemark.setRemarks(bean.getOverrideCode().get(i).getRemarks());
			} else {
				overrideRemark.setRemarks("NA");
			}
			overrideRemarkList[i] = overrideRemark;
		}
		Response response = new Response();
		int count = 0;
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, user, pass);
			StructDescriptor structDescriptor = new StructDescriptor("TYP_APPROVEREMARK", con);
			STRUCT[] structs = new STRUCT[bean.getOverrideCode().size()];
			for (int i = 0; i < bean.getOverrideCode().size(); ++i) {
				overrideCodeBean str = (overrideCodeBean) bean.getOverrideCode().get(i);
				if (str.getRemarks() == null) {
					str.setRemarks("NA");
				}
				Object[] objects = new Object[] { str.getId(), str.getRemarks() };
				STRUCT struct = new STRUCT(structDescriptor, con, objects);
				structs[i] = struct;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYP_OVERRIDE_ID", con);
			ARRAY array_to_pass = new ARRAY(des, con, structs);
			OracleCallableStatement st = (OracleCallableStatement) con
					.prepareCall("call USP_APPROVE_OVERRIDE_CODE(?,?,?,?)");
			st.setObject(1, userId);
			st.setArray(2, array_to_pass);
			st.setInt(3, action);
			st.registerOutParameter(4, Types.INTEGER);
			st.execute();
			count = ((OracleCallableStatement) st).getInt(4);
			if (bean.getAction() == 1) {
				response.setMessage(" Approved Successfully");
			} else if (bean.getAction() == 2) {
				response.setMessage(" Rejected Successfully");
			} else {
				response.setMessage("Nothing to Happen");
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
	public List<Object> getPatientDetails(String urn, Integer memberId, Date requestedDate, String hospitalCode,
			String generatedThrough) {
		ResultSet patientObj = null;
		List<Object> patientDtls = new ArrayList<>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_PATIENT_DETAILS")
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MEMBERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GENERATEDTHROUGH", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULTSET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_URN", urn);
			storedProcedureQuery.setParameter("P_MEMBERID", memberId);
			storedProcedureQuery.setParameter("P_DATE", requestedDate);
			storedProcedureQuery.setParameter("P_GENERATEDTHROUGH", generatedThrough);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.execute();
			patientObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULTSET");
			while (patientObj.next()) {
				VerifyOverrideCodeBean bean = new VerifyOverrideCodeBean();
				bean.setUrn(patientObj.getString(1));
				bean.setDate(patientObj.getString(2));
				bean.setPatientName(patientObj.getString(3));
				bean.setMemberId(patientObj.getInt(4));
				String aadhar=AadhaarVaultUtils.callAadhaarService(patientObj.getString(5), "1", AadhaarVaultUtils.ServiceType.GET_AADHAAR_FROM_REFERENCE);
				bean.setAdharNo(AadhaarVaultUtils.maskAadharNumber(aadhar));
				bean.setVerifiedBy(patientObj.getString(6));
				bean.setVerifiedThrough(patientObj.getString(7));
				bean.setStatus(patientObj.getString(8));
				bean.setModeOfVerify(patientObj.getString(9));
				patientDtls.add(bean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (patientObj != null)
					patientObj.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return patientDtls;
	}

	@Override
	public void downLoadOverrideFile(String fileName, String year, String hCode, HttpServletResponse response) {
		String folderName = null;
		try {
			folderName = bskyResourcesBundel3.getString("folder.Overridefile");
			CommonFileUpload.downLoadFileForOverride(fileName, response, folderName, year, hCode);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
}
