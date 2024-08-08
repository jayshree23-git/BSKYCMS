package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.MskGrivanceBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.WhatsappMessageErrorLog;
import com.project.bsky.model.WhatsappMessageLog;
import com.project.bsky.model.WhatsappTemplate;
import com.project.bsky.repository.WhatsappMessageLogRepository;
import com.project.bsky.repository.WhatsappTemplateRepository;
import com.project.bsky.service.HrApprovalService;
import com.project.bsky.service.MailingService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.JwtUtil;
import com.project.bsky.util.MailUtils;

/**
 * @author santanu.barad
 *
 */
@Service
public class HrApprovalServiceImpl implements HrApprovalService {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Autowired
	private JwtUtil util;

	@Autowired
	private LoginServiceImpl loginService;

	@Autowired
	private WhatsAppServiceImpl whatsAppServiceImpl;

	@Autowired
	private WhatsappTemplateRepository whatsappTemplateRepository;

	@Autowired
	private WhatsappMessageLogRepository whatsappMessageLogRepository;

	@Value("${configKey}")
	private String password;

	@Autowired
	private MailingService mailingService;

	@Override
	public String getFreshApplication(CPDApproveRequestBean requestData) {
		JSONArray freshApp = new JSONArray();
		ResultSet resultSet = null;
		JSONObject resObject = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_REGISTRATION_LIST")
					.registerStoredProcedureParameter("P_FULL_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PHONE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Application_status", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FULL_NAME", requestData.getCpdName());
			storedProcedureQuery.setParameter("P_PHONE", requestData.getMobile());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestData.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestData.getToDate());
			storedProcedureQuery.setParameter("P_Application_status", requestData.getStatus());
			storedProcedureQuery.execute();
			resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (resultSet.next()) {
				resObject = new JSONObject();
				resObject.put("userId", resultSet.getLong(1));
				resObject.put("fullName", resultSet.getString(2));
				resObject.put("phone", resultSet.getString(3));
				resObject.put("email", resultSet.getString(4));
				resObject.put("dob", DateFormat.formatDate(resultSet.getDate(5)));
				resObject.put("dateOfSubmission", DateFormat.formatDate(resultSet.getDate(6)));
				freshApp.put(resObject);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return freshApp.toString();
	}

	@Override
	public String getFreshApplicationDetails(Long userId) {
		JSONArray basicArray = new JSONArray();
		JSONArray qualificationArray = new JSONArray();
		JSONArray experienceArray = new JSONArray();
		JSONArray addressArray = new JSONArray();
		JSONArray specialityArray = new JSONArray();

		JSONObject resObject = null;
		JSONObject qualificationObject = null;
		JSONObject experienceObject = null;
		JSONObject addressObject = null;
		JSONObject specialityObject = null;

		JSONObject finalObject = new JSONObject();

		ResultSet basicDetails = null;
		ResultSet qualification = null;
		ResultSet experience = null;
		ResultSet address = null;
		ResultSet speciality = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPDREGISTRATIONDETAILS")
					.registerStoredProcedureParameter("P_CPD_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_basicdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_addressdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_QUALIFICATION", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_experience", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_SPECIALITY", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_CPD_USERID", userId);
			storedProcedureQuery.execute();
			basicDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_basicdetails");
			address = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_addressdetails");
			qualification = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_QUALIFICATION");
			experience = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_experience");
			speciality = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_SPECIALITY");
			while (basicDetails.next()) {
				resObject = new JSONObject();
				resObject.put("basicDetailsId", basicDetails.getLong(1));
				resObject.put("cpdUserId", basicDetails.getLong(2));
				resObject.put("applicantFullName", basicDetails.getString(3));
				resObject.put("postName", basicDetails.getString(4));
				resObject.put("photo", basicDetails.getString(5));
				resObject.put("fatherName", basicDetails.getString(6));
				resObject.put("identityProofNo", basicDetails.getString(7));
				resObject.put("identityDoc", basicDetails.getString(8));
				resObject.put("dob", DateFormat.formatDate(basicDetails.getDate(9)));
//				resObject.put("stateName", basicDetails.getString(10));
//				resObject.put("distName", basicDetails.getString(11));
				resObject.put("age", basicDetails.getLong(10));
				resObject.put("gender", basicDetails.getString(11));
				resObject.put("doctorLicenseNo", basicDetails.getString(12));
				resObject.put("email", basicDetails.getString(13));
				resObject.put("languageReadWrite", basicDetails.getString(14));
				resObject.put("applicationSubmitDate", DateFormat.formatDate(basicDetails.getDate(15)));
				resObject.put("identityType", basicDetails.getString(16));
				resObject.put("signatureDOc", basicDetails.getString(17));
				resObject.put("presentMobile", basicDetails.getString(18));
				basicArray.put(resObject);
			}

			while (address.next()) {
				addressObject = new JSONObject();
				addressObject.put("addressId", address.getLong(1));
				addressObject.put("cpdUserId", address.getLong(2));
				addressObject.put("presentCountry", address.getString(3));
				addressObject.put("presentState", address.getString(4));
				addressObject.put("presentDistrict", address.getString(5));
				addressObject.put("presentMobile", address.getString(6));
				addressObject.put("presentAltNumber", address.getString(7));
				addressObject.put("presntPostalAdr", address.getString(8));
				addressObject.put("presentLandMark", address.getString(9));
				addressObject.put("presentPincode", address.getString(10));
				addressObject.put("sameAsPresent", address.getString(11));
				addressObject.put("permanentCountry", address.getString(12));
				addressObject.put("permanentState", address.getString(13));
				addressObject.put("permanentDistrict", address.getString(14));
				addressObject.put("permanentMobile", address.getString(15));
				addressObject.put("permanentAltNumber", address.getString(16));
				addressObject.put("permanentPostalAddress", address.getString(17));
				addressObject.put("permanentLandMark", address.getString(18));
				addressObject.put("permanentPincode", address.getString(19));
				addressArray.put(addressObject);
			}
			while (qualification.next()) {
				qualificationObject = new JSONObject();
				qualificationObject.put("qualificationName", qualification.getString(1));
				qualificationObject.put("universityName", qualification.getString(2));
				qualificationObject.put("totalMark", qualification.getLong(3));
				qualificationObject.put("markObtained", qualification.getLong(4));
				qualificationObject.put("passingYear", qualification.getLong(5));
				qualificationObject.put("percentage", qualification.getLong(6));
				qualificationObject.put("duration", qualification.getString(7));
				qualificationObject.put("document", qualification.getString(8));
				qualificationArray.put(qualificationObject);
			}
			while (experience.next()) {
				experienceObject = new JSONObject();
				experienceObject.put("employerName", experience.getString(1));
				experienceObject.put("postHeld", experience.getString(2));
				experienceObject.put("fromDate", DateFormat.formatDate(experience.getDate(3)));
				experienceObject.put("toDate", DateFormat.formatDate(experience.getDate(4)));
				experienceObject.put("totalExperience", experience.getString(5));
				experienceObject.put("experienceDoc", experience.getString(6));
				experienceArray.put(experienceObject);
			}
			while (speciality.next()) {
				specialityObject = new JSONObject();
				specialityObject.put("packageCode", speciality.getString(1));
				specialityObject.put("packageName", speciality.getString(2));
				specialityObject.put("document", speciality.getString(3));
				specialityArray.put(specialityObject);
			}
			finalObject.put("basicArray", basicArray);
			finalObject.put("qualificationArray", qualificationArray);
			finalObject.put("experienceArray", experienceArray);
			finalObject.put("addressArray", addressArray);
			finalObject.put("specialityArray", specialityArray);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (basicDetails != null) {
					basicDetails.close();
				}
				if (qualification != null) {
					qualification.close();
				}
				if (experience != null) {
					experience.close();
				}
				if (address != null) {
					address.close();
				}
				if (speciality != null) {
					speciality.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return finalObject.toString();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Response scheduleApplication(Map<String, Object> requestData) throws ParseException {
		Response response = new Response();
		Integer returnValue = null;
		Long currentUser = util.getCurrentUser();
		Long interViewAction = Long.valueOf(requestData.get("interviewAction").toString());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date fromDate = dateFormat.parse(String.valueOf(requestData.get("fromDate")));
		Date toDate = dateFormat.parse(String.valueOf(requestData.get("toDate")));
		String mobile = String.valueOf(requestData.get("mobile"));
		Long cpdUserId = Long.valueOf(requestData.get("cpdUserId").toString());
		String fromTime = String.valueOf(requestData.get("fromTime"));
		String toTime = String.valueOf(requestData.get("toTime"));
		String fromDate1 = String.valueOf(requestData.get("fromDate"));
		String toDate1 = String.valueOf(requestData.get("toDate"));
		String email = String.valueOf(requestData.get("email"));
		Integer templateId = null;
		String bodyText = null;
		String msgAction = null;
		String msgDepartMentId = null;
		String msgTemplateId = null;
		String msgBody = null;
		String subject = null, textMessagebody = null;
		// schedule
		if (interViewAction == 2) {
			templateId = 9;
			bodyText = fromDate1 + "#$#" + toDate1 + "#$#" + fromTime + "#$#" + toTime;
			// Reject
		} else if (interViewAction == 3) {
			templateId = 11;
			bodyText = "NA";
			// Re-Schedule
		} else if (interViewAction == 4) {
			templateId = 9;
			bodyText = fromDate1 + "#$#" + toDate1 + "#$#" + fromTime + "#$#" + toTime;
			// Approve
		} else if (interViewAction == 5) {
			templateId = 4;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_INTERVIEW_SCHEDULE")
					.registerStoredProcedureParameter("P_ACTION", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPDUSERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTERVIEW_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTERVIEW_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTERVIEW_FROMTIME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTERVIEW_TOTIME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_ACTION", interViewAction);
			storedProcedureQuery.setParameter("P_CPDUSERID", cpdUserId);
			storedProcedureQuery.setParameter("P_INTERVIEW_FROMDATE", fromDate);
			storedProcedureQuery.setParameter("P_INTERVIEW_TODATE", toDate);
			storedProcedureQuery.setParameter("P_INTERVIEW_FROMTIME", fromTime);
			storedProcedureQuery.setParameter("P_INTERVIEW_TOTIME", toTime);
			storedProcedureQuery.setParameter("P_REMARKS", String.valueOf(requestData.get("remarks")));
			storedProcedureQuery.setParameter("P_USERID", currentUser);

			storedProcedureQuery.execute();
			returnValue = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");

			if (returnValue == 1 && interViewAction == 2) {
				// For WhatsApp message
				sendWhatsAppMsg(mobile, bodyText, templateId, cpdUserId);
				msgAction = "";
				msgDepartMentId = "";
				msgTemplateId = null;
				// For Text message
				msgBody = MailUtils.getInterViewScheduleMsgWhatsApp(fromDate1, toDate1, fromTime, toTime);
				String msgRes = loginService.sendSMSForInterviewSchedule(mobile, msgBody, msgAction, msgDepartMentId,
						msgTemplateId);
				// For EMail
				subject = "Interview Schedule";
				textMessagebody = MailUtils.getInterViewScheduleMsg(fromDate1, toDate1, fromTime, toTime);
				mailingService.sendInterViewMail(subject, email, textMessagebody);
				response.setStatus("success");
				response.setMessage("Scheduled Successfully");
			} else if (returnValue == 1 && interViewAction == 3) {
				sendWhatsAppMsg(mobile, bodyText, templateId, cpdUserId);
				msgAction = "";
				msgDepartMentId = "";
				msgTemplateId = null;
				msgBody = MailUtils.getInterViewRejectedMsgWhatsApp();
				String msgRes = loginService.sendSMSForInterviewSchedule(mobile, msgBody, msgAction, msgDepartMentId,
						msgTemplateId);
				subject = "Interview Rejected";
				textMessagebody = MailUtils.getInterViewRejectedMsg();
				mailingService.sendInterViewMail(subject, email, textMessagebody);
				response.setStatus("success");
				response.setMessage("Rejected Successfully");
			} else if (returnValue == 1 && interViewAction == 4) {
				msgAction = "";
				msgDepartMentId = "";
				msgTemplateId = null;
				msgBody = MailUtils.getInterviewReScheduledMsgWhatsApp();
				String msgRes = loginService.sendSMSForInterviewSchedule(mobile, msgBody, msgAction, msgDepartMentId,
						msgTemplateId);
				sendWhatsAppMsg(mobile, bodyText, templateId, cpdUserId);
				subject = "Interview Re-Scheduled";
				textMessagebody = MailUtils.getInterviewReScheduledMsg(fromDate1, toDate1, fromTime, toTime);
				mailingService.sendInterViewMail(subject, email, textMessagebody);
				response.setStatus("success");
				response.setMessage("Re-Scheduled Successfully");
			} else if (returnValue == 1 && interViewAction == 5) {
//				sendWhatsAppMsg(mobile, bodyText, templateId, cpdUserId);
//				msgAction = "";
//				msgDepartMentId = "";
//				msgTemplateId = null;
//				msgBody = MailUtils.getApprovedMsgWhatsApp();
//				String msgRes = loginService.sendSMSForInterviewSchedule(mobile, msgBody, msgAction, msgDepartMentId,
//						msgTemplateId);
//				subject = "Interview Re-Scheduled";
//				textMessagebody = MailUtils.getApprovedMsg();
				response.setStatus("success");
				response.setMessage("Approved Successfully");
			} else {
				response.setStatus("failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error("Exception Occured in scheduleApplication of HrApprovalServiceImpl class", e);
			throw new RuntimeException(e.getMessage());
		}

		return response;
	}

	@Override
	public String getViewApplication(CPDApproveRequestBean requestData) {
		JSONArray freshApp = new JSONArray();
		ResultSet resultSet = null;
		JSONObject resObject = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_EMPANELMENT_HRVIEW")
					.registerStoredProcedureParameter("P_ACTION", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUS", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION", 2l);
			storedProcedureQuery.setParameter("P_FROMDATE", requestData.getFromDate());
			storedProcedureQuery.setParameter("P_TODATE", requestData.getToDate());
			storedProcedureQuery.setParameter("P_STATUS", requestData.getStatus());
			storedProcedureQuery.execute();
			resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (resultSet.next()) {
				resObject = new JSONObject();
				resObject.put("cpdName", resultSet.getString(1));
				resObject.put("mobile", resultSet.getString(2));
				resObject.put("email", resultSet.getString(3));
				resObject.put("dob", resultSet.getString(4));
				resObject.put("lisenceNo", resultSet.getString(5));
				resObject.put("interviewDateTime", resultSet.getString(6));
				resObject.put("status", resultSet.getString(7));
				resObject.put("submitDate", resultSet.getString(8));
				resObject.put("interviewFromDate", resultSet.getString(9));
				resObject.put("interviewToDate", resultSet.getString(10));
				resObject.put("interviewFromTime", resultSet.getString(11));
				resObject.put("interviewToTime", resultSet.getString(12));
				resObject.put("scheduledBy", resultSet.getLong(13));
				resObject.put("scheduledName", resultSet.getString(14));
				resObject.put("cpdUserId", resultSet.getLong(15));
				freshApp.put(resObject);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return freshApp.toString();
	}

	@Override
	public void commonDownloadMethod(String fileName, String prifix, String userid, HttpServletResponse response)
			throws IOException {
		CommonFileUpload.commonDownloadMethodForCPD(fileName, prifix, userid, response);

	}

	@Override
	public String getApprovedApplicationDetails(Long userId) {
		JSONArray basicArray = new JSONArray();
		JSONArray interviewArray = new JSONArray();
		JSONArray specialityArray = new JSONArray();
		JSONArray addressArray = new JSONArray();

		JSONObject resObject = null;
		JSONObject interviewObject = null;
		JSONObject specialityObject = null;
		JSONObject addressObject = null;

		JSONObject finalObject = new JSONObject();

		ResultSet basicDetails = null;
		ResultSet interview = null;
		ResultSet speciality = null;
		ResultSet address = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_USER_CPD_APPROVAL_DETAILS")
					.registerStoredProcedureParameter("P_CPD_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_basicdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_addressdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_INTERVIEW", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_SPECIALITY", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_CPD_USERID", userId);
			storedProcedureQuery.execute();
			basicDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_basicdetails");
			address = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_addressdetails");
			interview = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_INTERVIEW");
			speciality = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_SPECIALITY");
			while (basicDetails.next()) {
				resObject = new JSONObject();
				resObject.put("basicDetailsId", basicDetails.getLong(1));
				resObject.put("cpdUserId", basicDetails.getLong(2));
				resObject.put("applicantFullName", basicDetails.getString(3));
				resObject.put("postName", basicDetails.getString(4));
				resObject.put("photo", basicDetails.getString(5));
				resObject.put("fatherName", basicDetails.getString(6));
				resObject.put("identityProofNo", basicDetails.getString(7));
				resObject.put("identityDoc", basicDetails.getString(8));
				resObject.put("dob", DateFormat.formatDate(basicDetails.getDate(9)));
				resObject.put("age", basicDetails.getLong(10));
				resObject.put("gender", basicDetails.getString(11));
				resObject.put("doctorLicenseNo", basicDetails.getString(12));
				resObject.put("email", basicDetails.getString(13));
				resObject.put("languageReadWrite", basicDetails.getString(14));
				resObject.put("applicationSubmitDate", DateFormat.formatDate(basicDetails.getDate(15)));
				resObject.put("identityType", basicDetails.getString(16));
				resObject.put("signatureDOc", basicDetails.getString(17));
				resObject.put("presentMobile", basicDetails.getString(18));
				basicArray.put(resObject);
			}

			while (address.next()) {
				addressObject = new JSONObject();
				addressObject.put("addressId", address.getLong(1));
				addressObject.put("cpdUserId", address.getLong(2));
				addressObject.put("presentCountry", address.getString(3));
				addressObject.put("presentState", address.getString(4));
				addressObject.put("presentDistrict", address.getString(5));
				addressObject.put("presentMobile", address.getString(6));
				addressObject.put("presentAltNumber", address.getString(7));
				addressObject.put("presntPostalAdr", address.getString(8));
				addressObject.put("presentLandMark", address.getString(9));
				addressObject.put("presentPincode", address.getString(10));
				addressObject.put("sameAsPresent", address.getString(11));
				addressObject.put("permanentCountry", address.getString(12));
				addressObject.put("permanentState", address.getString(13));
				addressObject.put("permanentDistrict", address.getString(14));
				addressObject.put("permanentMobile", address.getString(15));
				addressObject.put("permanentAltNumber", address.getString(16));
				addressObject.put("permanentPostalAddress", address.getString(17));
				addressObject.put("permanentLandMark", address.getString(18));
				addressObject.put("permanentPincode", address.getString(19));
				addressArray.put(addressObject);
			}
			while (interview.next()) {
				interviewObject = new JSONObject();
				interviewObject.put("interviewId", interview.getLong(1));
				interviewObject.put("interviewFromDate", DateFormat.formatDate(interview.getDate(2)));
				interviewObject.put("interviewToDate", DateFormat.formatDate(interview.getDate(3)));
				interviewObject.put("fromTime", interview.getString(4));
				interviewObject.put("toTime", interview.getString(5));
				interviewObject.put("remarks", interview.getString(6));
				interviewObject.put("activeStatus", interview.getString(7));
				interviewObject.put("createdOn", interview.getString(8));
				interviewArray.put(interviewObject);
			}
			while (speciality.next()) {
				specialityObject = new JSONObject();
				specialityObject.put("packageCode", speciality.getString(1));
				specialityObject.put("packageName", speciality.getString(2));
				specialityObject.put("document", speciality.getString(3));
				specialityArray.put(specialityObject);
			}
			finalObject.put("basicArray", basicArray);
			finalObject.put("interviewArray", interviewArray);
			finalObject.put("specialityArray", specialityArray);
			finalObject.put("addressArray", addressArray);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (basicDetails != null) {
					basicDetails.close();
				}
				if (interview != null) {
					interview.close();
				}
				if (speciality != null) {
					speciality.close();
				}
				if (address != null) {
					address.close();
				}

			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return finalObject.toString();
	}

	@Override
	public Response finalApproveApplication(Map<String, Object> requestData) throws ParseException {
		Response response = new Response();
		Integer returnValue = null;
		Long currentUser = util.getCurrentUser();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date joiningFromDate = dateFormat.parse(String.valueOf(requestData.get("joiningFromDate")));
		Date fromDate = dateFormat.parse(String.valueOf(requestData.get("mouFromDate")));
		Date toDate = dateFormat.parse(String.valueOf(requestData.get("mouToDate")));
		String fromDate1 = String.valueOf(requestData.get("mouFromDate"));
		String toDate1 = String.valueOf(requestData.get("mouToDate"));
		Long cpdUserId = Long.valueOf(requestData.get("cpdUserId").toString());
		String userName = String.valueOf(requestData.get("userName"));
		Long maxClaim = Long.valueOf(requestData.get("maximumClaim").toString());
		String mobile = String.valueOf(requestData.get("mobile"));
		String email = String.valueOf(requestData.get("email"));
		String bodyText = null;
		String msgAction = null;
		String msgDepartMentId = null;
		String msgTemplateId = null;
		String msgBody = null;
		String subject = null, textMessagebody = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_FINAL_APPROVAL")
					.registerStoredProcedureParameter("P_ACTION", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPDUSERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERNAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DATE_OF_JOINING", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MOU_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MOU_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MAX_CLAIM", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_ACTION", 6l);
			storedProcedureQuery.setParameter("P_CPDUSERID", cpdUserId);
			storedProcedureQuery.setParameter("P_USERID", currentUser);
			storedProcedureQuery.setParameter("P_USERNAME", userName);
			storedProcedureQuery.setParameter("P_DATE_OF_JOINING", joiningFromDate);
			storedProcedureQuery.setParameter("P_MOU_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_MOU_TO_DATE", toDate);
			storedProcedureQuery.setParameter("P_MAX_CLAIM", maxClaim);

			storedProcedureQuery.execute();
			returnValue = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			if (returnValue == 1) {
				bodyText = userName + "#$#" + password;
				sendWhatsAppMsg(mobile, bodyText, 10, cpdUserId);
				msgAction = "";
				msgDepartMentId = "";
				msgTemplateId = null;
				msgBody = MailUtils.getFinalApprovedMsgWhatsApp(userName, password);
				String msgRes = loginService.sendSMSForInterviewSchedule(mobile, msgBody, msgAction, msgDepartMentId,
						msgTemplateId);
				textMessagebody = MailUtils.getFinalApprovedMsgWhatsApp(userName, password);
				mailingService.sendInterViewMail(subject, email, textMessagebody);
				response.setStatus("success");
				response.setMessage("Approved Successfully");
			} else {
				response.setStatus("failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error("Exception Occured in finalApproveApplication of HrApprovalServiceImpl class", e);
			throw new RuntimeException(e.getMessage());
		}

		return response;
	}

	public void sendWhatsAppMsg(String mobile, String bodyText, Integer templateId, Long userId) {
		WhatsappMessageLog whatsappMessageLog = null;
		try {
			WhatsappTemplate whatsappTemplate = whatsappTemplateRepository.getWhatsappTemplateById(templateId);
			int messageStatus = whatsAppServiceImpl.sendRequest(mobile, bodyText,
					whatsappTemplate.getTemplateName().trim());
			whatsappMessageLog = new WhatsappMessageLog();
			logger.info("Status Code: {}", messageStatus);
			whatsappMessageLog.setTemplateId(templateId);
			whatsappMessageLog.setMessageStatus(messageStatus == 200 ? 0L : 1L);
			whatsappMessageLog.setUserId(userId);
			whatsappMessageLog.setPhoneNo(mobile);
			whatsappMessageLog.setApiId(12);
			whatsappMessageLog.setRequestData(null);
			whatsappMessageLog.setCreatedBy(Math.toIntExact(util.getCurrentUser()));
			whatsappMessageLog.setCreatedOn(new Date());
			whatsappMessageLog.setStatusFlag(0);
			whatsappMessageLogRepository.save(whatsappMessageLog);
		} catch (Exception e) {
			logger.error("Exception Occured in sendWhatsAppMsg of HrApprovalServiceImpl class", e);
		}
	}
}
