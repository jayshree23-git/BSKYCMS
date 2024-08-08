package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.model.NotificationDetails;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.GroupTypeRepository;
import com.project.bsky.repository.NotificationDetailsRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.NotificationDetailsService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.Notificationdoc;

@Service
public class NotificationDetailsServiceImpl implements NotificationDetailsService {

	@Autowired
	private NotificationDetailsRepository notificationrepo;

	@Autowired
	private GroupTypeRepository grouptyperepo;

	@Autowired
	private UserDetailsRepository userrepo;

	@Autowired
	private Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Value("${file.path.Notification}")
	private String file;

	private static ResourceBundle bskyResourcesBundel4 = ResourceBundle.getBundle("fileConfiguration");

	@Override
	public Response save(NotificationDetails notificationdetails, MultipartFile form) {
		Response response = new Response();
		System.out.println(notificationdetails);
		try {
			GroupTypeDetails g = grouptyperepo.findBytypeId(Integer.parseInt(notificationdetails.getSgroup()));
			UserDetails u = userrepo.findById(Long.parseLong(notificationdetails.getScreate())).get();
			notificationdetails.setGroupId(g);
			notificationdetails.setCreatedBy(u);
			notificationdetails.setCreatedOn(date);
			notificationdetails.setStatusFlag(0);
			notificationdetails.setFromDate(new SimpleDateFormat("dd-MMM-yyyy").parse(notificationdetails.getFdate()));
			notificationdetails.setToDate(new SimpleDateFormat("dd-MMM-yyyy").parse(notificationdetails.getTdate()));
			if (form != null) {
				String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				String Month = new SimpleDateFormat("MMM").format(new Date());
				Map<String, String> filePath = Notificationdoc.createFilefornotiication(year, form, Month);
				for (Map.Entry<String, String> entry : filePath.entrySet()) {
					if (file.contains(entry.getKey()))
						notificationdetails.setDocpath(entry.getValue());
				}
			} else {
				notificationdetails.setDocpath(null);
			}
			notificationrepo.save(notificationdetails);
			response.setMessage("Notification Submitted Successfully");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen");
			response.setStatus("Failed");

		}
		return response;
	}

	@Override
	public List<NotificationDetails> findall() {

		List<NotificationDetails> list2 = new ArrayList<NotificationDetails>();
		try {
			DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
			List<NotificationDetails> list = notificationrepo.notificationdata();
			for (NotificationDetails x : list) {
				x.setFdate(f.format(x.getFromDate()));
				x.setTdate(f.format(x.getToDate()));
				x.setScreate(x.getCreatedOn().toString());
				list2.add(x);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list2;
	}

	@Override
	public Response update(NotificationDetails notificationdetails, MultipartFile form) {
		Response response = new Response();
		try {
			NotificationDetails notificationdetails2 = notificationrepo
					.findById(notificationdetails.getNotificationId()).get();
			GroupTypeDetails g = grouptyperepo.findBytypeId(Integer.parseInt(notificationdetails.getSgroup()));
			notificationdetails.setUpdatedBy(Long.parseLong(notificationdetails.getScreate()));
			notificationdetails.setUpdateOn(date);
			notificationdetails.setGroupId(g);
			notificationdetails.setCreatedBy(notificationdetails2.getCreatedBy());
			notificationdetails.setCreatedOn(notificationdetails2.getCreatedOn());
			notificationdetails.setFromDate(new SimpleDateFormat("dd-MMM-yyyy").parse(notificationdetails.getFdate()));
			notificationdetails.setToDate(new SimpleDateFormat("dd-MMM-yyyy").parse(notificationdetails.getTdate()));
			if (form != null) {
				String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				String Month = new SimpleDateFormat("MMM").format(new Date());
				Map<String, String> filePath = Notificationdoc.createFilefornotiication(year, form, Month);
				for (Map.Entry<String, String> entry : filePath.entrySet()) {
					if (file.contains(entry.getKey()))
						notificationdetails.setDocpath(entry.getValue());
				}
			} else {
				notificationdetails.setDocpath(notificationdetails2.getDocpath());
			}
			notificationrepo.save(notificationdetails);
			response.setMessage("Notification Updated Successfully");
			response.setStatus("Success");
		} catch (Exception e) {
			response.setMessage("Some Error Happen");
			response.setStatus("Failed");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public List<NotificationDetails> getnotificationshow(Integer groupId) {

		return notificationrepo.getnotificationshow(groupId);
	}

	@Override
	public void downLoadPassbook(String fileName, String year, HttpServletResponse response, String month) {
		System.out.println(fileName + "--" + year + "--" + response + "--" + month);
		String folderName = null;
		try {
			if (fileName.contains("NOTICE")) {
				folderName = bskyResourcesBundel4.getString("folder.Notification");
			} else {
				folderName = bskyResourcesBundel4.getString("folder.Mouuploaddoc");
			}
			CommonFileUpload.downLoadNotificationdoc(fileName, year, response, month, folderName);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public List<Map<String, Object>> getHospitalListClaimsNotVerified(Long userId, int actionCode, int days) {
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("USP_GET_HOSPITAL_LIST_NOT_VERIFIED_BY_SNA")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DAYS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)

					.setParameter("P_USER_ID", userId).setParameter("P_ACTION_CODE", actionCode)
					.setParameter("P_DAYS", days);

			storedProcedureQuery.execute();
			List<?> resultList = storedProcedureQuery.getResultList();

			return resultList.stream().map(object -> (Object[]) object).map(obj -> {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("hospitalCode", obj[0] != null ? obj[0].toString() : "N/A");
				responseMap.put("hospitalName", obj[1] != null ? obj[1].toString() : "N/A");
				responseMap.put("claimCount", obj[2] != null ? obj[2].toString() : "N/A");
				try {
					responseMap.put("lastActionDate",
							obj[3] != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(obj[3].toString())
									: null);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
				return responseMap;
			}).collect(Collectors.toList());

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Map<String, Object>> getHospitalClaimsNotVerified(Long userId, String hospitalCode, int actionCode,
			int days) {
		List<Map<String, Object>> responseMapList = new ArrayList<>();
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("USP_GET_HOSPITAL_LIST_NOT_VERIFIED_BY_SNA")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DAYS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)

					.setParameter("P_USER_ID", userId).setParameter("P_HOSPITAL_CODE", hospitalCode)
					.setParameter("P_ACTION_CODE", actionCode).setParameter("P_DAYS", days);

			storedProcedureQuery.execute();
			List<?> resultList = storedProcedureQuery.getResultList();

			responseMapList = resultList.stream().map(object -> (Object[]) object).map(obj -> {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("claimId", obj[0] != null ? obj[0].toString() : "N/A");
				responseMap.put("claimNo", obj[1] != null ? obj[1].toString() : "N/A");
				responseMap.put("caseNo", obj[2] != null ? obj[2].toString() : "N/A");
				responseMap.put("urn", obj[3] != null ? obj[3].toString() : "N/A");
				responseMap.put("hospitalName", obj[4] != null ? obj[4].toString() : "N/A");
				responseMap.put("packageCode", obj[5] != null ? obj[5].toString() : "N/A");
				responseMap.put("packageName", obj[6] != null ? obj[6].toString() : "N/A");
				responseMap.put("actualDateOfAdmission",
						obj[7] != null ? com.project.bsky.util.DateFormat.FormatStringToDate(obj[7].toString())
								: "N/A");
				responseMap.put("actualDateOfDischarge",
						obj[8] != null ? com.project.bsky.util.DateFormat.FormatStringToDate(obj[8].toString())
								: "N/A");
				responseMap.put("cpdName", obj[9] != null ? obj[9].toString() : "N/A");
				responseMap.put("patientName", obj[10] != null ? obj[10].toString() : "N/A");
				responseMap
						.put("claimStatus",
								obj[11] != null
										? Integer.parseInt(obj[11].toString()) == 1 ? "Approval"
												: Integer.parseInt(obj[11].toString()) == 2 ? "CPD Rejected" : "Other"
										: "N/A");
				return responseMap;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return responseMapList;
	}

	@Override
	public List<NotificationDetails> getdataforNotificationDetails(String fromdate, String todate) {
		List<NotificationDetails> listOfReport2 = new ArrayList<NotificationDetails>();
		try {
			DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
			Date fromDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
			Date todate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			List<NotificationDetails> listOfReport = notificationrepo.findBydataBetween(fromDate1, todate1);
			for (NotificationDetails x : listOfReport) {
				x.setFdate(f.format(x.getFromDate()));
				x.setTdate(f.format(x.getToDate()));
				listOfReport2.add(x);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listOfReport2;
	}

	@Override
	public List<NotificationDetails> getdataforNotificationDetailsOns(String fromdate, String todate, String groupId) {
		List<NotificationDetails> listOfReport2 = new ArrayList<NotificationDetails>();
		try {
			Date fromDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
			Date todate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			if (groupId == "") {
				DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
				List<NotificationDetails> listOfReport = notificationrepo.findBydataBetween(fromDate1, todate1);
				for (NotificationDetails x : listOfReport) {
					x.setFdate(f.format(x.getFromDate()));
					x.setTdate(f.format(x.getToDate()));
					listOfReport2.add(x);
				}
			} else {
				DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
				GroupTypeDetails groupTypeDetails = grouptyperepo.findByTypeId(Integer.parseInt(groupId));
				List<NotificationDetails> listOfReport = notificationrepo.findBydataBetween1(fromDate1, todate1,
						groupTypeDetails);
				for (NotificationDetails x : listOfReport) {
					x.setFdate(f.format(x.getFromDate()));
					x.setTdate(f.format(x.getToDate()));
					listOfReport2.add(x);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listOfReport2;
	}

}
