/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.IntGrvUserBean;
import com.project.bsky.bean.InternalGrievanceBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.model.InternalGrievance;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.GroupTypeRepository;
import com.project.bsky.repository.InternalGrievanceRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.InternalGrievanceService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.GrievanceDoc;
import com.project.bsky.util.SMSUtil;

/**
 * @author priyanka.singh
 *
 */
@Service
public class InternalGrievanceServiceImpl implements InternalGrievanceService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UserDetailsRepository userrepo;

	@Autowired
	private InternalGrievanceRepository internalGrievanceRepository;

	@Autowired
	private GroupTypeRepository grouptyperepo;

	@Autowired
	private Logger logger;

	@Value("${file.path.Internalgrivance}")
	private String file;

	private static ResourceBundle bskyResourcesBundel5 = ResourceBundle.getBundle("fileConfiguration");

	@SuppressWarnings("unused")
	@Override
	public Response saveinternalGrievanceDetails(InternalGrievance internalGrievance, MultipartFile form) {
		Response response = new Response();
		try {
			GroupTypeDetails groupdetails = grouptyperepo.findBytypeId(internalGrievance.getSgroup());
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();
			Date date1 = cal.getTime();
			UserDetails userdetails = userrepo.findById((internalGrievance.getCreatedBy().getUserId())).get();
			internalGrievance.setStatusFlag(0);
			internalGrievance.setGroupId(internalGrievance.getGroupId());
			internalGrievance.setGroupId(groupdetails);
			internalGrievance.setCreatedBy(userdetails);
			internalGrievance.setCreatedOn(Calendar.getInstance().getTime());
			if (form != null) {
				String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				String Month = new SimpleDateFormat("MMM").format(new Date());
				Map<String, String> filePath = GrievanceDoc.createFileforInternalgrivance(year, form, Month,
						internalGrievance.getUserId());
				for (Map.Entry<String, String> entry : filePath.entrySet()) {
					if (file.contains(entry.getKey()))
						internalGrievance.setDocumentName(entry.getValue());
				}
			} else {
				internalGrievance.setDocumentName(null);
			}
			Integer countPCode = internalGrievanceRepository.checkCountDaate();
			String s = new SimpleDateFormat("ddMMyy").format(new Date());
			String tktValue = "TKT" + "/" + s + "/" + (countPCode + 1);
			internalGrievance.setTokenNumber(tktValue);
			internalGrievanceRepository.save(internalGrievance);
			Thread Thread = new Thread(new MyRunnable(internalGrievance));
			Thread.start();
			response.setMessage("Request Sucessfully Applied . Your Requested Token No is : " + tktValue);
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<InternalGrievance> getGrievanceDetails() {

		List<InternalGrievance> getInternalGrievanvanceDetail1 = new ArrayList<InternalGrievance>();
		List<InternalGrievance> getInternalGrievanvan = new ArrayList<InternalGrievance>();
		try {
			getInternalGrievanvanceDetail1 = internalGrievanceRepository.findAlldata();
			for (InternalGrievance x : getInternalGrievanvanceDetail1) {
				if (x.getCreatedOn() != null) {
					x.setScreate((x.getCreatedOn().toString().substring(0, 10)));
				}
				if (x.getClosingDate() != null) {
					x.setCloseDate((x.getClosingDate().toString().substring(0, 10)));
				}
				if (x.getExpectedDate() != null) {
					x.setExpectedDate1((x.getExpectedDate().toString().substring(0, 10)));
				}
				if (x.getClosingDescription() != null) {
					x.setClosingDescription(x.getClosingDescription());
				} else {
					x.setClosingDescription("N/A");
				}
				getInternalGrievanvan.add(x);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getInternalGrievanvanceDetail1;
	}

	@Override
	public void downLoadPassbook(String fileName, String year, HttpServletResponse response, String month) {
		String folderName = null;
		try {
			folderName = bskyResourcesBundel5.getString("folder.Grievance");
			CommonFileUpload.downLoadGrivancedoc(fileName, year, response, month, folderName);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public InternalGrievance getInternalGrievanceById(Long grievanceId) {
		try {
			return internalGrievanceRepository.findById(grievanceId).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	@Override
	public Response update(String assign, String closedate, Integer status, Long id, Long updatedBy,
			String closingDescription) {
		Response response = new Response();
		try {
			InternalGrievance internalGrievance = internalGrievanceRepository.findById(id).get();
			internalGrievance.setUpdateOn(Calendar.getInstance().getTime());
			internalGrievance.setUpdatedBy(updatedBy);
			if (!closedate.equals("")) {
				internalGrievance.setClosingDate((new SimpleDateFormat("dd-MMM-yyyy").parse(closedate)));
			}
			internalGrievance.setAssignedName(assign);
			internalGrievance.setStatusFlag(status);
			internalGrievance.setClosingDescription(closingDescription);
			internalGrievanceRepository.save(internalGrievance);
			response.setMessage("Remarks Updated Successfully");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<IntGrvUserBean> getgrvuserdatabytypeid(Integer typeid) {
		List<IntGrvUserBean> list = new ArrayList<IntGrvUserBean>();
		List<UserDetails> userlist = null;
		try {
			if (typeid != null) {
				userlist = userrepo.getintuserdetails(typeid);
			} else {
				userlist = userrepo.findAllActive();
			}

			for (UserDetails x : userlist) {
				IntGrvUserBean intgrvbean = new IntGrvUserBean();
				intgrvbean.setUsername(x.getUserName());
				intgrvbean.setUserid(String.valueOf(x.getUserId()));
				intgrvbean.setGroupid(String.valueOf(x.getGroupId().getTypeId()));
				if (x.getGroupId().getTypeId() == 5) {
					intgrvbean.setFullname(x.getFullname() + " (" + x.getUserName() + ")");
				} else {
					intgrvbean.setFullname(x.getFullname());
				}
				intgrvbean.setStatusflag(String.valueOf(x.getStatus()));
				intgrvbean.setPhoneno(String.valueOf(x.getPhone()));
				intgrvbean.setEmail(x.getEmail());
				intgrvbean.setGroupName(x.getGroupId().getGroupTypeName());
				intgrvbean.setIsOtp(x.getIsOtpAllowed());
				list.add(intgrvbean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	String module(String no) {
		if (no.equals("1")) {
			return "TMS";
		} else if (no.equals("2")) {
			return "CMS";
		} else if (no.equals("3")) {
			return "Grievance";
		} else if (no.equals("4")) {
			return "Hospital Empanelment";
		} else if (no.equals("5")) {
			return "ChartBoart";
		} else if (no.equals("6")) {
			return "Mobile Application";
		} else if (no.equals("7")) {
			return "Website";
		} else if (no.equals("8")) {
			return "CCE(104)";
		} else if (no.equals("9")) {
			return "User Management";
		} else {
			return "N/A";
		}

	}

	String type(Integer no) {
		if (no == 1) {
			return "Complaint";
		} else if (no == 2) {
			return "Issue";
		} else if (no == 3) {
			return "Request";
		} else if (no == 3) {
			return "Suggestion";
		} else {
			return "N/A";
		}
	}

	String priority(Integer no) {
		if (no == 1) {
			return "High";
		} else if (no == 2) {
			return "Medium";
		} else if (no == 3) {
			return "Low";
		} else {
			return "N/A";
		}
	}

	class MyRunnable implements Runnable {
		private InternalGrievance internalGrievance;

		public MyRunnable(InternalGrievance internalGrievance) {
			this.internalGrievance = internalGrievance;
		}

		@Override
		public void run() {

			List<String> res = new ArrayList<String>();
			res.add("tapas.samal@csm.tech");
			res.add("anshuman.mishra@csm.tech");
			res.add("gyanaranjan.das@csm.tech");
			res.add("sonali.mishra@csm.tech");
			res.add("trilita.choudhury@csm.tech");
			String subject = "Grievance Request Generated";
			String textmessage = "Dear,\r\n" + "\r\n" + "A Grievance Request(" + internalGrievance.getTokenNumber()
					+ ")applied on " + new SimpleDateFormat("dd/MM/yyyy").format(internalGrievance.getCreatedOn())
					+ " has been generated." + " \r\n" + "\r\n" + "\r\n" + "Description - "
					+ internalGrievance.getDescription() + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "Regards\r\n"
					+ "BSKY Team\r\n" + "\r\n" + "\r\n" + "This is a system generated mail. Please do not reply.\r\n"
					+ "\r\n";
			for (String s : res) {
				String mail = s;
				SMSUtil.sendmail(subject, textmessage, mail);
			}
		}
	}

	@Override
	public List<Object> getGrievnceFilterDetails(String categoryType, String priority, String status, Date fromDate,
			Date toDate) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("usp_internal_grievance_filter_list")
					.registerStoredProcedureParameter("p_category_type", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_priority", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUS_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_category_type", categoryType);
			storedProcedure.setParameter("P_priority", priority);
			storedProcedure.setParameter("P_STATUS_FLAG", status);
			storedProcedure.setParameter("P_FROM_DATE", fromDate);
			storedProcedure.setParameter("P_TO_DATE", toDate);

			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (list.next()) {
				InternalGrievanceBean details = new InternalGrievanceBean();
				details.setTokenNumber(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setGrievanceBy(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setModuleName(list.getString(3) == null ? "N/A" : list.getString(3));
				details.setCategoryType(list.getString(4) == null ? "N/A" : list.getString(4));
				details.setPriority(list.getString(5) == null ? "N/A" : list.getString(5));
				details.setCreatedOn(list.getString(6) == null ? "N/A" : list.getString(6));
				details.setExpectedDate1(list.getString(7) == null ? "N/A" : list.getString(7));
				details.setCloseDate(list.getString(8) == null ? "N/A" : list.getString(8));
				details.setAssignedName(list.getString(9) == null ? " " : list.getString(9));
				details.setDescription(list.getString(10) == null ? "N/A" : list.getString(10));
				details.setClosingDescription(list.getString(11) == null ? "N/A" : list.getString(11));
				details.setDocumentName(list.getString(12) == null ? "N/A" : list.getString(12));
				details.setStatusFlag(list.getString(13) == null ? "N/A" : list.getString(13));
				details.setFullname(list.getString(14) == null ? "N/A" : list.getString(14));
				object.add(details);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return object;
	}

}
