package com.project.bsky.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import com.project.bsky.bean.CDMOForwardBean;
import com.project.bsky.bean.FeedbackCallingReport;
import com.project.bsky.bean.LoggedUserDetailsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.config.dto.NotingDto;
import com.project.bsky.config.dto.NotingParameter;
import com.project.bsky.entity.OnlineServiceApproval;
import com.project.bsky.entity.OnlineServiceApprovalNotings;
import com.project.bsky.entity.QueryDocEntity;
import com.project.bsky.entity.TSetAuthority;
import com.project.bsky.entity.TSetWorkFlow;
import com.project.bsky.model.CDMOConfiguration;
import com.project.bsky.model.User;
import com.project.bsky.repository.CDMOConfigurationRepository;
import com.project.bsky.repository.DistrictMasterRepository;
import com.project.bsky.repository.DynamicFormConfigurationAppRepository;
import com.project.bsky.repository.OnlineServiceApprovalNotingsRepository;
import com.project.bsky.repository.OnlineServiceApprovalRepository;
import com.project.bsky.repository.ProcessRepository;
import com.project.bsky.repository.QueryDocRepo;
import com.project.bsky.repository.TSetAuthorityRepository;
import com.project.bsky.repository.TSetWorkFlowRepository;
import com.project.bsky.repository.UserRepository;
import com.project.bsky.service.WorkflowService;
import com.project.bsky.util.ConvertClobToJson;
import com.project.bsky.util.PasswordEncrypter;
//import com.project.bsky.util.EmpActionDoc;

@Service
public class WorkflowServiceImpl implements WorkflowService {

	@Autowired
	private Logger logger;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	DynamicFormConfigurationAppRepository dynamicFormConfigurationAppRepository;

	@Autowired
	OnlineServiceApprovalRepository onlineServiceApprovalRepository;

	@Autowired
	UserRepository userRepository;
	@Autowired
	OnlineServiceApprovalNotingsRepository onlineServiceApprovalNotingsRepository;

	@Autowired
	DistrictMasterRepository districtMasterRepository;

	String state_id = "";

	@Autowired
	QueryDocRepo docRepo;
	/*
	 * @Autowired static AuthorityRepository authorityRepository;
	 */
	@Autowired
	private TSetWorkFlowRepository tSetWorkFlowRepository;

	@Autowired
	TSetAuthorityRepository tSetAuthorityRepository;

	@Autowired
	ProcessRepository processRepository;

	@Autowired
	private SessionFactory commonSessionFactory;

	@Autowired
	CDMOConfigurationRepository cdmoRepo;
	@PersistenceContext
	private EntityManager entityManager;

	EntityManager em;

	String dist_id = "";

	LoggedUserDetailsBean bean = new LoggedUserDetailsBean();

	LoggedUserDetailsBean resp = new LoggedUserDetailsBean();

	String userGroupId = "";

	String userName = "";

	@Override
	public List<Map<String, Object>> getallOfficersApi() {

		List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();

		jdbcTemplate.query(" select TYPE_ID,GROUP_TYPE_NAME from GROUP_TYPE ", new RowCallbackHandler() {
			public void processRow(ResultSet resultSet) throws SQLException {
				JSONObject object = null;
				do {

					try {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("intRoleId", resultSet.getInt("TYPE_ID"));
						map.put("vchRoleName", resultSet.getString("GROUP_TYPE_NAME"));
						myList.add(map);

					} catch (Exception e) {
						logger.error(ExceptionUtils.getStackTrace(e));
					}

				} while (resultSet.next());

			}
		});

		return myList;

	}

	@Override
	public List<Map<String, Object>> getallApprovalAction() {

		List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();

		jdbcTemplate.query(
				" select tinApprovalActionId,vchActionName from m_approval_actions where bitDeletedFlag = 0 ",
				new RowCallbackHandler() {
					public void processRow(ResultSet resultSet) throws SQLException {
						JSONObject object = null;
						do {

							try {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("tinApprovalActionId", resultSet.getInt("tinApprovalActionId"));
								map.put("vchActionName", resultSet.getString("vchActionName"));
								myList.add(map);

							} catch (Exception e) {
								logger.error(ExceptionUtils.getStackTrace(e));
							}

						} while (resultSet.next());

					}
				});

		return myList;
	}

	@Override
	public JSONObject fillWorkflow(String serviceId, String labelId) {
		JSONObject object = new JSONObject();
		int lb = Integer.parseInt(labelId);
		String qry = " select canvasData from t_set_workflow where deletedFlag = 0 and serviceId=" + serviceId;
		if (lb > 0) {
			qry += " and  labelId=" + lb;
		}

		jdbcTemplate.query(qry, new RowCallbackHandler() {
			public void processRow(ResultSet resultSet) throws SQLException {
				// JSONObject object = new JSONObject();

				do {

					try {
						String unEscapedHTML = StringEscapeUtils.unescapeHtml4(resultSet.getString("canvasData"));
						object.put("result", unEscapedHTML);

					} catch (Exception e) {
						logger.error(ExceptionUtils.getStackTrace(e));
					}

				} while (resultSet.next());

			}

		});

		return object;
	}

	@Transactional
	@Override
	public String setWorkflow(String setWorkflow) throws Exception {

		long millis = System.currentTimeMillis();
		JSONObject workFlowReq = new JSONObject(setWorkflow);
		JSONArray stageData = null;
		String response = null;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		KeyHolder keyHolder2 = new GeneratedKeyHolder();
		KeyHolder keyHolder3 = new GeneratedKeyHolder();
		KeyHolder keyHolder4 = new GeneratedKeyHolder();
		String arrays = workFlowReq.get("stageData").toString();
		if (workFlowReq.has("stageData")) {
			stageData = new JSONArray(arrays);
		}

		try {
			String escapedHTML = StringEscapeUtils.escapeHtml4(workFlowReq.getString("drawData"));

			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(" UPDATE t_set_workflow SET deletedFlag = 1 WHERE serviceId= "
								+ workFlowReq.getInt("serviceId") + " AND deletedFlag = 0 ");
					} catch (SQLException e1) {
						logger.error(ExceptionUtils.getStackTrace(e1));
					} catch (JSONException e1) {
						logger.error(ExceptionUtils.getStackTrace(e1));
					}

					try {

					} catch (Exception e) {
						logger.error(ExceptionUtils.getStackTrace(e));
					}

					return ps;
				}
			}, keyHolder3);

			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection
								.prepareStatement(" UPDATE t_set_authority SET bitDeletedFlag = 1 WHERE intProcessId = "
										+ workFlowReq.getInt("serviceId") + " AND bitDeletedFlag = 0 ");
					} catch (SQLException e1) {
						logger.error(ExceptionUtils.getStackTrace(e1));
					} catch (JSONException e1) {
						logger.error(ExceptionUtils.getStackTrace(e1));
					}

					try {

					} catch (Exception e) {
						logger.error(ExceptionUtils.getStackTrace(e));
					}

					return ps;
				}
			}, keyHolder4);
			TSetWorkFlow tSetWorkflow = new TSetWorkFlow();
			tSetWorkflow.setProjectId(workFlowReq.getInt("projectId"));
			tSetWorkflow.setServiceId(workFlowReq.getInt("serviceId"));
			tSetWorkflow.setCanvasData(escapedHTML);
			tSetWorkflow.setDeletedFlag(0);
			tSetWorkflow.setCreatedBy(20);
			tSetWorkflow.setCreatedOn(new Date(millis));
			tSetWorkFlowRepository.save(tSetWorkflow);

			for (int i = 0; i <= stageData.length() - 1; i++) {
				JSONObject object = stageData.getJSONObject(i);
				long random = System.currentTimeMillis();
				String randomNum = String.valueOf(random) + "" + String.valueOf(object.getInt("tinStageNo")) + ""
						+ String.valueOf(i);
				BigInteger randomInt = new BigInteger(randomNum);
				TSetAuthority authority = new TSetAuthority();
				authority.setIntHierarchyId(0);
				authority.setIntSetAuthId(0);
				authority.setIntSetAuthLinkId(randomInt.intValue());
				authority.setIntSetLetterLinkId(randomInt.intValue());
				authority.setDtmCreatedOn(new Date(millis));
//				 authority.setIntApprovingAuth(intApprovingAuth);
				authority.setIntATAProcessId(object.getInt("parallel"));
				authority.setIntCreatedBy(20);
				// authority.setIntLabelId();
				authority.setIntPrimaryAuth(object.getInt("roleId"));
				authority.setIntProcessId(object.getInt("selProcess"));
				authority.setIntProjectId(object.getInt("selProject"));
				authority.setIntRoleId(object.getInt("roleId"));
				authority.setTinStageNo(object.getInt("tinStageNo"));
				authority.setTinTimeLine(object.getInt("timeline"));

				authority.setVchAuthTypes(object.getString("authActions"));

				String s = object.get("approvalDocuments").toString();
				authority.setJsnApprovalDocument(s);
				authority.setBitDeletedFlag((byte) 0);

				TSetAuthority saveAuthority = tSetAuthorityRepository.save(authority);

			}

		}

		catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		response = "success";

		return response;

	}

	@SuppressWarnings("all")
	@Override
	public JSONObject getAuthAction(String request) throws Exception {
		Session hibernateSession = null;
		JSONObject object = new JSONObject(request);
		List<JSONObject> list = new ArrayList<>();
		JSONObject response = new JSONObject();
		int labelId = 0;
		Integer queryCount = 0;
		if (Integer.valueOf(object.getString("processId")) != 583) {
			state_id = getStateId((long) object.getInt("serviceId"));

			if (state_id.equals("21")) {
				labelId = 1;
			} else {
				labelId = 2;
			}
		} else {
			queryCount = onlineServiceApprovalNotingsRepository.getQueryCount(
					Integer.valueOf(object.getString("serviceId")), Integer.valueOf(object.getString("pendingAt")));
			labelId = 1;
		}
		String sql = " SELECT distinct sa.intPendingAt,ta.vchAuthTypes,ta.intPrimaryAuth,ta.intSetLetterLinkId, "
				+ " to_char(ta.jsnApprovalDocument) jsnApprovalDocument,intApprovingAuth,intSetAuthLinkId FROM t_online_service_approval sa INNER JOIN t_set_authority ta on ta.intProcessId = sa.intProcessId "
				+ " WHERE ta.intProcessId =" + Integer.valueOf(object.getString("processId")) + " and ta.tinStageNo ="
				+ object.getInt("stageNo") + " and intOnlineServiceId ="
				+ +Integer.valueOf(object.getString("serviceId"))
				+ "and sa.bitDeletedFlag = 0 and ta.bitDeletedFlag = 0 " + " and ta.INTLABELID = " + labelId;
		hibernateSession = commonSessionFactory.openSession();
		Query query = hibernateSession.createSQLQuery(sql);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String, Object>> aliasToValueMapList = query.list();
		JSONObject tableObject = null;
		String approvingauth = null;
		for (Map<String, Object> map : aliasToValueMapList) {
			tableObject = new JSONObject();
			for (Map.Entry<String, Object> entry : map.entrySet()) {

				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("jsnApprovalDocument") && value != null) {
					String json = String.valueOf(value);
					tableObject.put(key, new JSONArray(json).toString());
				}
				if (key.equals("intApprovingAuth") && value == null) {
					tableObject.put(key, approvingauth);
				}
				if (key.equals("VCHAUTHTYPES") && value != null) {
					String temp = value.toString();
					if (queryCount >= 3) {
						char charToRemove = '6';
						StringBuilder modifiedString = new StringBuilder();

						for (int i = 0; i < temp.length(); i++) {
							char currentChar = temp.charAt(i);
							if (currentChar != charToRemove) {
								modifiedString.append(currentChar);
							}
						}
						String result = modifiedString.toString();
						result = result.replaceAll("^,|,$", "");
						result = result.replaceAll(",+", ",");
						tableObject.put(key, result);
					} else {
						tableObject.put(key, temp);
					}
				}

				else {
					tableObject.put(key, value);
				}

			}
			list.add(tableObject);

		}

		String sql2 = "SELECT tinApprovalActionId,vchActionName FROM m_approval_actions WHERE bitDeletedFlag = 0 AND tinDisplayStatus = 1";

		Query query2 = hibernateSession.createSQLQuery(sql2);
		List<Object[]> objList = query2.list();
		JSONObject actionValueObj = null;
		if (!objList.isEmpty()) {
			actionValueObj = new JSONObject();
			for (Object[] obj : objList) {
				actionValueObj.put(String.valueOf(obj[0]), obj[1]);
			}

		}

		response.put("dataRes", list);
		response.put("QueryCount", queryCount);
		response.put("actions", actionValueObj);
		hibernateSession.close();

		return response;

	}

	@SuppressWarnings("all")
	@Override
	public JSONObject getApplication(String request) throws Exception {
		JSONObject object = new JSONObject(request);
		JSONObject response = new JSONObject();
		JSONArray headerArray = null;
		Session hibernateSession = null;
		JSONArray returnArray = new JSONArray();
		JSONArray docArray = new JSONArray();
		JSONArray mediaArray = new JSONArray();
		String docs = null;
		String media = null;
		JSONArray documentUpload = new JSONArray();
		JSONObject jsonDoc = new JSONObject();
		CDMOConfiguration loc = new CDMOConfiguration();
		Integer role = object.getInt("pendingAt");
		String distList = " ";
		String stateList = "";
		String hosList = " ";
		if (object.getString("itemId").equalsIgnoreCase("583") && resp.getGroupType().equals("12")) {
			loc = cdmoRepo.getCDMOByUserId(Integer.parseInt(resp.getUserName()));
		}

		String distInt = "";
		Iterator<String> cityIterator;
		Iterator<String> dcUserIterator;
		Iterator<String> hospitalIterator;
		Iterator<String> stateIterator;
		try {
			if (object.getString("itemId").equals("583")) {
				Integer authority = 0;
				String sql = "Select TA.NFSA_SFSA_CARD_NO,TA.EMAIL,AP.STMCREATEDON,TA.CONTACT_NO,BA.INTONLINESERVICEID,BA.INTPROCESSID, TA.GRIEVANCE_MEDIUM_ID,"
						+ "BA.INTUPDATEDBY,BA.VCHAPPLICATIONNO, TA.GRIEVANCE_DESCRIPTION , (select GRIEVANCETYPE_NAME from bsky_mst_grievancetype where GRIEVANCETYPE_ID= TA.GRIEVANCETYPE_ID)AS GRIEVANCETYPE ,"
						+ "AP.intPendingAt, (select GRIEVANCEBY_NAME from bsky_mst_grievanceby where  GRIEVANCEBY_ID= TA.GRIEVANCEBY_ID) AS GRIEVANCEBY_NAME, TA.CASE_TYPE,TA.PRIORITY_TYPE,TA.BENIFICIARY_NAME ,TA.IS_HOSPITAL_UNDER_BSKY,TA.IS_HOSPITAL_ENROLL_UNDER_BSKY,(select HOSPITAL_NAME from HOSPITAL_INFO where HOSPITAL_CODE=TA.HOSPITAL_NAME)AS HOSPITALNAME,"
						+ "AP.INTSENTFROM, ( select STATENAME from state where STATECODE=TA.H_STATE_CODE)As STATECODE,(select HOSPITAL_NAME||'('||HOSPITAL_CODE||')' from HOSPITAL_INFO where HOSPITAL_CODE=TA.HOSPITAL_NAME) As HOSPITALDETAILS, AP.INTUPDATEDBY AS GRIEVANCE_UPDATE ,"
						+ "(select DISTRICTNAME from district where DISTRICTCODE=TA.H_DISTRICT_CODE and STATECODE=TA.H_STATE_CODE)As DISTRICTCODE , (select count(1) from t_online_serv_app_notings N where N.INTONLINESERVICEID = AP.INTONLINESERVICEID AND INTFROMAUTHORITY= "
						+ object.getInt("pendingAt") + " ) as ccount,"
						+ "AP.intStageNo, AP.intATAProcessId, AP.tinStatus,AP.DTMSTATUSDATE, AP.tinQueryTo, "
						+ "AP.tinResubmitStatus,AP.dtmApprovalDate, (select NVL( LISTAGG(group_type_name, ',') WITHIN GROUP (ORDER BY group_type_name) "
						+ ",'') from group_type GR where gr.type_id=AP.intPendingAt)as pendingAuth,(select GRIEVANCE_MEDIUM_NAME from BSKY_MST_GRIEVANCE_MEDIUM where to_char(GRIEVANCE_MEDIUM_ID)=TA.GRIEVANCE_MEDIUM_ID) AS GRIEVANCE_MEDIUM_NAME, "
						+ "to_char(t1.TXTNOTING) AS TXTNOTING,t1.INTFROMAUTHORITY AS INTFROMAUTHORITY,TA.ASSIGNED_DC AS ASSIGNED_DC,U.FULL_NAME AS FULLNAME,"
					
						+ "(SELECT NVL( LISTAGG(BGD.DOCUMENT_NAME, ',') WITHIN GROUP (ORDER BY BGD.INTID) ,'') FROM bsky_grievance_document BGD WHERE BGD.INTPARENTID=BA.INTONLINESERVICEID AND BGD.BITDELETEDFLAG=0)DOCUMENT_NAME "
						+ ",(SELECT NVL( LISTAGG(BGD1.MEDIA_NAME, ',') WITHIN GROUP (ORDER BY BGD1.INTID) ,'') FROM bsky_grievance_document BGD1 WHERE BGD1.INTPARENTID=BA.INTONLINESERVICEID AND BGD1.BITDELETEDFLAG=0)MEDIA_NAME,to_char(t1.JSNOTHERDETAILS) as JSNOTHERDETAILS,to_char(t1.JSNOMEDIADETAILS) as JSNOMEDIADETAILS "
						+ "FROM BSKY_GRIEVANCE_REGISTRATION TA "
						+ "Left Join HOSPITAL_INFO H ON H.HOSPITAL_CODE=TA.HOSPITAL_NAME "

						+ "Left Join userdetails U ON U.USERID=H.ASSIGNED_DC "
						+ "JOIN t_online_service_application BA ON BA.INTONLINESERVICEID = TA.INTONLINESERVICEID AND BA.BITDELETEDFLAG=0 AND "
						+ "TA.BITDELETEDFLAG=0 "
						+ "JOIN t_online_service_approval AP ON AP.INTONLINESERVICEID = BA.INTONLINESERVICEID AND AP.BITDELETEDFLAG = 0 "
						+ "LEFT JOIN (SELECT t1.*,ROW_NUMBER() OVER(PARTITION BY INTONLINESERVICEID ORDER BY DTACTIONTAKEN DESC ) AS seqnum_d "
						+ " FROM t_online_serv_app_notings t1 ) t1 ON t1.INTONLINESERVICEID=AP.INTONLINESERVICEID  AND t1.seqnum_d = 1 "
						+ "WHERE BA.INTPROCESSID=" + object.getString("itemId");
				
//				String sql = "SELECT " + "TA.NFSA_SFSA_CARD_NO," + "TA.EMAIL," + "AP.STMCREATEDON," + "TA.CONTACT_NO," + 
//				"BA.INTONLINESERVICEID," + "BA.INTPROCESSID," + "TA.GRIEVANCE_MEDIUM_ID,"
//						+ "BA.INTUPDATEDBY," + "BA.VCHAPPLICATIONNO," + "TA.GRIEVANCE_DESCRIPTION," + "(SELECT GRIEVANCETYPE_NAME FROM bsky_mst_grievancetype WHERE GRIEVANCETYPE_ID = TA.GRIEVANCETYPE_ID) AS GRIEVANCETYPE," 
//				+ "AP.intPendingAt," + "(SELECT GRIEVANCEBY_NAME FROM bsky_mst_grievanceby WHERE GRIEVANCEBY_ID = TA.GRIEVANCEBY_ID) AS GRIEVANCEBY_NAME," + "TA.CASE_TYPE," + "TA.PRIORITY_TYPE," + "TA.BENIFICIARY_NAME," + "TA.IS_HOSPITAL_UNDER_BSKY," + "TA.IS_HOSPITAL_ENROLL_UNDER_BSKY," + "(SELECT HOSPITAL_NAME FROM HOSPITAL_INFO WHERE HOSPITAL_CODE = TA.HOSPITAL_NAME) AS HOSPITALNAME," + "AP.INTSENTFROM," + "(SELECT STATENAME FROM state WHERE STATECODE = TA.H_STATE_CODE) AS STATECODE," + "(SELECT HOSPITAL_NAME || '(' || HOSPITAL_CODE || ')' FROM HOSPITAL_INFO WHERE HOSPITAL_CODE = TA.HOSPITAL_NAME) AS HOSPITALDETAILS," + "AP.INTUPDATEDBY AS GRIEVANCE_UPDATE," + "(SELECT DISTRICTNAME FROM district WHERE DISTRICTCODE = TA.H_DISTRICT_CODE AND STATECODE = TA.H_STATE_CODE) AS DISTRICTCODE," + "(SELECT COUNT(1) FROM t_online_serv_app_notings N WHERE N.INTONLINESERVICEID = AP.INTONLINESERVICEID AND INTFROMAUTHORITY = ?) AS ccount," + "AP.intStageNo," + "AP.intATAProcessId," + "AP.tinStatus," + "AP.DTMSTATUSDATE," + "AP.tinQueryTo," + "AP.tinResubmitStatus," + "AP.dtmApprovalDate," + "(SELECT NVL(LISTAGG(group_type_name, ',') WITHIN GROUP (ORDER BY group_type_name), '') FROM group_type GR WHERE gr.type_id = AP.intPendingAt) AS pendingAuth," + "(SELECT GRIEVANCE_MEDIUM_NAME FROM BSKY_MST_GRIEVANCE_MEDIUM WHERE to_char(GRIEVANCE_MEDIUM_ID) = TA.GRIEVANCE_MEDIUM_ID) AS GRIEVANCE_MEDIUM_NAME," + "TO_CHAR(t1.TXTNOTING) AS TXTNOTING," + "t1.INTFROMAUTHORITY AS INTFROMAUTHORITY," + "UD.USERNAME AS ASSIGNED_DC_USERNAME," + "(SELECT NVL(LISTAGG(BGD.DOCUMENT_NAME, ',') WITHIN GROUP (ORDER BY BGD.INTID), '') FROM bsky_grievance_document BGD WHERE BGD.INTPARENTID = BA.INTONLINESERVICEID AND BGD.BITDELETEDFLAG = 0) AS DOCUMENT_NAME," + "(SELECT NVL(LISTAGG(BGD1.MEDIA_NAME, ',') WITHIN GROUP (ORDER BY BGD1.INTID), '') FROM bsky_grievance_document BGD1 WHERE BGD1.INTPARENTID = BA.INTONLINESERVICEID AND BGD1.BITDELETEDFLAG = 0) AS MEDIA_NAME," + "TO_CHAR(t1.JSNOTHERDETAILS) AS JSNOTHERDETAILS," + "TO_CHAR(t1.JSNOMEDIADETAILS) AS JSNOMEDIADETAILS " + "FROM BSKY_GRIEVANCE_REGISTRATION TA " + "JOIN t_online_service_application BA ON BA.INTONLINESERVICEID = TA.INTONLINESERVICEID AND BA.BITDELETEDFLAG = 0 AND TA.BITDELETEDFLAG = 0 " + "JOIN t_online_service_approval AP ON AP.INTONLINESERVICEID = BA.INTONLINESERVICEID AND AP.BITDELETEDFLAG = 0 " + "LEFT JOIN (SELECT t1.*, ROW_NUMBER() OVER (PARTITION BY INTONLINESERVICEID ORDER BY DTACTIONTAKEN DESC) AS seqnum_d FROM t_online_serv_app_notings t1) t1 ON t1.INTONLINESERVICEID = AP.INTONLINESERVICEID AND t1.seqnum_d = 1 " + "LEFT JOIN USERDETAILS UD ON UD.USERID = TA.ASSIGNED_DC "
//						+ "WHERE BA.INTPROCESSID=" + object.getString("itemId");			
				if (userGroupId.equals("12")) {
					if (!loc.getStateCode().equalsIgnoreCase("0"))
						sql = sql + " AND TA.H_STATE_CODE= " + loc.getStateCode();
					if (!loc.getDistrictCode().equalsIgnoreCase("0"))
						sql = sql + " AND TA.H_DISTRICT_CODE= " + loc.getDistrictCode();
				} else if (userGroupId.equals("6")) {
					String Dcsql2 = "Select unique UD.HOSPITAL_CODE from HOSPITAL_INFO UD where " + "UD.ASSIGNED_DC=?"
							+ " AND UD.STATUS_FLAG=0 ";
					List<String> data1 = jdbcTemplate.queryForList(Dcsql2, new Object[] { userName }, String.class);
					String hostList = "";
					StringBuilder hosList1 = new StringBuilder();
					hosList1.append("(");
					cityIterator = data1.iterator();
					if (role != null && role != 99) {
						while (cityIterator.hasNext()) {
							hosList1.append("'");
							hosList1.append(cityIterator.next());
							hosList1.append("',");
						}
						hosList1.append(")");
						hostList = hosList1.deleteCharAt(hosList1.length() - 2).toString();
						sql = sql + " AND ( TA.HOSPITAL_NAME IN " + hostList + " OR TA.ASSIGNED_DC="
								+ Integer.parseInt(resp.getUserName()) + ") ";
					}
				}

				if (Integer.parseInt(object.getString("serviceId")) > 0) {
					sql = sql + " AND BA.INTONLINESERVICEID= " + object.getString("serviceId");
				}
				if (role != null && role != 30 && role != 99) {
					if ((role != 37 && role != 41 && role != 38 && role != 12)) {
						if (object.has("pageType")) {

							if (object.has("pendingAt") && object.getInt("pendingAt") != 0
									&& object.getString("pageType").equals("pen")) {
								sql = sql + " AND AP.intPendingAt='" + object.getInt("pendingAt") + "'";

							} else if (object.has("pendingAt") && object.getInt("pendingAt") == 0
									&& object.getString("pageType").equals("sum")) {
								sql = sql + " AND AP.tinStatus IN (1,2,8,7,3) ";
							} else if (object.has("pendingAt") && object.getInt("pendingAt") != 0
									&& object.getString("pageType").equals("rev")) {
								sql = sql + " AND AP.tinStatus= " + 3;
							} else if (object.has("pendingAt") && object.getInt("pendingAt") != 0
									&& object.getString("pageType").equals("app")) {
								sql = sql + " AND AP.tinStatus= " + 8;
							} else if (object.has("pendingAt") && object.getInt("pendingAt") != 0
									&& object.getString("pageType").equals("rej")) {
								sql = sql + " AND AP.tinStatus= " + 7;
							}

						} else {
							sql = sql + " AND AP.intPendingAt='" + object.getInt("pendingAt") + "'";
						}
					} else if (role == 12) {
						sql = sql + " AND TA.HOSPITAL_NAME='0' AND TA.GRIEVANCE_MEDIUM_ID=2 ";
					}
				}
				if (object.has("mode")) {
					if (object.get("mode").equals("search")) {
						if (object.getString("formDate") != null && !(object.getString("formDate").equals(""))) {
							sql = sql + " AND AP.STMCREATEDON  between to_date('" + object.getString("formDate")
									+ "','dd-mon-yyyy') AND to_date('" + object.getString("toDate")
									+ "','dd-mon-yyyy' )+1 ";
							if (!(object.getString("stateCode").equals(""))) {
								sql = sql + " AND TA.H_STATE_CODE= " + Integer.parseInt(object.getString("stateCode"));
							}
							if (object.has("distCode") && (!(object.getString("distCode").equals("")))) {
								sql = sql + " AND TA.H_DISTRICT_CODE= "
										+ Integer.parseInt(object.getString("distCode"));

							}
							if (object.has("hostCode") && (!(object.getString("hostCode").equals("")))) {
								sql = sql + " AND TA.HOSPITAL_NAME= " + Integer.parseInt(object.getString("hostCode"));

							} // grievancebyId

							if (object.has("grievancebyId") && (!(object.getString("grievancebyId").equals("")))) {
								sql = sql + " AND TA.GRIEVANCEBY_ID= "
										+ Integer.parseInt(object.getString("grievancebyId"));

							}
							if (object.has("lstAct") && (!(object.getString("lstAct").equals("")))) {
								if (object.getString("lstAct").equals("DC")) {
									sql = sql + " AND AP.INTSENTFROM= " + 6;
								}
								if (object.getString("lstAct").equals("DGO")) {
									sql = sql + " AND AP.INTSENTFROM= " + 30;
								}
								if (object.getString("lstAct").equals("GO")) {
									sql = sql + " AND AP.INTSENTFROM= " + 99;
								}

							}

							if (object.has("pendingApplication")
									&& (!(object.getString("pendingApplication").equals("")))) {
								if (object.getString("pendingApplication").equals("DC")) {
									sql = sql + " AND AP.intPendingAt= " + 6;
								}
								if (object.getString("pendingApplication").equals("DGO")) {
									sql = sql + " AND AP.intPendingAt= " + 30;
								}
								if (object.getString("pendingApplication").equals("GO")) {
									sql = sql + " AND AP.intPendingAt= " + 99;
								}

							}
							if (object.has("DCcaseType") && (!(object.getString("DCcaseType").equals("")))) {
								if (object.getString("DCcaseType").equals("CD")) {
									sql = sql + " AND TA.ASSIGNED_DC=" + Integer.parseInt(resp.getUserName());
								}
								if (object.getString("DCcaseType").equals("FR")) {
									sql = sql + " AND TA.ASSIGNED_DC is null";
								}

							}
							if (object.has("GrvcaseType") && (!(object.getString("GrvcaseType").equals("")))) {
								if (role == 30) {
									if (object.getString("GrvcaseType").equals("Q")) {
										sql = sql
												+ " AND (AP.INTSENTFROM=99  AND AP.TINSTATUS=6 AND AP.INTPENDINGAT=30) ";
									} else if (object.getString("GrvcaseType").equals("N")) {
										sql = sql
												+ " AND AP.INTONLINESERVICEID not in (select INTONLINESERVICEID from t_online_service_approval \r\n"
												+ "where INTSENTFROM=99  AND TINSTATUS=6 AND INTPENDINGAT=30) ";
									}
								}
								if (role == 99) {
									if (object.getString("GrvcaseType").equals("Q")) {
										sql = sql + " AND AP.INTSENTFROM=30 and AP.INTPENDINGAT=99\r\n"
												+ "AND EXISTS (SELECT 1 FROM T_Online_Serv_App_Notings LG where LG.INTONLINESERVICEID=AP.INTONLINESERVICEID \r\n"
												+ "AND LG.INTPROCESSID=583 and LG.INTSTATUS=6 and LG.INTFROMAUTHORITY=99) ";
									} else if (object.getString("GrvcaseType").equals("N")) {
										sql = sql
												+ " AND NOT EXISTS (SELECT 1 FROM T_Online_Serv_App_Notings LG where LG.INTONLINESERVICEID=AP.INTONLINESERVICEID \r\n"
												+ "AND LG.INTPROCESSID=583 and LG.INTSTATUS=6 and LG.INTFROMAUTHORITY=99)";
									}
								}

							}
							if (object.has("grievancemediumId")
									&& (!(object.getString("grievancemediumId").equals("")))) {
								sql = sql + " AND to_char(TA.GRIEVANCE_MEDIUM_ID)= '"
										+ object.getString("grievancemediumId") + "'";

							}
							if (object.has("dcUserIdList")) {
								List<String> string = Arrays.asList(object.getString("dcUserIdList").substring(1,
										object.getString("dcUserIdList").length() - 1));
								String temp = "";
								StringBuilder temp1 = new StringBuilder();
								temp1.append("(");
								dcUserIterator = string.iterator();
								while (dcUserIterator.hasNext()) {
									temp1.append(dcUserIterator.next());
									temp1.append(",");
								}
								temp1.append(")");
								temp = temp1.deleteCharAt(temp1.length() - 2).toString();
								if (temp.length() > 2) {
									sql = sql
											+ " AND ( TA.HOSPITAL_NAME IN (select HOSPITAL_CODE from HOSPITAL_INFO where ASSIGNED_DC IN "
											+ temp + " AND STATUS_FLAG=0) OR TA.ASSIGNED_DC IN" + temp + ") ";
								}
							}
							if (object.has("hospitalCodeList")) {
								String string1 = object.getString("hospitalCodeList").substring(1,
										object.getString("hospitalCodeList").length() - 1);
								String[] str = string1.split(",");
								String temp1 = "";
								StringBuilder temp2 = new StringBuilder();
								temp2.append("(");
								for (int i = 0; i < str.length; i++) {
									temp2.append("'");
									temp2.append(str[i]);
									temp2.append("',");
								}
								temp2.append(")");
								temp1 = temp2.deleteCharAt(temp2.length() - 2).toString();
								if (temp1.length() > 4) {
									sql = sql + " AND TA.HOSPITAL_NAME IN " + temp1;
								}
							}
						}
					}
					if (object.get("mode").equals("user") || object.get("pageType").equals("sum")) {
						sql = sql + " AND AP.intPendingAt  NOT IN (" + object.getInt("pendingAt") + ")";
					}

				}
				sql = sql + " ORDER BY  AP.STMCREATEDON DESC";
				if (object.has("mode")) {
					if (object.get("mode").equals("user") || object.get("pageType").equals("sum")) {
						sql = " select * from( " + sql + ") where ccount >0 ";
					}
				}
				hibernateSession = commonSessionFactory.openSession();
				Query query = hibernateSession.createSQLQuery(sql);
				query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				List<Map<String, Object>> aliasToValueMapList = query.list();
				String date = null;
				JSONArray json = new JSONArray();
				for (Map<String, Object> map : aliasToValueMapList) {
					JSONObject tableObject = new JSONObject();
					Integer serviceId = 0;
					Integer intId = 0;
					for (Map.Entry<String, Object> entry : map.entrySet()) {

						String key = entry.getKey();
						Object value = entry.getValue();
						if (key.equals("STMCREATEDON")) {
							date = value.toString();
							String[] dataFormat = date.split("[.]");
							SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
							java.util.Date date1 = sm.parse(dataFormat[0]);
							SimpleDateFormat sm1 = new SimpleDateFormat("dd-MMM-yyyy");
							String dateFormater = sm1.format(date1);
							tableObject.put(key, dateFormater);
						} else if (key.equals("DOCUMENT_NAME")) {
							if (value != null && value != "" && !value.equals("null")) {
								docs = value.toString();
								String[] dataFormat = docs.split("[,]");
								for (int i = 0; i < dataFormat.length; i++) {
									docArray.put(PasswordEncrypter.encryptPassword(dataFormat[i]));
								}
							}
						} else if (key.equals("MEDIA_NAME")) {
							if (value != null && value != "" && !value.equals("null")) {
								media = value.toString();
								String[] dataFormat = media.split("[,]");
								for (int i = 0; i < dataFormat.length; i++) {
									mediaArray.put(PasswordEncrypter.encryptPassword(dataFormat[i]));
								}
							}
						} else if (key.equals("JSNOTHERDETAILS")) {
							if (value != null && value != "" && !value.equals("null")) {
								jsonDoc.put("JSNOTHERDETAILS", value.toString());
							}
						} else if (key.equals("JSNOMEDIADETAILS")) {
							if (value != null && value != "" && !value.equals("null")) {
								jsonDoc.put("JSNOMEDIADETAILS", value.toString());
							}
						} else if (key.equals("GRIEVANCE_MEDIUM_ID")) {
							if (value != null && !value.equals("null")) {
								int mediumId = Integer.parseInt(value.toString());
								if (mediumId == 10) {
									String pendingAt = (String) map.get("INTPENDINGAT");
									if (pendingAt.equalsIgnoreCase("6") && (role == 6 || role == 99)) {
										Calendar cal = Calendar.getInstance();
										BigDecimal tintstatus = (java.math.BigDecimal) map.get("TINSTATUS");
										BigDecimal fromAuth = (java.math.BigDecimal) map.get("INTFROMAUTHORITY");
										if (tintstatus.intValue() == 6 && fromAuth.intValue() == 30) {
											java.util.Date Updateddate = (java.util.Date) map.get("DTMSTATUSDATE");
											cal.setTime(Updateddate);
											cal.add(Calendar.DAY_OF_MONTH, 1);
										} else {
											java.util.Date date1 = (java.util.Date) map.get("STMCREATEDON");
											cal.setTime(date1);
											cal.add(Calendar.DAY_OF_MONTH, 3);
										}
										if (cal.getTime().before(new java.util.Date())) {
											tableObject.put("ISEXPIRYMOSARKAR", "TRUE");
										}
									} else if (pendingAt.equalsIgnoreCase("30") && (role == 30 || role == 99)) {
										Calendar cal = Calendar.getInstance();
										BigDecimal tintstatus = (java.math.BigDecimal) map.get("TINSTATUS");
										BigDecimal fromAuth = (java.math.BigDecimal) map.get("INTFROMAUTHORITY");
										if (tintstatus.intValue() == 1 && fromAuth.intValue() == 6) {
											java.util.Date Updateddate = (java.util.Date) map.get("DTMSTATUSDATE");
											cal.setTime(Updateddate);
											cal.add(Calendar.DAY_OF_MONTH, 3);
										}
										if (cal.getTime().before(new java.util.Date())) {
											tableObject.put("ISEXPIRYMOSARKAR", "TRUE");
										}
									}
								}
							}
						} else if (key.equals("INTUPDATEDBY")) {
							try {
								if (value != null) {
									String roleName = tSetWorkFlowRepository
											.getRoleName(Integer.parseInt(value.toString()));
									tableObject.put(key, roleName);
								} else {
									tableObject.put(key, map.get("GRIEVANCEBY_NAME"));
								}

							} catch (Exception e) {
								logger.error(ExceptionUtils.getStackTrace(e));

							}

						} else if (key.equals("INTONLINESERVICEID")) {
							serviceId = Integer.parseInt(value.toString());
							tableObject.put(key, value);

						} else if (key.equals("GRIEVANCE_UPDATE")) {
							if (value != null) {
								int userId = Integer.parseInt(value.toString());
								User user = userRepository.findByUserId(userId);
								tableObject.put(key, user.getFullName());
							}

						} else if (key.equals("INTID")) {
							intId = Integer.parseInt(value.toString());
						} else if (key.equals("STATECODE")) {
							try {
								tableObject.put(key, value.toString());
							} catch (Exception e) {
								logger.error(ExceptionUtils.getStackTrace(e));
								tableObject.put(key, "---");
							}
						} else if (key.equals("BENIFICIARY_NAME")) {
							try {
								if (value != null && !value.equals("null"))
									tableObject.put(key, value.toString());
								else
									tableObject.put(key, "");
							} catch (Exception e) {
								logger.error(ExceptionUtils.getStackTrace(e));
								tableObject.put(key, "");
							}
						} else if (key.equals("INTFROMAUTHORITY")) {
							if (value != null)
								authority = Integer.parseInt(value.toString());
						} else if (key.equals("ASSIGNED_DC") && map.get("INTSENTFROM") == null) {
							if (value != null && role == 6) {
								tableObject.put("INTSENTFROM", "12");
							}
						} else {

							tableObject.put(key, value);

						}

					}
					returnArray.put(tableObject);
					documentUpload.put(jsonDoc);
				}

				headerArray = new JSONArray();
				JSONObject columnObject8 = new JSONObject();
				columnObject8.put("colName", "VCHAPPLICATIONNO");
				columnObject8.put("colHeader", "Grievance Number");
				headerArray.put(columnObject8);
				if (role == 30 || role == 99 || role == 12) {
					JSONObject columnObject4 = new JSONObject();
					columnObject4.put("colName", "BENIFICIARY_NAME");
					columnObject4.put("colHeader", "Benificiary Name");
					headerArray.put(columnObject4);
				}
				JSONObject columnObject9 = new JSONObject();
				columnObject9.put("colName", "CONTACT_NO");
				columnObject9.put("colHeader", "Contact No");
				headerArray.put(columnObject9);
				JSONObject columnObject7 = new JSONObject();
				columnObject7.put("colName", "STATECODE");
				columnObject7.put("colHeader", "State");
				headerArray.put(columnObject7);

				JSONObject columnObject3 = new JSONObject();
				columnObject3.put("colName", "DISTRICTCODE");
				columnObject3.put("colHeader", "District");
				headerArray.put(columnObject3);

				if (role != 12) {
					JSONObject columnObject15 = new JSONObject();
					columnObject15.put("colName", "HOSPITALDETAILS");
					columnObject15.put("colHeader", "Hospital Details");
					headerArray.put(columnObject15);
				}
				JSONObject columnObject10 = new JSONObject();
				columnObject10.put("colName", "INTUPDATEDBY");
				columnObject10.put("colHeader", "Registered by");
				headerArray.put(columnObject10);
				if (role == 30 || role == 99 || role == 12) {
					JSONObject columnObject14 = new JSONObject();
					columnObject14.put("colName", "GRIEVANCE_MEDIUM_NAME");
					columnObject14.put("colHeader", "Grievance Medium Name");
					headerArray.put(columnObject14);
				}
				JSONArray grievanceHeaderArray = new JSONArray();
				JSONObject columnObjects8 = new JSONObject();
				columnObjects8.put("colName", "VCHAPPLICATIONNO");
				columnObjects8.put("colHeader", "Grievance No.");
				grievanceHeaderArray.put(columnObjects8);
				if (role == 99 || role == 30) {
					JSONObject columnObjects17 = new JSONObject();
					columnObjects17.put("colName", "BENIFICIARY_NAME");
					columnObjects17.put("colHeader", "Benificiary Name");
					grievanceHeaderArray.put(columnObjects17);
				}
				JSONObject columnObjects9 = new JSONObject();
				columnObjects9.put("colName", "CONTACT_NO");
				columnObjects9.put("colHeader", "Contact No.");
				grievanceHeaderArray.put(columnObjects9);

				JSONObject columnObjects3 = new JSONObject();
				columnObjects3.put("colName", "DISTRICTCODE");
				columnObjects3.put("colHeader", "District Name");
				grievanceHeaderArray.put(columnObjects3);
				JSONObject columnObjects7 = new JSONObject();
				columnObjects7.put("colName", "STATECODE");
				columnObjects7.put("colHeader", "State Name");
				grievanceHeaderArray.put(columnObjects7);
				JSONObject columnObjects10 = new JSONObject();
				columnObjects10.put("colName", "INTUPDATEDBY");
				columnObjects10.put("colHeader", "Registration By");
				grievanceHeaderArray.put(columnObjects10);
				JSONObject columnObjects11 = new JSONObject();
				columnObjects11.put("colName", "GRIEVANCETYPE");
				columnObjects11.put("colHeader", "Grievance Type");
				grievanceHeaderArray.put(columnObjects11);
				if (role != 12) {
					JSONObject columnObjects13 = new JSONObject();
					columnObjects13.put("colName", "HOSPITALNAME");
					columnObjects13.put("colHeader", "Hospital Name");
					grievanceHeaderArray.put(columnObjects13);
				}

				JSONObject columnObjects12 = new JSONObject();
				columnObjects12.put("colName", "GRIEVANCE_DESCRIPTION");
				columnObjects12.put("colHeader", "Grievance Description");
				grievanceHeaderArray.put(columnObjects12);
				if (role == 30 || role == 99) {
					JSONObject columnObjects15 = new JSONObject();
					if (authority == 6) {
						columnObjects15.put("colName", "TXTNOTING");
						columnObjects15.put("colHeader", "DC Remark");
					} else if (authority == 30) {
						columnObjects15.put("colName", "TXTNOTING");
						columnObjects15.put("colHeader", "Deputy Grievance Officer Remark");
					} else if (authority == 99) {
						columnObjects15.put("colName", "TXTNOTING");
						columnObjects15.put("colHeader", "Grievance Officer Remark");
					}
					grievanceHeaderArray.put(columnObjects15);
				}
				//Assigned DC
				if (role == 30 ) {
					JSONObject columnObjects18 = new JSONObject();
					columnObjects18.put("colName", "FULLNAME");
					columnObjects18.put("colHeader", "Assigned DC");
					grievanceHeaderArray.put(columnObjects18);
					}
				json.put(returnArray);
				response.put("dataRes", returnArray);
				response.put("cols", headerArray);
				response.put("Grievancecols", grievanceHeaderArray);
				response.put("documents", docArray);
				response.put("media", mediaArray);
				response.put("documentDetails", documentUpload);
			} else {
				loc = cdmoRepo.getCDMOByUserId(Integer.parseInt(resp.getUserName()));
				String sql = "Select TA.INTPROFILEID,TA.VCHAPPLICANTNAME,AP.STMCREATEDON,TA.VCHMOBILENO,BA.INTONLINESERVICEID,BA.INTPROCESSID,"
						+ "HS.VCH_ORG_EMAIL_ID,HS.INTID,HS.VCH_STATE,HS.VCH_ORG_HD_NAME,HS.VCH_EMPANEL_TYPE,BA.INTUPDATEDBY,D.DISTRICTNAME,D.DISTRICTCODE, "
						+ "AP.intPendingAt,"
						+ " AP.INTSENTFROM,HS.VCH_ROHINI_ID,HS.VCH_HOSP_ADDRESS,HS.VCH_CITY_TOWN,HS.VCH_HOSP_PIN,HS.VCH_HOSP_REGDNO, "
						+ " AP.intStageNo, AP.intATAProcessId, AP.tinStatus, AP.dtmStatusDate, AP.tinQueryTo, "
						+ " AP.tinResubmitStatus,AP.dtmApprovalDate, (select NVL( LISTAGG(group_type_name, ',') WITHIN GROUP (ORDER BY group_type_name)"
						+ "  ,'') from group_type GR where gr.type_id=AP.intPendingAt)as pendingAuth "
						+ "FROM T_APPLICANT_PROFILE TA "
						+ "JOIN t_online_service_application BA ON BA.intProfileId = TA.intProfileId AND BA.BITDELETEDFLAG=0 AND "
						+ "TA.BITDELETEDFLAG=0 "
						+ "JOIN t_online_service_approval AP ON AP.INTONLINESERVICEID = BA.INTONLINESERVICEID AND AP.BITDELETEDFLAG = 0 "
						+ "JOIN DYN_BSKY_HOS_B_INFO HS on HS.INTONLINESERVICEID = BA.INTONLINESERVICEID "
						+ "LEFT JOIN DISTRICT D on " + "HS.VCH_DISTRICT = TO_CHAR(D.DISTRICTCODE)  AND "// AND
																										// TO_CHAR(HS.VCH_DISTRICT)
																										// is NOT NULL
						+ "HS.VCH_STATE = D.STATECODE " + "WHERE BA.INTPROCESSID= " + object.getString("itemId");
				
				System.out.println("++++++++++"+sql);

				if (Integer.parseInt(object.getString("serviceId")) > 0) {
					sql = sql + " AND BA.INTONLINESERVICEID= " + object.getString("serviceId");
				}
				if (object.has("pageType")) {

					if (object.has("pendingAt") && object.getInt("pendingAt") != 0
							&& object.getString("pageType").equals("pen")) {
						sql = sql + " AND AP.intPendingAt='" + object.getInt("pendingAt") + "'";
					}

					else if (object.has("pendingAt") && object.getInt("pendingAt") == 0
							&& object.getString("pageType").equals("sum")) {
						sql = sql + " AND AP.tinStatus IN (1,2,8,7,3)" + " AND AP.INTATAPROCESSID !=0";
					} else if (object.has("pendingAt") && object.getInt("pendingAt") != 0
							&& object.getString("pageType").equals("rev")) {
						sql = sql + " AND AP.tinStatus= " + 3;
					} else if (object.has("pendingAt") && object.getInt("pendingAt") != 0
							&& object.getString("pageType").equals("app")) {
						sql = sql + " AND AP.tinStatus= " + 8;
					} else if (object.has("pendingAt") && object.getInt("pendingAt") != 0
							&& object.getString("pageType").equals("rej")) {
						sql = sql + " AND AP.tinStatus= " + 7;
					}
				} else {
					sql = sql + " AND AP.intPendingAt='" + object.getInt("pendingAt") + "'";
				}
				if (userGroupId.equals("12") && object.get("mode").equals("all")) {
					if (!loc.getStateCode().equals("0"))
						sql = sql + " AND HS.VCH_STATE= " + loc.getStateCode() + " AND HS.VCH_DISTRICT= " + "'"
								+ loc.getDistrictCode() + "'" + " AND TO_CHAR(HS.VCH_DISTRICT) is NOT NULL";
				} else if (userGroupId.equals("6") && object.get("mode").equals("all")) {
					sql = sql + " AND HS.VCH_STATE= " + 21;
					String Dcsql1 = "Select unique UD.DISTRICT_CODE from HOSPITAL_INFO UD where " + "UD.ASSIGNED_DC=?"
							+ "AND UD.STATE_CODE=?";

					List<String> data = jdbcTemplate.queryForList(Dcsql1, new Object[] { userName, 21 }, String.class);
					distList = distList + "(";
					cityIterator = data.iterator();
					while (cityIterator.hasNext()) {
						distList = distList + "'" + cityIterator.next() + "',";
					}
					distList = distList + ")";
					distList = new StringBuilder(distList).deleteCharAt(distList.length() - 2).toString();
					sql = sql + " AND HS.VCH_DISTRICT IN " + distList;
				}
				if (object.get("mode").equals("search") && !object.get("stateCode").equals("")
						&& (userGroupId.equals("12") && !object.get("distCode").equals(""))) {

					if (!object.get("stateCode").equals("21")
							&& !object.get("distCode").equals(loc.getDistrictCode())) {
						throw new Exception("State not assigned to user");
					} else
						sql = sql + " AND HS.VCH_STATE= " + 21 + " AND HS.VCH_DISTRICT= " + "'" + loc.getDistrictCode()
								+ "'";
				}

				else if (object.get("mode").equals("search") && !object.get("stateCode").equals("")
						&& userGroupId.equals("6") && !object.get("distCode").equals(" ")) {
					if (!object.get("stateCode").equals("21")) {
						throw new Exception("State not assigned to user");
					}
					String Dcsql1 = "Select unique UD.DISTRICT_CODE from HOSPITAL_INFO UD where " + "UD.ASSIGNED_DC=?"
							+ "AND UD.STATE_CODE=?";

					List<String> data = jdbcTemplate.queryForList(Dcsql1, new Object[] { userName, 21 }, String.class);
					distList = distList + "(";
					cityIterator = data.iterator();
					while (cityIterator.hasNext()) {
						distList = distList + "'" + cityIterator.next() + "',";
					}
					distList = distList + ")";
					distList = new StringBuilder(distList).deleteCharAt(distList.length() - 2).toString();

					sql = sql + " AND HS.VCH_STATE= " + 21 + " AND HS.VCH_DISTRICT IN " + distList;
				} else if (object.get("mode").equals("search") && !object.get("stateCode").equals("")
						&& userGroupId.equals("13") && !object.get("distCode").equals("")) {
					sql = sql + " AND HS.VCH_STATE= " + object.get("stateCode") + " AND HS.VCH_DISTRICT = " + "'"
							+ object.get("distCode") + "'";
				}

				else if (object.get("mode").equals("search") && !object.get("stateCode").equals("")
						&& userGroupId.equals("23") && !object.get("distCode").equals("")) {
					sql = sql + " AND HS.VCH_STATE= " + object.get("stateCode") + " AND HS.VCH_DISTRICT = " + "'"
							+ object.get("distCode") + "'";
				}

				// for only state search

				else if (object.get("mode").equals("search") && !object.get("stateCode").equals("")
						&& userGroupId.equals("23") && object.get("distCode").equals("")) {
					sql = sql + " AND HS.VCH_STATE= '" + object.get("stateCode") + "'";
				}

				else if (object.get("mode").equals("search") && !object.get("stateCode").equals("")
						&& (userGroupId.equals("19") || userGroupId.equals("41") || userGroupId.equals("4"))
						&& !object.get("distCode").equals("")) {
					sql = sql + " AND HS.VCH_STATE= " + object.get("stateCode") + " AND HS.VCH_DISTRICT = " + "'"
							+ object.get("distCode") + "'";
				}

				if (object.get("mode").equals("search") && object.get("lstAct").equals("CDMO")) {
					if ((resp.getGroupType().equals("13")) || (resp.getGroupType().equals("4"))
							|| (resp.getGroupType().equals("23"))) {
						sql = sql + " AND AP.INTSENTFROM = 12";
					}

					if (resp.getGroupType().equals("19") || resp.getGroupType().equals("41")) {
						sql = sql + " AND AP.INTSENTFROM = 12";
					} else if (loc.getDistrictCode() != null) {
						sql = sql + " AND AP.INTSENTFROM= " + 12 + " AND HS.VCH_STATE= " + 21 + " AND HS.VCH_DISTRICT= "
								+ "'" + loc.getDistrictCode() + "'";
					} else if (resp.getGroupType().equals("6") && object.get("mode").equals("search")
							&& object.get("lstAct").equals("CDMO")) {
						String Dcsql1 = "Select unique UD.DISTRICT_CODE from HOSPITAL_INFO UD where "
								+ "UD.ASSIGNED_DC=?" + "AND UD.STATE_CODE=?";
						List<String> data = jdbcTemplate.queryForList(Dcsql1, new Object[] { userName, 21 },
								String.class);
						distList = distList + "(";
						cityIterator = data.iterator();
						while (cityIterator.hasNext()) {
							distList = distList + "'" + cityIterator.next() + "',";
						}
						distList = distList + ")";
						distList = new StringBuilder(distList).deleteCharAt(distList.length() - 2).toString();

						sql = sql + " AND AP.INTSENTFROM= " + 12;
					}
				} else if (object.get("mode").equals("search") && object.get("lstAct").equals("DC")) {
					if (loc.getDistrictCode() != null) {
						sql = sql + " AND AP.INTSENTFROM= " + 6 + " AND HS.VCH_STATE= " + 21 + " AND HS.VCH_DISTRICT= "
								+ "'" + loc.getDistrictCode() + "'";
					}
					if (resp.getGroupType().equals("19") || resp.getGroupType().equals("41")) {
						sql = sql + " AND AP.INTSENTFROM = 6";
					} else if (((resp.getGroupType().equals("13")) || (resp.getGroupType().equals("4"))
							|| (resp.getGroupType().equals("23")))) {
						sql = sql + " AND AP.INTSENTFROM = 6";
					} else if (resp.getGroupType().equals("6") && object.get("mode").equals("search")
							&& object.get("lstAct").equals("DC")) {
						String Dcsql1 = "Select unique UD.DISTRICT_CODE from HOSPITAL_INFO UD where "
								+ "UD.ASSIGNED_DC=?" + "AND UD.STATE_CODE=?";

						List<String> data = jdbcTemplate.queryForList(Dcsql1, new Object[] { userName, 21 },
								String.class);

						distList = distList + "(";
						cityIterator = data.iterator();
						while (cityIterator.hasNext()) {
							distList = distList + "'" + cityIterator.next() + "',";
						}
						distList = distList + ")";
						distList = new StringBuilder(distList).deleteCharAt(distList.length() - 2).toString();
						sql = sql + " AND AP.INTSENTFROM= " + 6;
					}
				} else if (object.get("mode").equals("search") && object.get("lstAct").equals("SHAS")) {
					if (((resp.getGroupType().equals("13")) || (resp.getGroupType().equals("4"))
							|| (resp.getGroupType().equals("23")))) {
						sql = sql + " AND AP.INTSENTFROM= " + 13;
					}
					if (resp.getGroupType().equals("19") || resp.getGroupType().equals("41")) {
						sql = sql + " AND AP.INTSENTFROM = 13";
					} else if (loc.getDistrictCode() != null) {
						sql = sql + " AND AP.INTSENTFROM= " + 13 + " AND HS.VCH_STATE= " + 21 + " AND HS.VCH_DISTRICT= "
								+ "'" + loc.getDistrictCode() + "'";
					} else if (resp.getGroupType().equals("6") && object.get("mode").equals("search")
							&& object.get("lstAct").equals("SHAS")) {
						String Dcsql1 = "Select unique UD.DISTRICT_CODE from HOSPITAL_INFO UD where "
								+ "UD.ASSIGNED_DC=?" + "AND UD.STATE_CODE=?";

						List<String> data = jdbcTemplate.queryForList(Dcsql1, new Object[] { userName, 21 },
								String.class);
						distList = distList + "(";
						cityIterator = data.iterator();
						while (cityIterator.hasNext()) {
							distList = distList + "'" + cityIterator.next() + "',";
						}
						distList = distList + ")";
						distList = new StringBuilder(distList).deleteCharAt(distList.length() - 2).toString();
						sql = sql + " AND AP.INTSENTFROM= " + 13;
					}
				} else if (object.get("mode").equals("search") && object.get("lstAct").equals("Hospital")) {
					if (((resp.getGroupType().equals("13")) || (resp.getGroupType().equals("4"))
							|| (resp.getGroupType().equals("23"))) && object.get("mode").equals("search")
							&& object.get("lstAct").equals("Hospital")) {
						sql = sql + " AND AP.INTSENTFROM  NOT IN (12,6,13,4,19)  ";
					}
					if (resp.getGroupType().equals("19")) {
						sql = sql + " AND AP.INTSENTFROM  NOT IN (12,6,13,4) ";
					} else if (loc.getDistrictCode() != null) {
						sql = sql + " AND HS.VCH_STATE= " + 21 + " AND HS.VCH_DISTRICT= " + "'" + loc.getDistrictCode()
								+ "'" + "  AND AP.INTSENTFROM  NOT IN (12,6,13)";
					} else if (resp.getGroupType().equals("6") && object.get("mode").equals("search")
							&& object.get("lstAct").equals("Hospital")) {
						String Dcsql1 = "Select unique UD.DISTRICT_CODE from HOSPITAL_INFO UD where "
								+ "UD.ASSIGNED_DC=?" + "AND UD.STATE_CODE=?";

						List<String> data = jdbcTemplate.queryForList(Dcsql1, new Object[] { userName, 21 },
								String.class);
						distList = distList + "(";
						cityIterator = data.iterator();
						while (cityIterator.hasNext()) {
							distList = distList + "'" + cityIterator.next() + "',";
						}
						distList = distList + ")";
						distList = new StringBuilder(distList).deleteCharAt(distList.length() - 2).toString();
						sql = sql + "  AND AP.INTSENTFROM  NOT IN (12,6,13,4)";
					}
				} else if ((resp.getGroupType().equals("13") || resp.getGroupType().equals("19")
						|| resp.getGroupType().equals("41") || (resp.getGroupType().equals("23")))
						&& object.get("mode").equals("search") && object.get("lstAct").equals("SNA")) {
					sql = sql + " AND AP.INTSENTFROM = 4";
				}
				// new condition for shasceo(last action) wise search
				else if ((resp.getGroupType().equals("23")) && object.get("mode").equals("search")
						&& object.get("lstAct").equals("SHASCEO")) {
					sql = sql + " AND AP.INTSENTFROM = 19";
				}

				// new condition for pending at search(IN QCAdmin report of SHASQC)

				if ((resp.getGroupType().equals("23")) && object.get("mode").equals("search")
						&& !object.get("pendingApplication").equals("")) {
					if (object.getString("pendingApplication").equals("CDMO")) {
						sql = sql + "AND AP.intPendingAt= " + 12;
					}
					if (object.getString("pendingApplication").equals("DC")) {
						sql = sql + "AND AP.intPendingAt= " + 6;
					}
					if (object.getString("pendingApplication").equals("SHAS")) {
						sql = sql + "AND AP.intPendingAt= " + 13;
					}
					if (object.getString("pendingApplication").equals("SNA")) {
						sql = sql + "AND AP.intPendingAt= " + 4;
					}
					if (object.getString("pendingApplication").equals("SHASCEO")) {
						sql = sql + "AND AP.intPendingAt= " + 19;
					}
				}

				else
					sql = sql + " ORDER BY  AP.STMCREATEDON DESC";
				hibernateSession = commonSessionFactory.openSession();
				Query query = hibernateSession.createSQLQuery(sql);
				query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				List<Map<String, Object>> aliasToValueMapList = query.list();
				String date = null;
				JSONArray json = new JSONArray();
				for (Map<String, Object> map : aliasToValueMapList) {
					JSONObject tableObject = new JSONObject();
					Integer serviceId = 0;
					Integer intId = 0;
					for (Map.Entry<String, Object> entry : map.entrySet()) {

						String key = entry.getKey();
						Object value = entry.getValue();
						if (key.equals("STMCREATEDON")) {
							date = value.toString();
							String[] dataFormat = date.split("[.]");
							SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
							java.util.Date date1 = sm.parse(dataFormat[0]);
							SimpleDateFormat sm1 = new SimpleDateFormat("dd-MMM-yyyy");
							String dateFormater = sm1.format(date1);
							tableObject.put(key, dateFormater);
						} else if (key.equals("INTUPDATEDBY")) {
							try {
								String roleName = tSetWorkFlowRepository
										.getRoleName(Integer.parseInt(value.toString()));
								tableObject.put(key, roleName);
							} catch (Exception e) {
								logger.error(ExceptionUtils.getStackTrace(e));
								tableObject.put(key, "Hospital");
							}

						} else if (key.equals("INTONLINESERVICEID")) {
							serviceId = Integer.parseInt(value.toString());
							tableObject.put(key, value);
						} else if (key.equals("INTID")) {
							intId = Integer.parseInt(value.toString());
						} else if (key.equals("VCH_STATE")) {
							try {
								String tableName = "dyn_bsky_hos_b_info";
								Clob getFilterData = processRepository.filterDataById(intId, serviceId, tableName);
								JSONObject jsonObj = new JSONObject(
										ConvertClobToJson.convertClobToJSONString(getFilterData));
								tableObject.put(key, jsonObj.get("VCH_STATE"));
							} catch (Exception e) {
								logger.error(ExceptionUtils.getStackTrace(e));
								tableObject.put(key, "---");
							}
						} else {
							tableObject.put(key, value);
						}

					}
					returnArray.put(tableObject);
				}
				headerArray = new JSONArray();
				JSONObject columnObject8 = new JSONObject();
				columnObject8.put("colName", "VCHAPPLICANTNAME");
				columnObject8.put("colHeader", "Hospital Name");
				headerArray.put(columnObject8);
				JSONObject columnObject4 = new JSONObject();
				columnObject4.put("colName", "VCH_HOSP_REGDNO");
				columnObject4.put("colHeader", "Registration Number");
				headerArray.put(columnObject4);
//				JSONObject columnObject2 = new JSONObject();
//				columnObject2.put("colName", "VCH_ROHINI_ID");
//				columnObject2.put("colHeader", " Rohini ID");
//				headerArray.put(columnObject2);
				JSONObject columnObject6 = new JSONObject();
				columnObject6.put("colName", "VCH_ORG_HD_NAME");
				columnObject6.put("colHeader", "Head Name");
				headerArray.put(columnObject6);
				JSONObject columnObject9 = new JSONObject();
				columnObject9.put("colName", "VCHMOBILENO");
				columnObject9.put("colHeader", "Mobile Number");
				headerArray.put(columnObject9);
				JSONObject columnObject3 = new JSONObject();
				columnObject3.put("colName", "DISTRICTNAME");
				columnObject3.put("colHeader", "District");
				headerArray.put(columnObject3);
				JSONObject columnObject7 = new JSONObject();
				columnObject7.put("colName", "VCH_STATE");
				columnObject7.put("colHeader", "State");
				headerArray.put(columnObject7);
				JSONObject columnObject = new JSONObject();
				columnObject.put("colName", "STMCREATEDON");
				columnObject.put("colHeader", "Applied Date");
				headerArray.put(columnObject);

				JSONObject columnObject10 = new JSONObject();
				columnObject10.put("colName", "INTUPDATEDBY");
				columnObject10.put("colHeader", "Applied/Updated By");
				headerArray.put(columnObject10);

				json.put(returnArray);
				response.put("dataRes", returnArray);
				response.put("cols", headerArray);
				if (loc != null && loc.getDistrictCode() != null) {
					String statecd = loc.getStateCode();
					String districtcd = loc.getDistrictCode();
					String districtname = districtMasterRepository.getDistrictName(statecd, districtcd);

					response.put("cdmoDistrict", loc.getDistrictCode());
					response.put("cdmoDistrictName", districtname);

				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			if (hibernateSession != null)
				hibernateSession.close();
		}
		return response;
	}

	@SuppressWarnings("unlikely-arg-type")
	public List<String> fnGetAuthority(Integer processId, Integer stageNo, Integer projectId, Integer roleId,
			List<String> myList) {
		List<String> flow = new ArrayList<>();
		String FORWARD_TO_AUTH = "0";
		String NEXT_FORWARD_TO = "";
		boolean flag = false;
		String authList = "";
		String nextForwardAuthority = "";
		String nextFlowDetails = "";
		String forwardAuthority = "";
		String flowDetails = "";
		try {
			// //System.out.println("set auth calllled------");
			if (processId != 583) {
				if (state_id.equals("21")) {
					projectId = 1;
				} else {
					projectId = 2;
				}
			} else
				projectId = 1;
			flowDetails = tSetAuthorityRepository.getintATAProcessId(processId, stageNo, projectId);

			forwardAuthority = tSetAuthorityRepository.getIntProcessIdAndTinStageNoAndIntProjectId(processId, stageNo,
					projectId);

			// //System.out.println("forward auth:" + forwardAuthority);

			String[] roleIds = forwardAuthority.split(",");

			if (Integer.valueOf(flowDetails) == 1 && myList.size() > 0) {

				for (int i = 0; i < roleIds.length; i++) {

					if (roleId != Integer.valueOf(roleIds[i])) {
						authList = authList + roleIds[i];
						FORWARD_TO_AUTH = String.valueOf(roleId);
					}

				}

				nextForwardAuthority = authList;

				nextFlowDetails = flowDetails;

			} else {
				// //System.out.println("projid-2" + projectId);
				nextForwardAuthority = tSetAuthorityRepository.getIntProcessIdAndTinStageNoAndIntProjectId(processId,
						stageNo + 1, projectId);

				nextFlowDetails = tSetAuthorityRepository.getintATAProcessId(processId, stageNo + 1, projectId);

			}

			if (nextForwardAuthority != null) {

				NEXT_FORWARD_TO = nextForwardAuthority;

			} else {

				NEXT_FORWARD_TO = "0";

			}

			String authDetails = FORWARD_TO_AUTH + '-' + NEXT_FORWARD_TO;

			String flowType = flowDetails;

			String nextFlowType = "0";

			if (nextFlowDetails != null) {
				nextFlowType = nextFlowDetails;
			}

			flow.add(authDetails);
			flow.add(flowType);
			flow.add(nextFlowType);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return flow;
	}

	@Transactional
	@Override
	public String takeAction(JSONObject object) throws JSONException {
		int stageNo = 0;
		long currentTime = System.currentTimeMillis();
		JSONObject response = new JSONObject();
		int flag = 0;
		try {
			if (object.getInt("processId") != 583) {
				state_id = getStateId((long) object.getInt("serviceId"));
			}
			OnlineServiceApproval approval = onlineServiceApprovalRepository
					.findByIntProcessIdAndIntOnlineServiceIdAndIntStageNoAndBitDeletedFlag(object.getInt("processId"),
							object.getInt("serviceId"), object.getInt("stageNo"), 0);
			if (Integer.valueOf(object.getInt("action")) == 1) { // Markup
				String[] pendingList = approval.getIntPendingAt().split(",");
				List<String> myList = new ArrayList<>(Arrays.asList(pendingList));
				myList.remove(object.get("updatedByRoleId"));

				List<String> authorityArr = fnGetAuthority(object.getInt("processId"), object.getInt("stageNo"),
						object.getInt("serviceId"), Integer.valueOf(object.getInt("updatedByRoleId")), myList);

				String authorityDetails = "";
				String flowType = "0";
				String nextFlowType = "0";

				if (authorityArr.size() > 0) {

					authorityDetails = authorityArr.get(0);
					flowType = authorityArr.get(1);
					nextFlowType = authorityArr.get(2);
				}
				if (myList.size() == 0) {

					stageNo = object.getInt("stageNo") + 1;
				} else {
					stageNo = object.getInt("stageNo");
				}

				String[] authorityDetailsArr = authorityDetails.split("-");
				String nextAuthority = authorityDetailsArr[1];

				OnlineServiceApprovalNotings notings = new OnlineServiceApprovalNotings();
				notings.setINTPROCESSID(Integer.valueOf(object.getInt("processId")));
				notings.setINTPROFILEID(0);
				notings.setINTONLINESERVICEID(Integer.valueOf(object.getInt("serviceId")));
				notings.setINTFROMAUTHORITYID(Integer.valueOf(object.getInt("updatedByRoleId")));
				notings.setBITDELETEDFLAG(0);
				notings.setINTTOAUTHORITY(nextAuthority);
				notings.setDTACTIONTAKEN(new Date(currentTime));
				notings.setINTSTATUS(1); // Integer.valueOf(object.getInt("action"))
				notings.setTINSTAGECTR(stageNo);
				notings.setTXTNOTINGOriginal(object.getString("remark"));
				notings.setINTCREATEDBY(object.getInt("updatedBy"));
				if (object.has("priority")) {
					notings.setPriority(object.getString("priority"));
				}
				if (object.has("multipart")) {
					notings.setJSNOTHERDETAILSOriginal(object.getString("multipart"));
				}
				if (Integer.valueOf(object.getInt("processId")) == 142) {
					if (object.has("multipart2")) {
						notings.setJSNOTHERDETAILSOriginal(object.getString("multipart2"));
					}
				} else {
					if (object.has("multipart2")) {
						notings.setJSNOMediaDETAILSOriginal(object.getString("multipart2"));
					}
				}
				onlineServiceApprovalNotingsRepository.saveAndFlush(notings);

				approval.setIntStageNo(stageNo);
				approval.setIntPendingAt(nextAuthority);
				approval.setIntATAProcessId(Integer.valueOf(nextFlowType));
				approval.setIntSentFrom(Integer.valueOf(object.getInt("updatedByRoleId")));
				approval.setTinStatus(1);
				approval.setIntForwardTo(Integer.valueOf(nextAuthority));
				approval.setIntUpdatedBy(object.getInt("updatedBy"));
				String pattern = "MM-dd-yyyy";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String date = simpleDateFormat.format(new Date(currentTime));
				Long millis = System.currentTimeMillis();
				approval.setDtmStatusDate(new Date((millis)));
				if (object.has("multipart")) {
					notings.setJSNOTHERDETAILS(object.getString("multipart"));
				}

				if (Integer.valueOf(object.getInt("processId")) == 142) {
					if (object.has("multipart2")) {
						notings.setJSNOTHERDETAILSOriginal(object.getString("multipart2"));
					}
				} else {
					if (object.has("multipart2")) {
						notings.setJSNOMediaDETAILSOriginal(object.getString("multipart2"));
					}
				}

				onlineServiceApprovalRepository.saveAndFlush(approval);

				response.put("status", "200");
				response.put("msg", "Success");

				return response.toString();

			} else if ((Integer.valueOf(object.getInt("action")) == 2)) { // MarkDown
				Integer label_id = 0;
				state_id = getStateId((long) object.getInt("serviceId"));
				if (object.getInt("processId") != 583) {
					if (state_id.equals("21")) {
						label_id = 1;
					} else {
						label_id = 2;
					}
				} else
					label_id = 1;
				String flowDetails = tSetAuthorityRepository.getintATAProcessId(object.getInt("processId"),
						object.getInt("stageNo") - 1, label_id);

				String forwardAuthority = tSetAuthorityRepository.getIntProcessIdAndTinStageNoAndIntProjectId(
						object.getInt("processId"), object.getInt("stageNo") - 1, label_id);
				OnlineServiceApprovalNotings notings = new OnlineServiceApprovalNotings();
				notings.setINTPROCESSID(Integer.valueOf(object.getInt("processId")));
				notings.setINTPROFILEID(0);
				notings.setINTONLINESERVICEID(Integer.valueOf(object.getInt("serviceId"))); // updatedByRoleId
				notings.setINTFROMAUTHORITYID(Integer.valueOf(object.getInt("updatedByRoleId")));
				notings.setBITDELETEDFLAG(0); //
				notings.setINTTOAUTHORITY(forwardAuthority); // statusDate
				notings.setDTACTIONTAKEN(new Date(currentTime));
				notings.setINTSTATUS(2); // Integer.valueOf(object.getInt("action"))
				notings.setTINSTAGECTR(object.getInt("stageNo") - 1);
				notings.setTXTNOTINGOriginal(object.getString("remark"));
				notings.setINTCREATEDBY(object.getInt("updatedBy"));
				if (object.has("multipart")) {
					notings.setJSNOTHERDETAILSOriginal(object.getString("multipart"));
				}
				onlineServiceApprovalNotingsRepository.saveAndFlush(notings);

				OnlineServiceApproval approval1 = onlineServiceApprovalRepository
						.findByIntProcessIdAndIntOnlineServiceIdAndIntStageNoAndBitDeletedFlag(
								Integer.valueOf(object.getInt("processId")),
								Integer.valueOf(object.getInt("serviceId")), object.getInt("stageNo"), 0);

				approval1.setIntStageNo(object.getInt("stageNo") - 1);
				approval1.setIntPendingAt(forwardAuthority);
				approval1.setIntATAProcessId(2);
				approval1.setIntSentFrom(Integer.valueOf(object.getInt("updatedByRoleId")));
				approval1.setTinStatus(2);
				approval1.setDtmStatusDate(new Date(currentTime));
				approval1.setIntUpdatedBy(object.getInt("updatedBy"));
				if (object.has("multipart")) {
					notings.setJSNOTHERDETAILSOriginal(object.getString("multipart"));
				}
				onlineServiceApprovalRepository.saveAndFlush(approval1);

				response.put("status", "200");
				response.put("msg", "Success");

			} else if ((Integer.valueOf(object.getInt("action")) == 18)) { // Re-open
				Integer label_id = 0;
				state_id = getStateId((long) object.getInt("serviceId"));
				if (object.getInt("processId") != 583) {
					if (state_id.equals("21")) {
						label_id = 1;
					} else {
						label_id = 2;
					}
				} else
					label_id = 1;
				String flowDetails = tSetAuthorityRepository.getintATAProcessId(object.getInt("processId"),
						object.getInt("stageNo") - 1, label_id);

				String forwardAuthority = tSetAuthorityRepository.getIntProcessIdAndTinStageNoAndIntProjectId(
						object.getInt("processId"), object.getInt("stageNo") - 1, label_id);
				OnlineServiceApprovalNotings notings = new OnlineServiceApprovalNotings();
				notings.setINTPROCESSID(Integer.valueOf(object.getInt("processId")));
				notings.setINTPROFILEID(0);
				notings.setINTONLINESERVICEID(Integer.valueOf(object.getInt("serviceId"))); // updatedByRoleId
				notings.setINTFROMAUTHORITYID(Integer.valueOf(object.getInt("updatedByRoleId")));
				notings.setBITDELETEDFLAG(0); //
				notings.setINTTOAUTHORITY(forwardAuthority); // statusDate
				notings.setDTACTIONTAKEN(new Date(currentTime));
				notings.setINTSTATUS(18); // Integer.valueOf(object.getInt("action"))
				notings.setTINSTAGECTR(object.getInt("stageNo") - 1);
				notings.setTXTNOTINGOriginal(object.getString("remark"));
				notings.setINTCREATEDBY(object.getInt("updatedBy"));
				if (object.has("multipart")) {
					notings.setJSNOTHERDETAILS(object.getString("multipart"));
				}
				onlineServiceApprovalNotingsRepository.saveAndFlush(notings);

				OnlineServiceApproval approval1 = onlineServiceApprovalRepository
						.findByIntProcessIdAndIntOnlineServiceIdAndIntStageNoAndBitDeletedFlag(
								Integer.valueOf(object.getInt("processId")),
								Integer.valueOf(object.getInt("serviceId")), object.getInt("stageNo"), 0);

				approval1.setIntStageNo(object.getInt("stageNo") - 1);
				approval1.setIntPendingAt(forwardAuthority);
				approval1.setIntATAProcessId(2);
				approval1.setIntSentFrom(Integer.valueOf(object.getInt("updatedByRoleId")));
				approval1.setTinStatus(18);
				approval1.setDtmStatusDate(new Date(currentTime));
				approval1.setIntUpdatedBy(object.getInt("updatedBy"));
				onlineServiceApprovalRepository.saveAndFlush(approval1);

				response.put("status", "200");
				response.put("msg", "Success");

			} else if (Integer.valueOf(object.getInt("action")) == 8) { // Approved

				List<String> myList = new ArrayList<>(Arrays.asList(approval.getIntPendingAt()));
				myList.remove(object.get("updatedByRoleId"));

				List<String> authorityArr = fnGetAuthority(object.getInt("processId"), object.getInt("stageNo"), 0,
						Integer.valueOf(object.getInt("updatedByRoleId")), myList);

				String authorityDetails = "";
				String flowType = "0";
				String nextFlowType = "0";

				if (!authorityArr.isEmpty()) {

					authorityDetails = authorityArr.get(0);
					flowType = authorityArr.get(1);
					nextFlowType = authorityArr.get(2);
				}

				String[] authorityDetailsArr = authorityDetails.split("-");
				String nextAuthority = authorityDetailsArr[1];

				OnlineServiceApprovalNotings notings = new OnlineServiceApprovalNotings();
				notings.setINTPROCESSID(Integer.valueOf(object.getInt("processId")));
				notings.setINTPROFILEID(0);
				notings.setINTONLINESERVICEID(Integer.valueOf(object.getInt("serviceId"))); // updatedByRoleId
				notings.setINTFROMAUTHORITYID(Integer.valueOf(object.getInt("updatedByRoleId")));
				notings.setBITDELETEDFLAG(0); //
				notings.setINTTOAUTHORITY(nextAuthority); // statusDate
				notings.setDTACTIONTAKEN(new Date(currentTime));
				notings.setINTSTATUS(8); // Integer.valueOf(object.getInt("action"))

				notings.setTXTNOTINGOriginal(object.getString("remark"));
				notings.setINTCREATEDBY(object.getInt("updatedBy"));//
				onlineServiceApprovalNotingsRepository.saveAndFlush(notings);
				OnlineServiceApproval approval2 = onlineServiceApprovalRepository
						.findByIntProcessIdAndIntOnlineServiceIdAndBitDeletedFlag(
								Integer.valueOf(object.getInt("processId")),
								Integer.valueOf(object.getInt("serviceId")), 0);

				approval2.setIntPendingAt(nextAuthority);
				approval2.setIntATAProcessId(Integer.valueOf(nextFlowType));
				approval2.setIntSentFrom(Integer.valueOf(object.getInt("updatedByRoleId")));
				approval2.setTinStatus(8);
				approval2.setIntForwardTo(0);
				approval2.setDtmStatusDate(new Date(currentTime));
				onlineServiceApprovalRepository.saveAndFlush(approval2);

				if (object.getInt("processId") == 583) {
					if (object.has("contactNumber")) {
						String mobileNumber = object.getString("contactNumber");
						sendOTP(mobileNumber, "Approved");
					}
				}
				response.put("status", "200");
				response.put("msg", "Success");

			} else if (Integer.valueOf(object.getInt("action")) == 19) { // Grievance Approved

				List<String> myList = new ArrayList<>(Arrays.asList(approval.getIntPendingAt()));
				myList.remove(object.get("updatedByRoleId"));

				List<String> authorityArr = fnGetAuthority(object.getInt("processId"), object.getInt("stageNo"), 0,
						Integer.valueOf(object.getInt("updatedByRoleId")), myList);

				String authorityDetails = "";
				String flowType = "0";
				String nextFlowType = "0";

				if (!authorityArr.isEmpty()) {

					authorityDetails = authorityArr.get(0);
					flowType = authorityArr.get(1);
					nextFlowType = authorityArr.get(2);
				}

				String[] authorityDetailsArr = authorityDetails.split("-");
				String nextAuthority = authorityDetailsArr[1];

				OnlineServiceApprovalNotings notings = new OnlineServiceApprovalNotings();
				notings.setINTPROCESSID(Integer.valueOf(object.getInt("processId")));
				notings.setINTPROFILEID(0);
				notings.setINTONLINESERVICEID(Integer.valueOf(object.getInt("serviceId"))); // updatedByRoleId
				notings.setINTFROMAUTHORITYID(Integer.valueOf(object.getInt("updatedByRoleId")));
				notings.setBITDELETEDFLAG(0); //
				notings.setINTTOAUTHORITY(nextAuthority); // statusDate
				notings.setDTACTIONTAKEN(new Date(currentTime));
				notings.setINTSTATUS(19); // Integer.valueOf(object.getInt("action"))

				notings.setTXTNOTINGOriginal(object.getString("remark"));
				notings.setINTCREATEDBY(object.getInt("updatedBy"));//
				onlineServiceApprovalNotingsRepository.saveAndFlush(notings);
				OnlineServiceApproval approval2 = onlineServiceApprovalRepository
						.findByIntProcessIdAndIntOnlineServiceIdAndBitDeletedFlag(
								Integer.valueOf(object.getInt("processId")),
								Integer.valueOf(object.getInt("serviceId")), 0);

				approval2.setIntPendingAt(nextAuthority);
				approval2.setIntATAProcessId(Integer.valueOf(nextFlowType));
				approval2.setIntSentFrom(Integer.valueOf(object.getInt("updatedByRoleId")));
				approval2.setTinStatus(19);
				approval2.setIntForwardTo(0);
				approval2.setDtmStatusDate(new Date(currentTime));
				onlineServiceApprovalRepository.saveAndFlush(approval2);
				if (object.getInt("processId") == 583) {
					if (object.has("contactNumber")) {
						String mobileNumber = object.getString("contactNumber");
						sendOTP(mobileNumber, "Grievance Disposal");
					}
				}
				response.put("status", "200");
				response.put("msg", "Success");

			} else if (Integer.valueOf(object.getInt("action")) == 7) { // Rejected

				List<String> myList = new ArrayList<>(Arrays.asList(approval.getIntPendingAt()));
				myList.remove(object.get("updatedByRoleId"));

				List<String> authorityArr = fnGetAuthority(object.getInt("processId"), object.getInt("stageNo"), 0,
						Integer.valueOf(object.getInt("updatedByRoleId")), myList);

				String authorityDetails = "";
				String flowType = "0";
				String nextFlowType = "0";

				if (!authorityArr.isEmpty()) {

					authorityDetails = authorityArr.get(0);
					flowType = authorityArr.get(1);
					nextFlowType = authorityArr.get(2);
				}

				String[] authorityDetailsArr = authorityDetails.split("-");
				String nextAuthority = authorityDetailsArr[1];

				OnlineServiceApprovalNotings notings = new OnlineServiceApprovalNotings();
				notings.setINTPROCESSID(Integer.valueOf(object.getInt("processId")));
				notings.setINTPROFILEID(0);
				notings.setINTONLINESERVICEID(Integer.valueOf(object.getInt("serviceId"))); // updatedByRoleId
				notings.setINTFROMAUTHORITYID(Integer.valueOf(object.getInt("updatedByRoleId")));
				notings.setBITDELETEDFLAG(0); //
				notings.setINTTOAUTHORITY("0"); // statusDate
				notings.setDTACTIONTAKEN(new Date(currentTime));
				notings.setINTSTATUS(7); // Integer.valueOf(object.getInt("action"))

				notings.setTXTNOTINGOriginal(object.getString("remark"));
				notings.setINTCREATEDBY(object.getInt("updatedBy"));// ;
				onlineServiceApprovalNotingsRepository.saveAndFlush(notings);
				OnlineServiceApproval approval2 = onlineServiceApprovalRepository
						.findByIntProcessIdAndIntOnlineServiceIdAndBitDeletedFlag(
								Integer.valueOf(object.getInt("processId")),
								Integer.valueOf(object.getInt("serviceId")), 0);

				approval2.setIntPendingAt("0");
				approval2.setIntATAProcessId(Integer.valueOf(0));
				approval2.setIntSentFrom(Integer.valueOf(object.getInt("updatedByRoleId")));
				approval2.setTinStatus(7);
				approval2.setIntForwardTo(0);
				approval2.setDtmStatusDate(new Date(currentTime));
				onlineServiceApprovalRepository.saveAndFlush(approval2);
				if (object.getInt("processId") == 583) {
					if (object.has("contactNumber")) {
						String mobileNumber = object.getString("contactNumber");
						sendOTP(mobileNumber, "Reject");
					}
				}
				response.put("status", "200");
				response.put("msg", "Success");

			} else if (Integer.valueOf(object.getInt("action")) == 6) { // Query
				if (object.getInt("processId") == 583) {
					Integer label_id = 0;
					state_id = getStateId((long) object.getInt("serviceId"));
					if (object.getInt("processId") != 583) {
						if (state_id.equals("21")) {
							label_id = 1;
						} else {
							label_id = 2;
						}
					} else
						label_id = 1;
					String flowDetails = tSetAuthorityRepository.getintATAProcessId(object.getInt("processId"),
							object.getInt("stageNo") - 1, label_id);

					String forwardAuthority = tSetAuthorityRepository.getIntProcessIdAndTinStageNoAndIntProjectId(
							object.getInt("processId"), object.getInt("stageNo") - 1, label_id);
					OnlineServiceApprovalNotings notings = new OnlineServiceApprovalNotings();
					notings.setINTPROCESSID(Integer.valueOf(object.getInt("processId")));
					notings.setINTPROFILEID(0);
					notings.setINTONLINESERVICEID(Integer.valueOf(object.getInt("serviceId"))); // updatedByRoleId
					notings.setINTFROMAUTHORITYID(Integer.valueOf(object.getInt("updatedByRoleId")));
					notings.setBITDELETEDFLAG(0); //
					notings.setINTTOAUTHORITY(forwardAuthority); // statusDate
					notings.setDTACTIONTAKEN(new Date(currentTime));
					notings.setINTSTATUS(6); // Integer.valueOf(object.getInt("action"))
					notings.setTINSTAGECTR(object.getInt("stageNo") - 1);
					notings.setTXTNOTINGOriginal(object.getString("remark"));
					notings.setINTCREATEDBY(object.getInt("updatedBy"));
					if (object.has("multipart")) {
						notings.setJSNOTHERDETAILS(object.getString("multipart"));
					}
					if (object.has("priority")) {
						notings.setPriority(object.getString("priority"));
					}
					onlineServiceApprovalNotingsRepository.saveAndFlush(notings);

					OnlineServiceApproval approval1 = onlineServiceApprovalRepository
							.findByIntProcessIdAndIntOnlineServiceIdAndIntStageNoAndBitDeletedFlag(
									Integer.valueOf(object.getInt("processId")),
									Integer.valueOf(object.getInt("serviceId")), object.getInt("stageNo"), 0);

					approval1.setIntStageNo(object.getInt("stageNo") - 1);
					approval1.setIntPendingAt(forwardAuthority);
					approval1.setIntATAProcessId(2);
					approval1.setIntSentFrom(Integer.valueOf(object.getInt("updatedByRoleId")));
					approval1.setTinStatus(6);
					approval1.setDtmStatusDate(new Date(currentTime));
					approval1.setIntUpdatedBy(object.getInt("updatedBy"));
					onlineServiceApprovalRepository.saveAndFlush(approval1);

					response.put("status", "200");
					response.put("msg", "Success");
				} else {
					String[] pendingList = approval.getIntPendingAt().split(",");
					List<String> myList = new ArrayList<>(Arrays.asList(pendingList));
					myList.remove(object.get("updatedByRoleId"));

					List<String> authorityArr = fnGetAuthority(object.getInt("processId"), object.getInt("stageNo"),
							object.getInt("serviceId"), Integer.valueOf(object.getInt("updatedByRoleId")), myList);

					String authorityDetails = "";
					String flowType = "0";
					String nextFlowType = "0";

					if (!authorityArr.isEmpty()) {

						authorityDetails = authorityArr.get(0);
						flowType = authorityArr.get(1);
						nextFlowType = authorityArr.get(2);
					}
					if (myList.isEmpty()) {

						stageNo = object.getInt("stageNo") + 1;
					} else {
						stageNo = object.getInt("stageNo");
					}

					String[] authorityDetailsArr = authorityDetails.split("-");
					String nextAuthority = authorityDetailsArr[1];

					OnlineServiceApprovalNotings notings = new OnlineServiceApprovalNotings();
					notings.setINTPROCESSID(Integer.valueOf(object.getInt("processId")));
					notings.setINTPROFILEID(0);
					notings.setINTONLINESERVICEID(Integer.valueOf(object.getInt("serviceId"))); // updatedByRoleId
					notings.setINTFROMAUTHORITYID(Integer.valueOf(object.getInt("updatedByRoleId")));
					notings.setBITDELETEDFLAG(0); //
					notings.setINTTOAUTHORITY("0"); // statusDate
					notings.setDTACTIONTAKEN(new Date(currentTime));
					notings.setINTSTATUS(6); // Integer.valueOf(object.getInt("action"))

					notings.setTXTNOTINGOriginal(object.getString("remark"));
					notings.setINTCREATEDBY(object.getInt("updatedBy"));//
					onlineServiceApprovalNotingsRepository.saveAndFlush(notings);

					OnlineServiceApproval approval2 = onlineServiceApprovalRepository
							.findByIntProcessIdAndIntOnlineServiceIdAndBitDeletedFlag(
									Integer.valueOf(object.getInt("processId")),
									Integer.valueOf(object.getInt("serviceId")), 0);

					approval2.setIntPendingAt("0");
					approval2.setIntSentFrom(Integer.valueOf(object.getInt("updatedByRoleId")));
					approval2.setTinStatus(6);
					approval2.setIntForwardTo(0);
					approval2.setTinQueryTo(2);
					approval2.setDtmStatusDate(new Date(currentTime));
					onlineServiceApprovalRepository.saveAndFlush(approval2);
				}
			}

			else if (Integer.valueOf(object.getInt("action")) == 3) { // Resubmited

				OnlineServiceApprovalNotings notings = new OnlineServiceApprovalNotings();
				notings.setINTPROCESSID(Integer.valueOf(object.getInt("processId")));
				notings.setINTPROFILEID(0);
				notings.setINTONLINESERVICEID(Integer.valueOf(object.getInt("serviceId"))); // updatedByRoleId
				notings.setINTFROMAUTHORITYID(Integer.valueOf(object.getInt("updatedByRoleId")));
				notings.setBITDELETEDFLAG(0); //
				notings.setDTACTIONTAKEN(new Date(currentTime));
				notings.setINTSTATUS(3); // Integer.valueOf(object.getInt("action"))

				notings.setTXTNOTINGOriginal(object.getString("remark"));
				notings.setINTCREATEDBY(object.getInt("updatedBy"));
				onlineServiceApprovalNotingsRepository.saveAndFlush(notings);

				OnlineServiceApproval approval2 = onlineServiceApprovalRepository
						.findByIntProcessIdAndIntOnlineServiceIdAndBitDeletedFlag(
								Integer.valueOf(object.getInt("processId")),
								Integer.valueOf(object.getInt("serviceId")), 0);

				approval2.setIntSentFrom(Integer.valueOf(object.getInt("updatedByRoleId")));
				approval2.setTinStatus(3);
				approval2.setIntForwardTo(0);
				approval2.setTinStatus(3);
				approval2.setIntPendingAt("0");
				approval2.setDtmStatusDate(new Date(currentTime));
				onlineServiceApprovalRepository.saveAndFlush(approval2);

				response.put("status", "200");
				response.put("msg", "Success");

			} else if ((Integer.valueOf(object.getInt("action")) == 17)) { // Grievance Reject
				Integer label_id = 0;
				state_id = getStateId((long) object.getInt("serviceId"));
				if (object.getInt("processId") != 583) {
					if (state_id.equals("21")) {
						label_id = 1;
					} else {
						label_id = 2;
					}
				} else
					label_id = 1;

				String flowDetails = tSetAuthorityRepository.getintATAProcessId(object.getInt("processId"),
						object.getInt("stageNo"), label_id);
				OnlineServiceApprovalNotings notings = new OnlineServiceApprovalNotings();
				notings.setINTPROCESSID(Integer.valueOf(object.getInt("processId")));
				notings.setINTPROFILEID(0);
				notings.setINTONLINESERVICEID(Integer.valueOf(object.getInt("serviceId"))); // updatedByRoleId
				notings.setINTFROMAUTHORITYID(Integer.valueOf(object.getInt("updatedByRoleId")));
				notings.setBITDELETEDFLAG(0); //
				notings.setINTTOAUTHORITY("99"); // statusDate
				notings.setDTACTIONTAKEN(new Date(currentTime));
				notings.setINTSTATUS(17); // Integer.valueOf(object.getInt("action"))
				notings.setTINSTAGECTR(3);
				notings.setTXTNOTINGOriginal(object.getString("remark"));
				notings.setINTCREATEDBY(object.getInt("updatedBy"));
				if (object.has("multipart")) {
					notings.setJSNOTHERDETAILS(object.getString("multipart"));
				}
				if (object.has("priority")) {
					notings.setPriority(object.getString("priority"));
				}
				onlineServiceApprovalNotingsRepository.saveAndFlush(notings);

				OnlineServiceApproval approval1 = onlineServiceApprovalRepository
						.findByIntProcessIdAndIntOnlineServiceIdAndIntStageNoAndBitDeletedFlag(
								Integer.valueOf(object.getInt("processId")),
								Integer.valueOf(object.getInt("serviceId")), object.getInt("stageNo"), 0);

				approval1.setIntStageNo(3);
				approval1.setIntPendingAt("99");
				approval1.setIntATAProcessId(2);
				approval1.setIntSentFrom(Integer.valueOf(object.getInt("updatedByRoleId")));
				approval1.setTinStatus(17);
				approval1.setDtmStatusDate(new Date(currentTime));
				approval1.setIntUpdatedBy(object.getInt("updatedBy"));
				onlineServiceApprovalRepository.saveAndFlush(approval1);

				response.put("status", "200");
				response.put("msg", "Success");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return response.toString();
	}

	@Override
	public JSONObject getApplicationList(String request) throws Exception {
		JSONObject object = new JSONObject(request);
		List<JSONObject> list = new ArrayList<>();
		JSONObject response = new JSONObject();
		Integer processId = 0;
		Integer profileId = 541;
		JSONArray formDetails = null;
		String mainTableName = "";
		String tableName = "";
		JSONArray headerArray = null;
		JSONArray array = null;
		Session hibernateSession = null;
		JSONArray returnArray = new JSONArray();
		List<String> listList = new ArrayList<String>();
		try {
			String sql = "select TA.INTPROFILEID,TA.VCHAPPLICANTNAME,"
					+ "TA.VCHMOBILENO,BA.INTONLINESERVICEID,BA.INTPROCESSID,(Select count(1) "
					+ "from t_online_service_approval where BA.INTONLINESERVICEID=INTONLINESERVICEID "
					+ "AND TA.INTPROFILEID=INTPROFILEID AND BITDELETEDFLAG=0) as NoOfCount FROM T_APPLICANT_PROFILE TA "
					+ "JOIN t_online_service_application BA ON BA.intProfileId = TA.intProfileId AND BA.BITDELETEDFLAG=0 AND "
					+ "TA.BITDELETEDFLAG=0  AND BA.INTPROCESSID=" + object.getString("itemId");
			sql = (profileId > 0) ? sql + " AND TA.INTPROFILEID = " + profileId : "";

			// //System.out.println(sql);
			List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql);
			hibernateSession = commonSessionFactory.openSession();
			Query query = hibernateSession.createSQLQuery(sql);
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			List<Map<String, Object>> aliasToValueMapList = query.list();
			// //System.out.println(aliasToValueMapList);
			JSONObject tableObject = new JSONObject();
			for (Map<String, Object> map : aliasToValueMapList) {

				for (Map.Entry<String, Object> entry : map.entrySet()) {

					String key = entry.getKey();
					Object value = entry.getValue();
					tableObject.put(key, value);
				}

			}

			JSONArray json = new JSONArray();
			headerArray = new JSONArray();
			JSONObject columnObject1 = new JSONObject();
			columnObject1.put("colName", "VCH_HOSP_PIN");
			columnObject1.put("colHeader", "Hospital Pincode");
			headerArray.put(columnObject1);
			JSONObject columnObject2 = new JSONObject();
			columnObject2.put("colName", "VCH_ROHINI_ID");
			columnObject2.put("colHeader", "Hospital Rohini ID");
			headerArray.put(columnObject2);
			JSONObject columnObject3 = new JSONObject();
			columnObject3.put("colName", "VCH_CITY_TOWN");
			columnObject3.put("colHeader", "City/Town");
			headerArray.put(columnObject3);
			JSONObject columnObject4 = new JSONObject();
			columnObject4.put("colName", "VCH_HOSP_REGDNO");
			columnObject4.put("colHeader", "Hospital Registration Number");
			JSONObject columnObject6 = new JSONObject();
			columnObject6.put("colName", "VCH_ORG_HD_NAME");
			columnObject6.put("colHeader", "Organisation Head Name");
			headerArray.put(columnObject6);
			json.put(tableObject);
			response.put("dataRes", json);
			response.put("cols", headerArray);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			if (hibernateSession != null)
				hibernateSession.close();
		}
		return response;

	}

	@SuppressWarnings("deprecation")
	@Override
	public String getStateId(Long serviceId) {
		String stateId = "";
		try {

			String sql = "Select HS.VCH_STATE from DYN_BSKY_HOS_B_INFO HS where " + "HS.INTONLINESERVICEID=?";

			stateId = (String) jdbcTemplate.queryForObject(sql, new Object[] { serviceId }, String.class);

		} catch (Exception ex) {
			logger.error(ExceptionUtils.getStackTrace(ex));
		}
		return stateId;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getDistByUserName(String username) {
		String dist = "";
		try {

			String sql = "Select UD.DISTRICT_CODE from HOSPITAL_INFO UD where " + "UD.ASSIGNED_DC=?";

			dist = (String) jdbcTemplate.queryForObject(sql, new Object[] { username }, String.class);

		} catch (Exception ex) {
			logger.error(ExceptionUtils.getStackTrace(ex));
		}
		return dist;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<NotingDto> getNotingIds(NotingParameter notingParameter) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		List<NotingDto> notingStoList = new ArrayList<>();
		List<OnlineServiceApprovalNotings> getNotingEntityList = null;
		ArrayList<QueryDocEntity> querydocs = new ArrayList<>();
		ArrayList<QueryDocEntity> querydocsList = new ArrayList<>();
		try {
			getNotingEntityList = onlineServiceApprovalNotingsRepository.getRemark(notingParameter.getProcessId(),
					notingParameter.getServiceId());
			for (OnlineServiceApprovalNotings getNotingEntity : getNotingEntityList) {
				NotingDto notingDto = new NotingDto();

				if (getNotingEntity.getTXTNOTINGOriginal() != null) {
					getNotingEntity.setTXTNOTING(getNotingEntity.getTXTNOTINGOriginal());
					notingDto.setTxtnoting(getNotingEntity.getTXTNOTINGOriginal());
				}
				if (getNotingEntity.getJSNOTHERDETAILSOriginal() != null) {
					getNotingEntity.setJSNOTHERDETAILS(getNotingEntity.getJSNOTHERDETAILSOriginal());
					notingDto.setJsnotherdetails(getNotingEntity.getJSNOTHERDETAILSOriginal());
				}
				if (getNotingEntity.getTXTREVERTREMARKOriginal() != null) {
					InputStream in = getNotingEntity.getTXTREVERTREMARKOriginal().getAsciiStream();
					StringWriter w = new StringWriter();
					IOUtils.copy(in, w);
					String clobAsString = w.toString();
					getNotingEntity.setTXTREVERTREMARK(clobAsString);
					notingDto.setTxtrevertremark(clobAsString);
				}
				if (getNotingEntity.getJSNOMediaDETAILSOriginal() != null) {
					getNotingEntity.setJSNOTHERDETAILSOriginal(getNotingEntity.getJSNOMediaDETAILSOriginal());
					notingDto.setJsnomediadetails(getNotingEntity.getJSNOMediaDETAILSOriginal());
				}
				if (getNotingEntity.getINTFROMAUTHORITYID() != null) {
					getNotingEntity.setINTFROMAUTHORITY(onlineServiceApprovalNotingsRepository
							.getGroupTypeName(getNotingEntity.getINTFROMAUTHORITYID()));
					try {
						if (getNotingEntity.getINTFROMAUTHORITY().equals(null)) {
							notingDto.setIntfromauthority("Applicant");
						} else {
							notingDto.setIntfromauthority(onlineServiceApprovalNotingsRepository
									.getGroupTypeName(getNotingEntity.getINTFROMAUTHORITYID()));
						}
					} catch (Exception e) {
						logger.error(ExceptionUtils.getStackTrace(e));
						notingDto.setIntfromauthority("Applicant");
					}

				}
				SimpleDateFormat sm = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
				notingDto.setDtactiontaken(sm.format(getNotingEntity.getDTACTIONTAKEN()));
				notingDto.setIntnotingsid(getNotingEntity.getINTNOTINGSID());
				notingDto.setIntonlineserviceid(getNotingEntity.getINTONLINESERVICEID());
				notingDto.setIntprocessid(getNotingEntity.getINTPROCESSID());
				notingDto.setIntprofileid(getNotingEntity.getINTPROFILEID());
				notingDto.setIntstatus(getNotingEntity.getINTSTATUS());
				notingDto.setInttoauthority(getNotingEntity.getINTTOAUTHORITY());
				notingDto.setTinresubmitstatus(getNotingEntity.getTINRESUBMITSTATUS());
				notingDto.setTinstagectr(getNotingEntity.getTINSTAGECTR());
				notingDto.setTxtrevertremark(getNotingEntity.getTXTREVERTREMARK());
				notingDto.setPriority(getNotingEntity.getPriority());

				querydocs = docRepo.getQueryDoc(notingParameter.getServiceId(), getNotingEntity.getINTNOTINGSID());
				for (QueryDocEntity getDocs : querydocs) {
					QueryDocEntity docDto = new QueryDocEntity();
					if (getDocs.getDoc() != null) {
						docDto.setDocName(getDocs.getDocName());
						docDto.setDoc(getDocs.getDoc());
					}
					querydocsList.add(docDto);
				}
				notingDto.setQueryDoc(querydocsList);
				notingStoList.add(notingDto);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return notingStoList;

	}

	@Override
	public LoggedUserDetailsBean getUserDetails(LoggedUserDetailsBean bean) {
		resp.setGroupType(bean.getGroupType());
		resp.setUserName(bean.getUserName());
		userGroupId = bean.getGroupType();
		userName = bean.getUserName();
		return resp;

	}

	@Override
	public JSONObject getGrievanceReport(String request) throws Exception {

		JSONObject object = new JSONObject(request);
		JSONObject response = new JSONObject();
		JSONArray headerArray = null;
		Session hibernateSession = null;
		JSONArray returnArray = new JSONArray();
		CDMOConfiguration loc = new CDMOConfiguration();
		Integer role = object.getInt("pendingAt");
		String distList = " ";
		String hosList = " ";
		String date1 = object.getString("formDate");
		String date2 = object.getString("toDate");

		if (object.getString("itemId") != "583") {
			if (resp.getGroupType().equals("12")) {
				loc = cdmoRepo.getCDMOByUserId(Integer.parseInt(userName));
			}
		}

		String distInt = "";
		Iterator<String> cityIterator;

		ResultSet goReq = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GRIEVANCE_GO_REPORT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("p_user_id", Long.parseLong(userName));
			storedProcedure.setParameter("P_FROM_DATE", date1);
			storedProcedure.setParameter("P_TO_DATE", date2);
			storedProcedure.setParameter("P_HOSPITAL_CODE", object.getString("hostCode"));
			storedProcedure.setParameter("P_STATE_CODE", object.getString("stateCode"));
			storedProcedure.setParameter("P_DISTRICT_CODE", object.getString("distCode"));
			storedProcedure.execute();

			goReq = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");

			while (goReq.next()) {
				JSONObject goObj = new JSONObject();
				goObj.put("STATENAME", goReq.getString(1));
				goObj.put("DISTRICTNAME", goReq.getString(2));
				goObj.put("HOSPITALNAME", goReq.getString(3));
				goObj.put("PENDINGATDC", goReq.getString(4));
				goObj.put("PENDINGATDGO", goReq.getString(5));
				goObj.put("PENDINGATGO", goReq.getString(6));
				goObj.put("ACTIONTAKENBYDC", goReq.getString(7));
				goObj.put("ACTIONTAKENBYDGO", goReq.getString(8));
				goObj.put("ACTIONTAKENBYGO", goReq.getString(9));
				goObj.put("TOTALGRIEVANCE", goReq.getString(10));
				returnArray.put(goObj);
			}
			response.put("dataRes", returnArray);
			response.put("cols", headerArray);
			// response.put("Grievancecols", grievanceHeaderArray);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			if (hibernateSession != null)
				hibernateSession.close();
		}
		return response;
	}

	public String sendOTP(String mobileNo, String otp) {
		String result = null;
		String responseString = null;
		String message = "Dear User, Your One Time Password (OTP) for Forgot Password is " + otp
				+ ". Don't share it with anyone. BSKY, Govt. of Odisha";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("https://govtsms.odisha.gov.in/api/api.php");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("action", "singleSMS"));
			nameValuePairs.add(new BasicNameValuePair("department_id", "D006001"));
			nameValuePairs.add(new BasicNameValuePair("template_id", "1007480143063815155"));
			nameValuePairs.add(new BasicNameValuePair("sms_content", message));
			nameValuePairs.add(new BasicNameValuePair("phonenumber", mobileNo));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = bf.readLine()) != null) {
				responseString = line;

			}
			return responseString;
		} catch (UnsupportedEncodingException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (ClientProtocolException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return responseString;

	}

	@Override
	public Response saveCDMOForwardDetails(@RequestBody CDMOForwardBean logBean) {
		Response response = new Response();
		Integer claimsnoInteger = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_grv_cdmo_action")
					.registerStoredProcedureParameter("p_online_serviceid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action_status", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_cdmo_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_priority", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dc_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_created_on", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_online_serviceid", logBean.getServiceId());
			storedProcedureQuery.setParameter("p_action_status", logBean.getAction());
			storedProcedureQuery.setParameter("p_remark", logBean.getRemark());
			storedProcedureQuery.setParameter("p_cdmo_id", logBean.getUpdatedBy());
			storedProcedureQuery.setParameter("p_priority", logBean.getPriority());
			storedProcedureQuery.setParameter("p_dc_userid", logBean.getDcUserId());
			storedProcedureQuery.setParameter("p_created_on", new java.util.Date());
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			if (claimsnoInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Grievance Forwarded Successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error("Exception raised in saveCDMOForwardDetails method of WorkflowServiceImpl:", e);
		}

		return response;
	}

	@Override
	public String getCDMOActionTakenDetails(Integer userId, String fromDate, String toDate) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_grv_cdmo_action_list")
					.registerStoredProcedureParameter("P_USER_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userId);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("APPLICATIONNO", snoDetailsObj.getString(1));
				jsonObject.put("CONTACTNO", snoDetailsObj.getString(2));
				jsonObject.put("INTONLINESERVICEID", snoDetailsObj.getString(3));
				jsonObject.put("STATE", snoDetailsObj.getString(4));
				jsonObject.put("DISTRICT", snoDetailsObj.getString(5));
				jsonObject.put("GRIEVANCE_BY_NAME", snoDetailsObj.getString(6));
				jsonObject.put("GRIEVANCE_MEDIUM_NAME", snoDetailsObj.getString(7));
				jsonObject.put("GRIEVANCE_TYPE", snoDetailsObj.getString(8));
				jsonObject.put("ACTIONON", snoDetailsObj.getString(9));
				jsonObject.put("DCNAME", snoDetailsObj.getString(10));
				jsonArray.put(jsonObject);
			}
			details.put("actionData", jsonArray);
		} catch (Exception e) {
			logger.error("Error in getCDMOActionTakenDetails of WorkflowServiceImpl :", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getCDMOActionTakenDetails of WorkflowServiceImpl :", e2);
			}
		}
		return details.toString();
	}

	@Override
	public String getCCEFeedbackReportDetails(FeedbackCallingReport logBean) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CCE_GRIEVANCE_FEEDBACK_STATUS")
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_STATE_CODE", logBean.getState());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", logBean.getDistrict());
			storedProcedureQuery.setParameter("P_FROM_DATE",
					new SimpleDateFormat("dd-MMM-yyyy").parse(logBean.getFromDate()));
			storedProcedureQuery.setParameter("P_TO_DATE",
					new SimpleDateFormat("dd-MMM-yyyy").parse(logBean.getToDate()));
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(1));
				jsonObject.put("TILL_LAST_MONTH", snoDetailsObj.getLong(2));
				jsonObject.put("DURING_THE_MONTH", snoDetailsObj.getLong(3));
				jsonObject.put("TOTAL", snoDetailsObj.getLong(4));
				jsonObject.put("TILL_LAST_MONTH_RESOLVED", snoDetailsObj.getLong(5));
				jsonObject.put("DURING_THE_MONTH_RESOLVED", snoDetailsObj.getLong(6));
				jsonObject.put("TOTAL_RESOLVED", snoDetailsObj.getLong(7));
				jsonObject.put("PENDING_MORE_THAN_90_DAYS", snoDetailsObj.getLong(8));
				jsonObject.put("PENDING_MORE_THAN_30_DAYS", snoDetailsObj.getLong(9));
				jsonObject.put("PENDING_MORE_THAN_15_DAYS", snoDetailsObj.getLong(10));
				jsonObject.put("PENDINGAT_DC", snoDetailsObj.getLong(11));
				jsonObject.put("PENDINGAT_DGO", snoDetailsObj.getLong(12));
				jsonObject.put("TOTAL_PENDING_DC_DGO", snoDetailsObj.getLong(13));
				jsonArray.put(jsonObject);
			}
			details.put("actionData", jsonArray);
		} catch (Exception e) {
			logger.error("Error in getCCEFeedbackReportDetails of WorkflowServiceImpl :", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getCCEFeedbackReportDetails of WorkflowServiceImpl :", e2);
			}
		}
		return details.toString();
	}
}
