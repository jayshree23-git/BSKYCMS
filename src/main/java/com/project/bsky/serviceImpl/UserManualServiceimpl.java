package com.project.bsky.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.project.bsky.bean.Detailshospitalprofilebean;
import com.project.bsky.bean.Grouptypelist;
import com.project.bsky.bean.HospitalDoctorprofile;
import com.project.bsky.bean.Hospitaldoctorprofilebean;
import com.project.bsky.bean.MultisurgeryDoctor;
import com.project.bsky.bean.PrimaryLinkBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.UserMenuMapping;
import com.project.bsky.repository.UserMenuMappingRepository;
import com.project.bsky.repository.UsermanulaRepository;
import com.project.bsky.service.UserManualService;
import com.project.bsky.util.UserMenualFileUpdation;

@Service
public class UserManualServiceimpl implements UserManualService {
	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${file.path.Usermanual}")
	private String Usermanual;

	@Autowired
	private UserMenuMappingRepository userRepo;

	@Autowired
	private UsermanulaRepository usermanulaRepository;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> getlistof() {
		List<Object> grouptypelist = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager.createStoredProcedureQuery("USP_USERMANUAL_LIST")
					.registerStoredProcedureParameter("P_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_actioncode", "A");
			storedProcedure.setParameter("p_userid", null);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_P_MSGOUT");
			while (rs.next()) {
				Grouptypelist grouplist = new Grouptypelist();
				grouplist.setTyprid(rs.getLong(1));
				grouplist.setGrouptypename(rs.getString(2));
				grouplist.setGroupid(rs.getLong(3));
				grouptypelist.add(grouplist);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return grouptypelist;
	}

	@Override
	public List<PrimaryLinkBean> getprimarylinkname(Integer groupid) {
		List<PrimaryLinkBean> list = new ArrayList<PrimaryLinkBean>();
		try {
			List<Long> cpdlist = usermanulaRepository.getAllGroup(groupid);
			for (Long cpd : cpdlist) {
				list = getPrimaryLinksFromUserId(cpd.intValue());
				if (list != null && list.size() != 0) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	public List<PrimaryLinkBean> getPrimaryLinksFromUserId(int userId) {
		List<PrimaryLinkBean> list = new ArrayList<PrimaryLinkBean>();
		try {
			List<UserMenuMapping> userList = userRepo.getPrimaryLinksFromUserId(userId);
			for (UserMenuMapping u : userList) {
				PrimaryLinkBean p = new PrimaryLinkBean();
				p.setPrimaryLinkId(u.getPrimaryLink().getPrimaryLinkId().intValue());
				p.setPrimaryLinkName(u.getPrimaryLink().getPrimaryLinkName().toString());
				p.setGlobalLinkId(Long.valueOf(u.getGlobal_Link_Id()));
				list.add(p);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response saveUsermanuallreport(String description, MultipartFile selectedFile, Long primarylinkid,
			String primarylinname, Long grouptype, String grouptypename, Long userid, Long actionflag,
			Long user_manual_id) throws Exception {
		Response response = new Response();
		Integer claimraiseInteger = null;
		Map<String, String> filePath = UserMenualFileUpdation.createFile(selectedFile);
		filePath.forEach((k, v) -> {
			if (v != null && !v.equalsIgnoreCase("")) {
				String fullFilePath = UserMenualFileUpdation.getFullDocumentPath(v,
						UserMenualFileUpdation.getFolderName(v));
				File file = new File(fullFilePath);
				if (!file.exists()) {
					filePath.forEach((k1, v1) -> {
						if (v1 != null && !v1.equalsIgnoreCase("")) {
							String fullFilePath1 = UserMenualFileUpdation.getFullDocumentPath(v1,
									UserMenualFileUpdation.getFolderName(v1));
							File file1 = new File(fullFilePath1);
							if (file1.exists()) {
								file1.delete();
							}
						}
					});

					if (k.equalsIgnoreCase("Usermanual"))
						throw new RuntimeException(
								selectedFile.getOriginalFilename() + " Usermanual Slip Failed To Save in Server!");
				}
			}
		});
		try {
			StoredProcedureQuery saveCpdUserData = this.entityManager
					.createStoredProcedureQuery("usp_user_manual_submit_cms")
					.registerStoredProcedureParameter("p_action_code", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_type_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_type_name", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_primary_link_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_primary_link_name", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_manual_document", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remarks", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_manual_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.OUT);
			if (actionflag == 1) {
				saveCpdUserData.setParameter("p_action_code", actionflag);
				saveCpdUserData.setParameter("p_user_type_id", grouptype);
				saveCpdUserData.setParameter("p_user_type_name", grouptypename.trim());
				saveCpdUserData.setParameter("p_primary_link_id", primarylinkid);
				saveCpdUserData.setParameter("p_primary_link_name", primarylinname.trim());
				for (Map.Entry<String, String> entry : filePath.entrySet()) {
					if (Usermanual.contains(entry.getKey()))
						saveCpdUserData.setParameter("p_user_manual_document", entry.getValue());
				}
				saveCpdUserData.setParameter("p_remarks", description.trim());
				saveCpdUserData.setParameter("p_userid", userid);
				saveCpdUserData.setParameter("p_user_manual_id", null);
				saveCpdUserData.execute();
			} else if (actionflag == 2) {
				saveCpdUserData.setParameter("p_action_code", actionflag);
				saveCpdUserData.setParameter("p_user_type_id", grouptype);
				saveCpdUserData.setParameter("p_user_type_name", grouptypename.trim());
				saveCpdUserData.setParameter("p_primary_link_id", primarylinkid);
				saveCpdUserData.setParameter("p_primary_link_name", primarylinname.trim());
				for (Map.Entry<String, String> entry : filePath.entrySet()) {
					if (Usermanual.contains(entry.getKey()))
						saveCpdUserData.setParameter("p_user_manual_document", entry.getValue());
				}
				saveCpdUserData.setParameter("p_remarks", description.trim());
				saveCpdUserData.setParameter("p_userid", userid);
				saveCpdUserData.setParameter("p_user_manual_id", user_manual_id);
				saveCpdUserData.execute();
			}
			claimraiseInteger = (Integer) saveCpdUserData.getOutputParameterValue("P_MSG_OUT");
			if (claimraiseInteger == 1) {
				if (actionflag == 1) {
					response.setStatus("Success");
					response.setMessage("Submitted Successfully");
				} else if (actionflag == 2) {
					response.setStatus("Success");
					response.setMessage("Updated Successfully");
				}
			} else if (claimraiseInteger == 2) {
				response.setStatus("Failed");
				response.setMessage("This Record  Already exits..");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return response;
	}

	@Override
	public List<Object> getallviewlist(Long userid) {
		List<Object> viewlist = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager.createStoredProcedureQuery("USP_USERMANUAL_LIST")
					.registerStoredProcedureParameter("P_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_actioncode", "B");
			storedProcedure.setParameter("p_userid", userid);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_P_MSGOUT");
			while (rs.next()) {
				Grouptypelist view = new Grouptypelist();
				view.setUser_type_id(rs.getLong(1));
				view.setUser_type_name(rs.getString(2));
				view.setPrimary_link_id(rs.getLong(3));
				view.setPrimary_link_name(rs.getString(4));
				view.setUser_manual_document(rs.getString(5));
				view.setRemarks(rs.getString(6));
				view.setFull_name(rs.getString(7));
				view.setCreated_on(rs.getString(8));
				view.setUser_manual_id(rs.getLong(9));
				viewlist.add(view);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return viewlist;
	}

	@Override
	public void downLoadFile(String fileName, Long type, HttpServletResponse response) {
		String folderName = null;
		if (fileName.startsWith(bskyResourcesBundel.getString("file.presurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.presurg.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.postsurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.postsurg.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Intrasurgery.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.IntraSurgeryPic.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Specimen.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.SpecimenRemovalPic.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Patient.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.PatientPic");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.moredocument.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Additionaldoc1");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.needmoredocument.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Additionaldoc2");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.AdditionalDoc.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.AdditionalDoc");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.DischargeSlip.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.DischargeSlip");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.investigationDoc1.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.investigation1");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.investigationDoc2.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.investigation2");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.cceDOc1.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.cce1");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.cceDOc2.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.cce2");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.cceDOc3.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.cce3");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.dgOfficer.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.dgoDocument");
		} else if (fileName.startsWith("OC")) {
			folderName = bskyResourcesBundel.getString("folder.Overridefile");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Declaration.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Declaration");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Identity.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Identity");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Recomply.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Recomply");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Usermanual.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Usermanual");
		} else {
			folderName = "PREAUTHDOC";
		}
		try {
			if (type == 1) {
				UserMenualFileUpdation.downloadFilenewenrollment(fileName, folderName, response);
			} else if (type == 2) {
				UserMenualFileUpdation.downloadFilenewenrollmentdownload(fileName, folderName, response);
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<Object> getlinklistdata(Long userid) {
		List<Object> linklist = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("usp_user_manual_download_list")
					.registerStoredProcedureParameter("p_user_type_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_user_type_id", userid);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (rs.next()) {
				Grouptypelist link = new Grouptypelist();
				link.setUser_type_name(rs.getString(1));
				link.setPrimary_link_id(rs.getLong(2));
				link.setPrimary_link_name(rs.getString(3));
				link.setUser_manual_document(rs.getString(4));
				link.setRemarks(rs.getString(5));
				link.setFull_name(rs.getString(6));
				link.setCreated_on(rs.getString(7));
				link.setUpdated_on(rs.getString(8));
				linklist.add(link);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return linklist;
	}

	@Override
	public List<Object> getSearchdata(Long primarylinkid) {
		List<Object> primarylink = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("usp_user_manual_search")
					.registerStoredProcedureParameter("p_primary_link_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_primary_link_id", primarylinkid);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (rs.next()) {
				Grouptypelist link = new Grouptypelist();
				link.setUser_type_name(rs.getString(1));
				link.setPrimary_link_id(rs.getLong(2));
				link.setPrimary_link_name(rs.getString(3));
				link.setUser_manual_document(rs.getString(4));
				link.setRemarks(rs.getString(5));
				link.setFull_name(rs.getString(6));
				link.setCreated_on(rs.getString(7));
				link.setUpdated_on(rs.getString(8));
				primarylink.add(link);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return primarylink;
	}

	@Override
	public List<Object> gethospitaldatabystate(String hospitalcode) {
		List<Object> hosdata = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_DOCTOR_PROFILE")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCTOR_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CONTACT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REGISTRATION_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DATE_OF_JOINING", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIALITY_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIALITY_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROFILE_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_speciality_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION_CODE", 1);
			storedProcedure.setParameter("P_HOSPITAL_CODE", hospitalcode.trim());
			storedProcedure.setParameter("P_DOCTOR_NAME", null);
			storedProcedure.setParameter("P_CONTACT_NO", null);
			storedProcedure.setParameter("P_REGISTRATION_NO", null);
			storedProcedure.setParameter("P_DATE_OF_JOINING", null);
			storedProcedure.setParameter("P_SPECIALITY_CODE", null);
			storedProcedure.setParameter("P_SPECIALITY_NAME", null);
			storedProcedure.setParameter("P_STATE_CODE", null);
			storedProcedure.setParameter("P_DISTRICT_CODE", null);
			storedProcedure.setParameter("P_USERID", null);
			storedProcedure.setParameter("P_PROFILE_ID", null);
			storedProcedure.setParameter("P_speciality_id", null);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Hospitaldoctorprofilebean hos = new Hospitaldoctorprofilebean();
				hos.setHospitalcode(rs.getString(1));
				hos.setHospitalname(rs.getString(2));
				hos.setStatecode(rs.getString(3));
				hos.setStatename(rs.getString(4));
				hos.setDistrictcode(rs.getString(5));
				hos.setDistrictname(rs.getString(6));
				hosdata.add(hos);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return hosdata;
	}

	@Override
	public List<Object> getSpecialitydetails(String hospitalcode) {
		List<Object> specaility = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_DOCTOR_PROFILE")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCTOR_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CONTACT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REGISTRATION_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DATE_OF_JOINING", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIALITY_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIALITY_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROFILE_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_speciality_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION_CODE", 2);
			storedProcedure.setParameter("P_HOSPITAL_CODE", hospitalcode.trim());
			storedProcedure.setParameter("P_DOCTOR_NAME", null);
			storedProcedure.setParameter("P_CONTACT_NO", null);
			storedProcedure.setParameter("P_REGISTRATION_NO", null);
			storedProcedure.setParameter("P_DATE_OF_JOINING", null);
			storedProcedure.setParameter("P_SPECIALITY_CODE", null);
			storedProcedure.setParameter("P_SPECIALITY_NAME", null);
			storedProcedure.setParameter("P_STATE_CODE", null);
			storedProcedure.setParameter("P_DISTRICT_CODE", null);
			storedProcedure.setParameter("P_USERID", null);
			storedProcedure.setParameter("P_PROFILE_ID", null);
			storedProcedure.setParameter("P_speciality_id", null);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Hospitaldoctorprofilebean specialitydata = new Hospitaldoctorprofilebean();
				specialitydata.setId(rs.getLong(1));
				specialitydata.setPackageheadercode(rs.getString(2));
				specialitydata.setPackageheader(rs.getString(3));
				specaility.add(specialitydata);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return specaility;
	}

	@Override
	public Response getSubmitdata(HospitalDoctorprofile requestBean) {
		Response response = new Response();
		String packageheadercode = null;
		String packageheader = null;
		StringBuffer bufferlist = new StringBuffer();
		StringBuffer bufferlist1 = new StringBuffer();
		Integer claimraiseInteger = null;
		if (requestBean.getSpecialitycode() != null) {
			for (String element : requestBean.getSpecialitycode()) {
				bufferlist.append(element + ",");
			}
			packageheadercode = bufferlist.substring(0, bufferlist.length() - 1);
		}
		if (requestBean.getPackageheader() != null) {
			for (String element : requestBean.getPackageheader()) {
				bufferlist1.append(element + ",");
			}
			packageheader = bufferlist1.substring(0, bufferlist1.length() - 1);
		}
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_DOCTOR_PROFILE")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCTOR_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CONTACT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REGISTRATION_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DATE_OF_JOINING", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIALITY_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIALITY_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROFILE_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_speciality_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);
			if (requestBean.getProfileid() == null) {
				storedProcedure.setParameter("P_ACTION_CODE", 3);
				storedProcedure.setParameter("P_HOSPITAL_CODE", requestBean.getHospitalCodename().trim());
				storedProcedure.setParameter("P_DOCTOR_NAME", requestBean.getDocname().trim());
				storedProcedure.setParameter("P_CONTACT_NO", requestBean.getContactnumber().trim());
				storedProcedure.setParameter("P_REGISTRATION_NO", requestBean.getRegnumber().trim());
				storedProcedure.setParameter("P_DATE_OF_JOINING", requestBean.getDateofjoining().trim());
				storedProcedure.setParameter("P_SPECIALITY_CODE", packageheadercode.trim());
				storedProcedure.setParameter("P_SPECIALITY_NAME", packageheader.trim());
				storedProcedure.setParameter("P_STATE_CODE", requestBean.getStatecodename().trim());
				storedProcedure.setParameter("P_DISTRICT_CODE", requestBean.getDistrictcodename().trim());
				storedProcedure.setParameter("P_USERID", requestBean.getUserid());
				storedProcedure.setParameter("P_PROFILE_ID", null);
				storedProcedure.setParameter("P_speciality_id", null);
				storedProcedure.execute();
				claimraiseInteger = (Integer) storedProcedure.getOutputParameterValue("P_OUT");
				if (claimraiseInteger == 1) {
					response.setStatus("Success");
					response.setMessage("Submitted Successfully");
				} else {
					response.setStatus("error");
					response.setMessage("Something Went Wrong");
				}
			} else {
				storedProcedure.setParameter("P_ACTION_CODE", 5);
				storedProcedure.setParameter("P_HOSPITAL_CODE", requestBean.getHospitalCodename().trim());
				storedProcedure.setParameter("P_DOCTOR_NAME", requestBean.getDocname().trim());
				storedProcedure.setParameter("P_CONTACT_NO", requestBean.getContactnumber().trim());
				storedProcedure.setParameter("P_REGISTRATION_NO", requestBean.getRegnumber().trim());
				storedProcedure.setParameter("P_DATE_OF_JOINING", requestBean.getDateofjoining().trim());
				storedProcedure.setParameter("P_SPECIALITY_CODE", packageheadercode.trim());
				storedProcedure.setParameter("P_SPECIALITY_NAME", packageheader.trim());
				storedProcedure.setParameter("P_STATE_CODE", requestBean.getStatecodename().trim());
				storedProcedure.setParameter("P_DISTRICT_CODE", requestBean.getDistrictcodename().trim());
				storedProcedure.setParameter("P_USERID", requestBean.getUserid());
				storedProcedure.setParameter("P_PROFILE_ID", requestBean.getProfileid());
				storedProcedure.setParameter("P_speciality_id", requestBean.getSpeciality_id());
				storedProcedure.execute();
				claimraiseInteger = (Integer) storedProcedure.getOutputParameterValue("P_OUT");
				if (claimraiseInteger == 1) {
					response.setStatus("Success");
					response.setMessage("Updated Successfully");
				} else {
					response.setStatus("error");
					response.setMessage("Something Went Wrong");
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public List<Object> getdetailshospitaldoctorprifile(String statecode, String districtcode, String hospitalcode,
			String hospitalCode2, Long userid) {
		List<Object> detialsforprofile = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_DOCTOR_PROFILE")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCTOR_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CONTACT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REGISTRATION_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DATE_OF_JOINING", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIALITY_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIALITY_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROFILE_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_speciality_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION_CODE", 4);
			storedProcedure.setParameter("P_HOSPITAL_CODE", hospitalcode.trim());
			storedProcedure.setParameter("P_DOCTOR_NAME", null);
			storedProcedure.setParameter("P_CONTACT_NO", null);
			storedProcedure.setParameter("P_REGISTRATION_NO", null);
			storedProcedure.setParameter("P_DATE_OF_JOINING", null);
			storedProcedure.setParameter("P_SPECIALITY_CODE", null);
			storedProcedure.setParameter("P_SPECIALITY_NAME", null);
			storedProcedure.setParameter("P_STATE_CODE", statecode.trim());
			storedProcedure.setParameter("P_DISTRICT_CODE", districtcode.trim());
			storedProcedure.setParameter("P_USERID", userid);
			storedProcedure.setParameter("P_PROFILE_ID", null);
			storedProcedure.setParameter("P_speciality_id", null);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				HospitalDoctorprofile hospitaldetailsDoctorprofile = new HospitalDoctorprofile();
				hospitaldetailsDoctorprofile.setProfileid(rs.getLong(1));
				hospitaldetailsDoctorprofile.setDocname(rs.getString(2));
				hospitaldetailsDoctorprofile.setContactnumber(rs.getString(3));
				hospitaldetailsDoctorprofile.setRegnumber(rs.getString(4));
				hospitaldetailsDoctorprofile.setDateofjoining(rs.getString(5));
				hospitaldetailsDoctorprofile.setHospitalCodename(rs.getString(6));
				hospitaldetailsDoctorprofile.setStatecodename(rs.getString(7));
				hospitaldetailsDoctorprofile.setDistrictcodename(rs.getString(8));
				hospitaldetailsDoctorprofile.setHospitalCodedata(rs.getString(9));
				hospitaldetailsDoctorprofile.setHospitalname(rs.getString(10));
				hospitaldetailsDoctorprofile.setStatename(rs.getString(11));
				hospitaldetailsDoctorprofile.setDistrictname(rs.getString(12));
				hospitaldetailsDoctorprofile.setSpeciality_code(rs.getString(13));
				detialsforprofile.add(hospitaldetailsDoctorprofile);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return detialsforprofile;
	}

	@Override
	public List<Object> gethospitladoctordetailsprofile(Long profileid) {
		List<Object> details = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_DOCTOR_PROFILE_DETAILS")
					.registerStoredProcedureParameter("P_PROFILE_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_PROFILE_ID", profileid);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Detailshospitalprofilebean bean = new Detailshospitalprofilebean();
				bean.setProfile_id(rs.getLong(1));
				bean.setDoctor_name(rs.getNString(2));
				bean.setContact_no(rs.getNString(3));
				bean.setRegistration_no(rs.getNString(4));
				bean.setDate_of_joining(rs.getNString(5));
				bean.setStatename(rs.getNString(6));
				bean.setDistrictname(rs.getNString(7));
				bean.setHospitalname(rs.getNString(8));
				bean.setSpecialitycode(rs.getNString(9));
				bean.setSpecalityname(rs.getNString(10));
				bean.setHospitalcode(rs.getNString(11));
				bean.setFullname(rs.getNString(12));
				bean.setCreatedon(rs.getNString(13));
				details.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details;
	}

	@Override
	public List<Object> getEditDoctorprofiledata(Long profilid) {
		List<Object> details = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_DOC_PROFILR_EDIT")
					.registerStoredProcedureParameter("P_PROFILE_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_PROFILE_ID", profilid);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				HospitalDoctorprofile hospitaldetail = new HospitalDoctorprofile();
				hospitaldetail.setProfileid(rs.getLong(1));
				hospitaldetail.setDocname(rs.getString(2));
				hospitaldetail.setContactnumber(rs.getString(3));
				hospitaldetail.setRegnumber(rs.getString(4));
				hospitaldetail.setDateofjoining(rs.getString(5));
				hospitaldetail.setHospitalCodename(rs.getString(6));
				hospitaldetail.setStatecodename(rs.getString(7));
				hospitaldetail.setDistrictcodename(rs.getString(8));
				hospitaldetail.setHospitalCodedata(rs.getString(9));
				hospitaldetail.setSpeciality_code(rs.getString(10));
				hospitaldetail.setSpeciality_name(rs.getString(11));
				hospitaldetail.setSpeciality_id(rs.getLong(12));
				details.add(hospitaldetail);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details;
	}

	@Override
	public String getclaimmultipledoctortreatedlist() {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_MULDCTR_MULLCN_TRTED")
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			while (rs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("URN", rs.getString(1) != null ? rs.getString(1) : "N/A");
				jsonObject.put("CLAIM_NO", rs.getString(2) != null ? rs.getString(2) : "N/A");
				jsonObject.put("CASENO", rs.getString(3) != null ? rs.getString(3) : "N/A");
				jsonObject.put("PATIENTNAME", rs.getString(4) != null ? rs.getString(4) : "N/A");
				jsonObject.put("PATIENTPHONENO", rs.getString(5) != null ? rs.getString(5) : "N/A");
				jsonObject.put("HOSPITALNAME", rs.getString(6) != null ? rs.getString(6) : "N/A");
				jsonObject.put("HOSPITALCODE", rs.getString(7) != null ? rs.getString(7) : "N/A");
				jsonObject.put("PACKAGECODE", rs.getString(8) != null ? rs.getString(8) : "N/A");
				jsonObject.put("PACKAGENAME", rs.getString(9) != null ? rs.getString(9) : "N/A");
				jsonObject.put("ACTUALDATEOFADMISSION", rs.getString(10));
				jsonObject.put("ACTUALDATEOFDISCHARGE", rs.getString(11));
				jsonObject.put("TOTALAMOUNTCLAIMED", rs.getString(12));
				jsonObject.put("claimid", rs.getString(13));
				jsonObject.put("TRANSACTIONDETAILSID", rs.getString(14));
				jsonObject.put("TXNPACKAGEDETAILID", rs.getString(15));
				jsonObject.put("AUTHORIZEDCODE", rs.getString(16));
				jsonObject.put("CLAIM_CASE_NO", rs.getString(17) != null ? rs.getString(17) : "N/A");
				jsonObject.put("SURGERY_DOCTORNAME", rs.getString(18) != null ? rs.getString(18) : "N/A");
				jsonObject.put("SURGERY_DOCTOR_REGNO", rs.getString(19) != null ? rs.getString(19) : "N/A");
				jsonObject.put("SURGERY_DATETIME", rs.getString(20) != null ? rs.getString(20) : "N/A");
				jsonObject.put("surgery_doctor_mobno", rs.getString(21) != null ? rs.getString(21) : "N/A");
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return jsonArray.toString();
	}

	@Override
	public List<Object> getDoctorTaggedHospitalName(String regno) {
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_DOCTOR_TAGGED_HOSPITAL")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REGISTRATION_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION_CODE", 1);
			storedProcedure.setParameter("P_REGISTRATION_NO", regno.trim());
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				MultisurgeryDoctor multi = new MultisurgeryDoctor();
				multi.setHospitalstatename(rs.getString(1));
				multi.setHospitaldistrictname(rs.getString(2));
				multi.setHospitalname(rs.getString(3));
				multi.setHospitalcode(rs.getString(4));
				list.add(multi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public List<Object> getDoctortreatedhospitalDetails(String regnonumber) {
		List<Object> details = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_DOCTOR_TAGGED_HOSPITAL")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REGISTRATION_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION_CODE", 2);
			storedProcedure.setParameter("P_REGISTRATION_NO", regnonumber.trim());
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				MultisurgeryDoctor records = new MultisurgeryDoctor();
				records.setHospitalcode(rs.getString(1));
				records.setHospitalname(rs.getString(2));
				records.setSurgerydateandtime(rs.getString(3));
				details.add(records);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return details;
	}
}
