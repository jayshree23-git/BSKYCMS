package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.CceOutBoundBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.Cce;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.CceRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.CCEOutBoundService;
import com.project.bsky.util.CommonFileUpload;

@SuppressWarnings("unused")
@Service
public class CCEOutBoundServiceImpl implements CCEOutBoundService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CceRepository cceRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private Logger logger;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public Map<Long, List<Object>> getCceOutBoundData(String userId, String formDate, String toDate, String action,
			String hospitalCode, Long cceId, Integer pageIn, Integer pageEnd, String queryStatus, String stateCode,
			String distCode) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> count = new ArrayList<Object>();
		Long size = null;
		ResultSet cceOutBoundDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CCE_OUTBOUNDCALL_LIST")
					.registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_IN", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_END", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userId);
			storedProcedureQuery.setParameter("P_FROM_DATE", formDate);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate);
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_Id", cceId);
			storedProcedureQuery.setParameter("P_PAGE_IN", pageIn);
			storedProcedureQuery.setParameter("P_PAGE_END", pageEnd);
			storedProcedureQuery.setParameter("P_FLAG", queryStatus);
			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.execute();
			if (action.equals("C")) {
				cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
				while (cceOutBoundDetailsObj.next()) {
					CceOutBoundBean rcBean = new CceOutBoundBean();
					rcBean.setCompleted(cceOutBoundDetailsObj.getInt(1));
					rcBean.setPending(cceOutBoundDetailsObj.getInt(2));
					rcBean.setTotalData(cceOutBoundDetailsObj.getInt(3));
					rcBean.setQuerySentDGO(cceOutBoundDetailsObj.getInt(4));
					rcBean.setFresh(cceOutBoundDetailsObj.getInt(5));
					count.add(rcBean);
				}
				map.put(size, count);
			} else if (action.equals("D")) {
				size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
				cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
				while (cceOutBoundDetailsObj.next()) {

					CceOutBoundBean rcBean = new CceOutBoundBean();
					if (cceOutBoundDetailsObj != null) {
						rcBean.setStatus(false);
					} else {
						rcBean.setStatus(true);
					}
					rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
					rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
					rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
					rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
					rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
					rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
					rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
					rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
					rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
					rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
					rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
					rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
					rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
					rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
					rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
					rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
					rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
					rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
					rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
					rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
					rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
					rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
					rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
					rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
					rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
					rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
					rcBean.setDoc1(cceOutBoundDetailsObj.getString(27));
					rcBean.setDoc2(cceOutBoundDetailsObj.getString(28));
					rcBean.setDoc3(cceOutBoundDetailsObj.getString(29));
					rcBean.setDcRemarks(cceOutBoundDetailsObj.getString(30));
					rcBean.setDgoQueryRemarks(cceOutBoundDetailsObj.getString(31));
					rcBean.setDgoQueryDoc(cceOutBoundDetailsObj.getString(32));
					rcBean.setState(cceOutBoundDetailsObj.getString(33));
					count.add(rcBean);
				}
				map.put(size, count);
			} else if (action.equals("A")) {

				size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
				cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
				while (cceOutBoundDetailsObj.next()) {

					CceOutBoundBean rcBean = new CceOutBoundBean();
					if (cceOutBoundDetailsObj != null) {
						rcBean.setStatus(false);
					} else {
						rcBean.setStatus(true);
					}
					rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
					rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
					rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
					rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
					rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
					rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
					rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
					rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
					rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
					rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
					rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
					rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
					rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
					rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
					rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
					rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
					rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
					rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
					rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
					rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
					rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
					rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
					rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
					rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
					rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
					rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
					rcBean.setCreatedOn(cceOutBoundDetailsObj.getString(27));
					rcBean.setDgoQueryRemarks(cceOutBoundDetailsObj.getString(28));
					rcBean.setDgoQueryDoc(cceOutBoundDetailsObj.getString(29));
					rcBean.setState(cceOutBoundDetailsObj.getString(30));
					rcBean.setITAFlag(cceOutBoundDetailsObj.getInt(31));
					rcBean.setDgoITARemark(cceOutBoundDetailsObj.getString(32));
					rcBean.setDgoDoc(cceOutBoundDetailsObj.getString(33));
					rcBean.setExpiryStatus(cceOutBoundDetailsObj.getInt(34));
					count.add(rcBean);
				}
				map.put(size, count);
			} else {
				size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
				cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
				while (cceOutBoundDetailsObj.next()) {

					CceOutBoundBean rcBean = new CceOutBoundBean();
					if (cceOutBoundDetailsObj != null) {
						rcBean.setStatus(false);
					} else {
						rcBean.setStatus(true);
					}
					rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
					rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
					rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
					rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
					rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
					rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
					rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
					rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
					rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
					rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
					rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
					rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
					rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
					rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
					rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
					rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
					rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
					rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
					rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
					rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
					rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
					rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
					rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
					rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
					rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
					rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
					rcBean.setCreatedOn(cceOutBoundDetailsObj.getString(27));
					rcBean.setDgoQueryRemarks(cceOutBoundDetailsObj.getString(28));
					rcBean.setDgoQueryDoc(cceOutBoundDetailsObj.getString(29));
					rcBean.setState(cceOutBoundDetailsObj.getString(30));
					rcBean.setITAFlag(cceOutBoundDetailsObj.getInt(31));
					count.add(rcBean);
				}
				map.put(size, count);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (cceOutBoundDetailsObj != null) {
					cceOutBoundDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return map;
	}

	@Override
	public Integer updateCCeOutBound(String userId, Long cceId, String urn, String hospitalCode, MultipartFile cceDoc1,
			MultipartFile cceDoc2, MultipartFile cceDoc3, String dateOfAdmission, String remarks, String action,
			String alternateNo) {
		Integer msgOut = null;
		Map<String, String> savedDCOutBoundFile;
		try {
			savedDCOutBoundFile = CommonFileUpload.saveCCEOUtBoundFile(dateOfAdmission.substring(0, 4).trim(),
					urn.trim(), hospitalCode.trim(), cceDoc1, cceDoc2, cceDoc3);
			if (savedDCOutBoundFile != null) {
				StoredProcedureQuery query = entityManager.createStoredProcedureQuery("USP_DC_CCEOUTBOUND_UPDATE");
				query.registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_ID", Long.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_CCEDOC1", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_CCEDOC2", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_CCEDOC3", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_ALTERNATEPHONENO", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);
				query.setParameter("P_USER_ID", userId);
				query.setParameter("P_ID", cceId);
				query.setParameter("P_URN", urn);
				query.setParameter("P_HOSPITAL_CODE", hospitalCode);
				query.setParameter("P_CCEDOC1", savedDCOutBoundFile.get("cceDoc1"));
				query.setParameter("P_CCEDOC2", savedDCOutBoundFile.get("cceDoc2"));
				query.setParameter("P_CCEDOC3", savedDCOutBoundFile.get("cceDoc3"));
				query.setParameter("P_REMARKS", remarks);
				query.setParameter("P_ACTION", action);
				query.setParameter("P_ALTERNATEPHONENO", alternateNo);
				query.execute();
				msgOut = (Integer) query.getOutputParameterValue("P_MSGOUT");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return msgOut;
	}
	
	@Override
	public Map<Long, List<Object>> getDgoCallCenterData(String userId, String formDate, String toDate, String action,
			String hospitalCode, Long cceId,Integer pageIn, Integer pageEnd,String queryStatus,String stateCode,String distCode) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> count = new ArrayList<Object>();
		Long size = null;
		ResultSet cceOutBoundDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DGO_CALLCENTER_LIST")
					.registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_IN", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_END", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userId);
			storedProcedureQuery.setParameter("P_FROM_DATE", formDate);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate);
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_Id", cceId);
			storedProcedureQuery.setParameter("P_PAGE_IN", pageIn);
			storedProcedureQuery.setParameter("P_PAGE_END", pageEnd);
			storedProcedureQuery.setParameter("P_FLAG", queryStatus);
			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.execute();
			if (action.equals("D")) {
				size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
				cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
				while (cceOutBoundDetailsObj.next()) {

					CceOutBoundBean rcBean = new CceOutBoundBean();
					if (cceOutBoundDetailsObj != null) {
						rcBean.setStatus(false);
					} else {
						rcBean.setStatus(true);
					}
					rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
					rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
					rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
					rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
					rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
					rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
					rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
					rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
					rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
					rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
					rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
					rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
					rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
					rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
					rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
					rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
					rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
					rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
					rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
					rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
					rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
					rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
					rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
					rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
					rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
					rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
					rcBean.setDgoDoc(cceOutBoundDetailsObj.getString(27));
					rcBean.setDgoRemarks(cceOutBoundDetailsObj.getString(28));
					rcBean.setDcRemarks(cceOutBoundDetailsObj.getString(29));
					rcBean.setDgoQueryRemarks(cceOutBoundDetailsObj.getString(30));
					rcBean.setDgoQueryDoc(cceOutBoundDetailsObj.getString(31));
					rcBean.setGoQueryRemarks(cceOutBoundDetailsObj.getString(32));
					rcBean.setDoc1(cceOutBoundDetailsObj.getString(33));
					rcBean.setDoc2(cceOutBoundDetailsObj.getString(34));
					rcBean.setDoc3(cceOutBoundDetailsObj.getString(35));
					rcBean.setState(cceOutBoundDetailsObj.getString(36));
					rcBean.setDgoITAFlag(cceOutBoundDetailsObj.getInt(37));
					rcBean.setDgoITARemark(cceOutBoundDetailsObj.getString(38));
					rcBean.setDgoQueryCount(cceOutBoundDetailsObj.getInt(39));
					count.add(rcBean);
				}
				map.put(size, count);
			}else if(action.equals("A")){

				size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
				cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
				while (cceOutBoundDetailsObj.next()) {

					CceOutBoundBean rcBean = new CceOutBoundBean();
					if (cceOutBoundDetailsObj != null) {
						rcBean.setStatus(false);
					} else {
						rcBean.setStatus(true);
					}
					rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
					rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
					rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
					rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
					rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
					rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
					rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
					rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
					rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
					rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
					rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
					rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
					rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
					rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
					rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
					rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
					rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
					rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
					rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
					rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
					rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
					rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
					rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
					rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
					rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
					rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
					rcBean.setDcRemarks(cceOutBoundDetailsObj.getString(27));
					rcBean.setDcSubmittedDate(cceOutBoundDetailsObj.getString(28));
					rcBean.setDoc1(cceOutBoundDetailsObj.getString(29));
					rcBean.setDoc2(cceOutBoundDetailsObj.getString(30));
					rcBean.setDoc3(cceOutBoundDetailsObj.getString(31));
					rcBean.setDCUserId(cceOutBoundDetailsObj.getString(32));
					rcBean.setDgoQueryCount(cceOutBoundDetailsObj.getInt(33));
					rcBean.setGoQueryRemarks(cceOutBoundDetailsObj.getString(34));
					rcBean.setState(cceOutBoundDetailsObj.getString(35));
					rcBean.setITAFlag(cceOutBoundDetailsObj.getInt(36));
					rcBean.setDgoQueryRemarks(cceOutBoundDetailsObj.getString(37));
					rcBean.setDgoQueryDoc(cceOutBoundDetailsObj.getString(38));
					rcBean.setDgoQueryDate(cceOutBoundDetailsObj.getString(39));
					rcBean.setGoQueryDate(cceOutBoundDetailsObj.getString(40));
					rcBean.setDgoRemarks(cceOutBoundDetailsObj.getString(41));
					rcBean.setDgoDoc(cceOutBoundDetailsObj.getString(42));
					rcBean.setDgoITAFlag(cceOutBoundDetailsObj.getInt(43));
					rcBean.setDgoITARemark(cceOutBoundDetailsObj.getString(44));
					rcBean.setExpiryStatus(cceOutBoundDetailsObj.getInt(45));					
					count.add(rcBean);
				}
				map.put(size, count);
			}
			else {

				size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
				cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
				while (cceOutBoundDetailsObj.next()) {

					CceOutBoundBean rcBean = new CceOutBoundBean();
					if (cceOutBoundDetailsObj != null) {
						rcBean.setStatus(false);
					} else {
						rcBean.setStatus(true);
					}
					rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
					rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
					rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
					rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
					rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
					rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
					rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
					rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
					rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
					rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
					rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
					rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
					rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
					rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
					rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
					rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
					rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
					rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
					rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
					rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
					rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
					rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
					rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
					rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
					rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
					rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
					rcBean.setDcRemarks(cceOutBoundDetailsObj.getString(27));
					rcBean.setDcSubmittedDate(cceOutBoundDetailsObj.getString(28));
					rcBean.setDoc1(cceOutBoundDetailsObj.getString(29));
					rcBean.setDoc2(cceOutBoundDetailsObj.getString(30));
					rcBean.setDoc3(cceOutBoundDetailsObj.getString(31));
					rcBean.setDCUserId(cceOutBoundDetailsObj.getString(32));
					rcBean.setDgoQueryCount(cceOutBoundDetailsObj.getInt(33));
					rcBean.setGoQueryRemarks(cceOutBoundDetailsObj.getString(34));
					rcBean.setState(cceOutBoundDetailsObj.getString(35));
					rcBean.setITAFlag(cceOutBoundDetailsObj.getInt(36));
					count.add(rcBean);
				}
				map.put(size, count);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (cceOutBoundDetailsObj != null) {
					cceOutBoundDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return map;
	}


	@Override
	public Integer updateDgoCallCenterData(String userId, Long cceId, String urn, String hospitalCode,
			MultipartFile dgoDoc, String dateOfAdmission, String remarks, Integer action) {
		Integer msgOut = null;
		Map<String, String> savedDCOutBoundFile;
		try {
			savedDCOutBoundFile = CommonFileUpload.saveDGODoc(dateOfAdmission.substring(0, 4).trim(), urn.trim(),
					hospitalCode.trim(), dgoDoc);
			if (savedDCOutBoundFile != null) {
				StoredProcedureQuery query = entityManager.createStoredProcedureQuery("USP_DGO_CALLCENTER_UPDATE");
				query.registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_ID", Long.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_DGODOC", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_ACTION", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);
				query.setParameter("P_USER_ID", userId);
				query.setParameter("P_ID", cceId);
				query.setParameter("P_URN", urn);
				query.setParameter("P_HOSPITAL_CODE", hospitalCode);
				query.setParameter("P_DGODOC", savedDCOutBoundFile.get("dgoDoc"));
				query.setParameter("P_REMARKS", remarks);
				query.setParameter("P_ACTION", action);
				query.execute();
				msgOut = (Integer) query.getOutputParameterValue("P_MSGOUT");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return msgOut;
	}

	@Override
	public Map<Long, List<Object>> getSupervisorCallCenterData(String userId, String formDate, String toDate,
			String action, String hospitalCode, Long cceId, Integer cceUserId, Integer pageIn, Integer pageEnd,
			String stateCode, String distCode) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> count = new ArrayList<Object>();
		Long size = null;
		ResultSet cceOutBoundDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SUPERVISOR_CALL_LIST")
					.registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CCEUSERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_IN", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_END", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userId);
			storedProcedureQuery.setParameter("P_FROM_DATE", formDate);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate);
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_ID", cceId);
			storedProcedureQuery.setParameter("P_CCEUSERID", cceUserId);
			storedProcedureQuery.setParameter("P_PAGE_IN", pageIn);
			storedProcedureQuery.setParameter("P_PAGE_END", pageEnd);
			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.execute();
			if (action.equals("C")) {
				cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
				while (cceOutBoundDetailsObj.next()) {
					CceOutBoundBean rcBean = new CceOutBoundBean();
					rcBean.setCompleted(cceOutBoundDetailsObj.getInt(1));
					rcBean.setYes(cceOutBoundDetailsObj.getInt(2));
					rcBean.setNo(cceOutBoundDetailsObj.getInt(3));
					rcBean.setPositive(cceOutBoundDetailsObj.getInt(4));
					rcBean.setNegetive(cceOutBoundDetailsObj.getInt(5));
					rcBean.setSupport(cceOutBoundDetailsObj.getInt(6));
					rcBean.setBehaviour(cceOutBoundDetailsObj.getInt(7));
					rcBean.setBribe(cceOutBoundDetailsObj.getInt(8));
					rcBean.setFreshCase(cceOutBoundDetailsObj.getInt(9));
					rcBean.setNotConnected(cceOutBoundDetailsObj.getInt(10));
					rcBean.setPending(cceOutBoundDetailsObj.getInt(11));
					rcBean.setSupportPercent(cceOutBoundDetailsObj.getInt(12));
					rcBean.setBehaviourPercent(cceOutBoundDetailsObj.getInt(13));
					rcBean.setBribePercent(cceOutBoundDetailsObj.getInt(14));
					rcBean.setFreshPercent(cceOutBoundDetailsObj.getInt(15));
					rcBean.setNotConnectedPercent(cceOutBoundDetailsObj.getInt(16));
					rcBean.setFacilitate(cceOutBoundDetailsObj.getInt(17));
					count.add(rcBean);
				}
				map.put(size, count);
			} else if (action.equals("D")) {
				size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
				cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
				while (cceOutBoundDetailsObj.next()) {

					CceOutBoundBean rcBean = new CceOutBoundBean();
					if (cceOutBoundDetailsObj != null) {
						rcBean.setStatus(false);
					} else {
						rcBean.setStatus(true);
					}
					rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
					rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
					rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
					rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
					rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
					rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
					rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
					rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
					rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
					rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
					rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
					rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
					rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
					rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
					rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
					rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
					rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
					rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
					rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
					rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
					rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
					rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
					rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
					rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
					rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
					rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
					rcBean.setFeedbackLoginId(cceOutBoundDetailsObj.getString(27));
					rcBean.setCreatedOn(cceOutBoundDetailsObj.getString(28));
					rcBean.setDialedCount(cceOutBoundDetailsObj.getInt(29));
					rcBean.setReAssignRemark(cceOutBoundDetailsObj.getString(30));
					count.add(rcBean);
				}
				map.put(size, count);
			} else if (action.equals("E")) {
				size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
				cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
				while (cceOutBoundDetailsObj.next()) {

					CceOutBoundBean rcBean = new CceOutBoundBean();
					if (cceOutBoundDetailsObj != null) {
						rcBean.setStatus(false);
					} else {
						rcBean.setStatus(true);
					}
					rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
					rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
					rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
					rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
					rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
					rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
					rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
					rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
					rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
					rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
					rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
					rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
					rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
					rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
					rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
					rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
					rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
					rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
					rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
					rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
					rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
					rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
					rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
					rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
					rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
					rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
					rcBean.setFeedbackLoginId(cceOutBoundDetailsObj.getString(27));
					rcBean.setCreatedOn(cceOutBoundDetailsObj.getString(28));
					rcBean.setDialedCount(cceOutBoundDetailsObj.getInt(29));
					rcBean.setReAssignFlag(cceOutBoundDetailsObj.getInt(30));
					rcBean.setDcSubmittedDate(cceOutBoundDetailsObj.getString(32));
					count.add(rcBean);
				}
				map.put(size, count);
			} else {
				size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
				cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
				while (cceOutBoundDetailsObj.next()) {

					CceOutBoundBean rcBean = new CceOutBoundBean();
					if (cceOutBoundDetailsObj != null) {
						rcBean.setStatus(false);
					} else {
						rcBean.setStatus(true);
					}
					rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
					rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
					rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
					rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
					rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
					rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
					rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
					rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
					rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
					rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
					rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
					rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
					rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
					rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
					rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
					rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
					rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
					rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
					rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
					rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
					rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
					rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
					rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
					rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
					rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
					rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
					rcBean.setFeedbackLoginId(cceOutBoundDetailsObj.getString(27));
					rcBean.setCreatedOn(cceOutBoundDetailsObj.getString(28));
					rcBean.setDialedCount(cceOutBoundDetailsObj.getInt(29));
					rcBean.setReAssignFlag(cceOutBoundDetailsObj.getInt(30));
					count.add(rcBean);
				}
				map.put(size, count);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (cceOutBoundDetailsObj != null) {
					cceOutBoundDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return map;
	}

	@Override
	public Response addReassignRemark(Long id, String reAssignRemarks, Integer reAssignFlag, Integer reAssignUser) {
		Response response = new Response();
		try {
			Cce cceDb = cceRepository.findById(id).get();
			if (Objects.nonNull(cceDb)) {
				cceDb.setReAssignFlag(1);
				cceDb.setReAssignRemark(reAssignRemarks);
				cceDb.setReAssignDate(new Date());
				cceDb.setReAssignUserId(reAssignUser);
				cceRepository.save(cceDb);
				response.setMessage("Record Saved Successfully");
				response.setStatus("Success");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<UserDetails> getUserNameByGroupId() {
		List<UserDetails> findUserName = userDetailsRepository.findDetails();
		return findUserName;
	}

	@Override
	public String getDistrictList(String stateCode, Long userId) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet dcDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_DISTRICT_LIST")
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.execute();
			dcDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (dcDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("districtId", dcDetailsObj.getLong(2));
				jsonObject.put("districtCode", dcDetailsObj.getString(3));
				jsonObject.put("districtName", dcDetailsObj.getString(4));
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (dcDetailsObj != null) {
					dcDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return jsonArray.toString();
	}

	@Override
	public String getHospital(String stateCode, String distCode, Long userId) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet dcDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_HOSPITAL_LIST")
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.execute();
			dcDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (dcDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("dcUserId", dcDetailsObj.getLong(1));
				jsonObject.put("stateCode", dcDetailsObj.getString(2));
				jsonObject.put("districtCode", dcDetailsObj.getString(3));
				jsonObject.put("districtName", dcDetailsObj.getString(4));
				jsonObject.put("hospitalCode", dcDetailsObj.getString(5));
				jsonObject.put("hospitalName", dcDetailsObj.getString(6));
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (dcDetailsObj != null) {
					dcDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return jsonArray.toString();
	}

	@Override
	public Map<Long, List<Object>> getITACceOutBoundData(String userId, String formDate, String toDate, String action,
			String hospitalCode, String stateCode, String distCode) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> count = new ArrayList<Object>();
		ResultSet cceOutBoundDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CCE_ITA_LIST")
					.registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userId);
			storedProcedureQuery.setParameter("P_FROM_DATE", new SimpleDateFormat("dd-MMM-yyyy").parse(formDate));
			storedProcedureQuery.setParameter("P_TO_DATE", new SimpleDateFormat("dd-MMM-yyyy").parse(toDate));
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.execute();
			cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (cceOutBoundDetailsObj.next()) {
				CceOutBoundBean rcBean = new CceOutBoundBean();
				if (cceOutBoundDetailsObj != null) {
					rcBean.setStatus(false);
				} else {
					rcBean.setStatus(true);
				}
				rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
				rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
				rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
				rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
				rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
				rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
				rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
				rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
				rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
				rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
				rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
				rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
				rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
				rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
				rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
				rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
				rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
				rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
				rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
				rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
				rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
				rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
				rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
				rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
				rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
				rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
				rcBean.setCreatedOn(cceOutBoundDetailsObj.getString(27));
				rcBean.setState(cceOutBoundDetailsObj.getString(28));
				rcBean.setDcRemarks(cceOutBoundDetailsObj.getString(29));
				rcBean.setDgoRemarks(cceOutBoundDetailsObj.getString(30));
				rcBean.setGoQueryRemarks(cceOutBoundDetailsObj.getString(31));
				rcBean.setITAFlag(cceOutBoundDetailsObj.getInt(32));
				count.add(rcBean);
			}
			map.put(Long.parseLong(String.valueOf(count.size())), count);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (cceOutBoundDetailsObj != null) {
					cceOutBoundDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return map;
	}

	@Override
	public Map<Long, List<Object>> getITADgoCallCenterData(String userId, String formDate, String toDate, String action,
			String hospitalCode, String stateCode, String distCode) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> count = new ArrayList<Object>();
		Long size = null;
		ResultSet cceOutBoundDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CCE_ITA_LIST")
					.registerStoredProcedureParameter("P_USER_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", Integer.parseInt(userId));
			storedProcedureQuery.setParameter("P_FROM_DATE", new SimpleDateFormat("dd-MMM-yyyy").parse(formDate));
			storedProcedureQuery.setParameter("P_TO_DATE", new SimpleDateFormat("dd-MMM-yyyy").parse(toDate));
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.execute();
			cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");

			while (cceOutBoundDetailsObj.next()) {
				CceOutBoundBean rcBean = new CceOutBoundBean();
				if (cceOutBoundDetailsObj != null) {
					rcBean.setStatus(false);
				} else {
					rcBean.setStatus(true);
				}
				rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
				rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
				rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
				rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
				rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
				rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
				rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
				rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
				rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
				rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
				rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
				rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
				rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
				rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
				rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
				rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
				rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
				rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
				rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
				rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
				rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
				rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
				rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
				rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
				rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
				rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
				rcBean.setCreatedOn(cceOutBoundDetailsObj.getString(27));
				rcBean.setState(cceOutBoundDetailsObj.getString(28));
				rcBean.setDcRemarks(cceOutBoundDetailsObj.getString(29));
				rcBean.setDgoRemarks(cceOutBoundDetailsObj.getString(30));
				rcBean.setGoQueryRemarks(cceOutBoundDetailsObj.getString(31));
				rcBean.setDcSubmittedDate(cceOutBoundDetailsObj.getString(32));
				rcBean.setDoc1(cceOutBoundDetailsObj.getString(33));
				rcBean.setDoc2(cceOutBoundDetailsObj.getString(34));
				rcBean.setDoc3(cceOutBoundDetailsObj.getString(35));
				rcBean.setDCUserId(cceOutBoundDetailsObj.getString(36));
				rcBean.setITAFlag(cceOutBoundDetailsObj.getInt(37));
				count.add(rcBean);
			}
			map.put(Long.parseLong(String.valueOf(count.size())), count);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (cceOutBoundDetailsObj != null) {
					cceOutBoundDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return map;
	}

	@Override
	public Response saveReassignRemark(Long id, String reAssignRemarks, Integer reAssignUser, Long cceUserId) {
		Response response = new Response();
		try {
			Optional<Cce> cceOptional = cceRepository.findById(id);
			cceOptional.ifPresent(cceDb -> {
				cceDb.setReAssignFlag(1);
				cceDb.setReAssignRemark(reAssignRemarks);
				cceDb.setReAssignDate(new Date());
				cceDb.setReAssignUserId(reAssignUser);
				cceDb.setExecutiveUserId(cceUserId);
				cceRepository.save(cceDb);
				response.setMessage("Record Saved Successfully");
				response.setStatus("Success");
			});
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}
}
