package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.BankIfsc;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.Updatecpdbean;
import com.project.bsky.model.DistrictDetails;
import com.project.bsky.model.GroupDetails;
import com.project.bsky.model.HospitaDetails;
import com.project.bsky.model.HospitalProfile;
import com.project.bsky.model.Hospitalpwd;
import com.project.bsky.model.Specialitydoctor;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsCpd;
import com.project.bsky.repository.DistrictDetailsRepo;
import com.project.bsky.repository.GroupDetailsRepo;
import com.project.bsky.repository.HopitalpwdRepository;
import com.project.bsky.repository.HospitaDetailsRepo;
import com.project.bsky.repository.HospitalProfileRepository;
import com.project.bsky.repository.SpecialityRepo;
import com.project.bsky.repository.UserDetailsCpdReposiitory;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.AdminConsoleService;
import com.project.bsky.service.GroupDetailsService;
import com.project.bsky.service.UserDetailsCpdLogService;
import com.project.bsky.service.UserDetailsForSaveCPdService;
import com.project.bsky.service.UserLoggingsuccessService;

/**
 * @author Rajendra.sahoo
 *
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/api")
public class ClaimProcessDoctorsController {

	@Autowired
	private HopitalpwdRepository hospiHopitalpwdRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepo;

	@Autowired
	private UserLoggingsuccessService userLoggingsuccessService;

	@Autowired
	private GroupDetailsRepo groupDetailsRepo;

	@Autowired
	private SpecialityRepo specialityRepo;

	@Autowired
	private HospitaDetailsRepo hospitaDetailsRepo;

	@Autowired
	private HospitalProfileRepository hospitalProfileRepo;

	@Autowired
	private UserDetailsForSaveCPdService userDetailsforSaveCpdService;

	@Autowired
	private GroupDetailsService groupDetailsService;

	@Autowired
	private DistrictDetailsRepo districtdetailsrepo;

	@Autowired
	private UserDetailsCpdReposiitory userdetailsforcpdrepository;

	@Autowired
	private UserDetailsCpdLogService userCpdService;

	@Autowired
	private AdminConsoleService adminService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getHospitalpwdData")
	public String getHospitalpwdData() {
		List<Hospitalpwd> hospitalpwdList = hospiHopitalpwdRepository.findAll();
		return hospitalpwdList.toString();
	}

	@ResponseBody
	@GetMapping(value = "/validateIFSC/{ifscCode}")
	public ResponseEntity<BankIfsc> getBankUsingIFSCCode(@PathVariable("ifscCode") String ifscCode, BankIfsc bankIfsc)
			throws JSONException {
		try {
			JSONObject jsonObject2 = new JSONObject(new RestTemplate()
					.getForObject("https://ifsc.razorpay.com/" + ifscCode.toUpperCase(), String.class));
			bankIfsc.setBank(jsonObject2.getString("BANK"));
			bankIfsc.setBranch(jsonObject2.getString("BRANCH"));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			bankIfsc.setBank("");
			bankIfsc.setBranch("");
		}
		return ResponseEntity.ok(bankIfsc);
	}

	@ResponseBody
	@GetMapping(value = "/getUserDetails")
	public String getUserDetails() {
		List<UserDetails> userDetails = userDetailsRepo.findAll();
		return userDetails.toString();

	}

	@ResponseBody
	@GetMapping(value = "/getGroupDetails")
	public List<GroupDetails> getGroupDetails() {
		List<GroupDetails> groupDetails = groupDetailsRepo.findAllGroupDetails();
		return groupDetails;
	}

	@ResponseBody
	@GetMapping(value = "/getSpecialization")
	public List<Specialitydoctor> getSpecialization() {
		List<Specialitydoctor> specialitydoctoras = specialityRepo.findAll();
		return specialitydoctoras;
	}

	@ResponseBody
	@GetMapping(value = "/getDetailsDistrict")
	public List<DistrictDetails> getDetailsDistrict() {
		List<DistrictDetails> sDetails = districtdetailsrepo.findAll();
		return sDetails;
	}

	@ResponseBody
	@GetMapping(value = "/getHospitaDetails")
	public List<HospitaDetails> getHospitalDetails() {
		List<HospitaDetails> hospitaDetails = hospitaDetailsRepo.findAll();
		hospitaDetails.removeAll(Collections.singleton(null));
		for (HospitaDetails hospitaDetails2 : hospitaDetails) {
		}
		return hospitaDetails;
	}

	@ResponseBody
	@GetMapping(value = "/getHospitaDetailsForHospitaName")
	public List<HospitalProfile> getHospitalDetaisForHospitaName() {
		List<HospitalProfile> hospitaDetails = hospitalProfileRepo.findAll();
		for (HospitalProfile hsopDetailsfHospitalName2 : hospitaDetails) {
		}
		return hospitaDetails;
	}

	@ResponseBody
	@PostMapping(value = "/saveGroup")
	public ResponseEntity<List<GroupDetails>> submitgroupData(@RequestParam(value = "groupName") String groupName,
			@RequestParam(value = "isSubgrouped") Integer isSubgrouped,
			@RequestParam(value = "parentGroupId") String parentGroupId) throws Exception {
		List<GroupDetails> groupDetailsList = null;
		try {
			Integer count = groupDetailsService.getGroupDetails(groupName, isSubgrouped, parentGroupId);
			groupDetailsList = groupDetailsRepo.findAll();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(groupDetailsList);
	}

	@ResponseBody
	@PostMapping("/UpdateGroup")
	public Integer updategroup(@RequestBody GroupDetails groupDetails) {
		if (groupDetails.getIsSubgrouped() == 0) {
			groupDetails.setParentGroupId(null);
		}
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		groupDetails.setLastUpdateDate(date);
		groupDetailsRepo.save(groupDetails);
		return 1;
	}

	@ResponseBody
	@PostMapping("/DeleteGroup")
	public Integer deletegroup(@RequestBody GroupDetails groupDetails) {
		groupDetailsRepo.delete(groupDetails);
		return 1;
	}

	@ResponseBody
	@PostMapping(value = "/saveCpdUserData")
	public ResponseEntity<Response> saveCpdUserData(@RequestParam(required = false, value = "fullName") String fullName,
			@RequestParam(required = false, value = "userName") String userName,
			@RequestParam(required = false, value = "mobileNo") String mobileNo,
			@RequestParam(required = false, value = "emailId") String emailId,
			@RequestParam(required = false, value = "dateofJoining") String dateofJoining,
			@RequestParam(required = false, value = "doctorLicenseNo") String doctorLicenseNo,
			@RequestParam(required = false, value = "payeeName") String payeeName,
			@RequestParam(required = false, value = "bankAccNo") String bankAccNo,
			@RequestParam(required = false, value = "ifscCode") String ifscCode,
			@RequestParam(required = false, value = "bankName") String bankName,
			@RequestParam(required = false, value = "branchName") String branchName,
			@RequestParam(required = false, value = "file") MultipartFile form,
			@RequestParam(required = false, value = "createon") String createon,
			@RequestParam(required = false, value = "maxClaim") Integer maxClaim, Response response) {
		Integer checkusername = userDetailsforSaveCpdService.checkusername(userName);
		Integer checklicense = userDetailsforSaveCpdService.checklicense(doctorLicenseNo, userName);
		try {
			if (checkusername == 0 && checklicense == 0) {
				UserDetailsCpd cpd = userDetailsforSaveCpdService.saveCpd(fullName, form, userName, mobileNo, emailId,
						dateofJoining, doctorLicenseNo, payeeName, bankAccNo, ifscCode.toUpperCase(), bankName,
						branchName, createon, maxClaim);
				if (cpd != null) {
					int userId = cpd.getUserid().getUserId().intValue();
					adminService.copyPrimaryLinksForCPD(userId, Integer.parseInt(createon));
					response.setMessage("CPD Created Successfully");
					response.setStatus("Success");
				} else {
					response.setMessage("Some error happen");
					response.setStatus("Failed");
				}

			} else if (checkusername != 0) {
				response.setMessage("UserName Already Exist");
				response.setStatus("Failed");
			} else if (checklicense != 0) {
				response.setMessage("LicenseNo Already Exist");
				response.setStatus("Failed");
			} else {
				response.setMessage("Some error happen");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/saveCpdLogData")
	public ResponseEntity<Response> saveCpdLogData(@RequestParam("userId") Long userId,
			@RequestParam("createdBy") Integer createdBy, Response response) {
		try {
			response = userCpdService.saveCpdLog(userId, createdBy);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getHospitalCode")
	public String getHospitalCode(@RequestParam("userId") Integer userId) {
		HospitalProfile hospitalProfile = userLoggingsuccessService.getHospitalCode(userId);
		return hospitalProfile.getHospitalCode();
	}

	@SuppressWarnings("deprecation")
	@Transactional
	@GetMapping(value = "/getallcpd")
	public List<UserDetailsCpd> getallcpd(@RequestParam(value = "fromDate", required = false) String fromdate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "status", required = false) Integer status) throws Exception {
		List<UserDetailsCpd> details=new ArrayList<>();
		try {			
			if (fromdate.equalsIgnoreCase("null") || fromdate.equalsIgnoreCase("undefined")) {
				fromdate = null;
			}
			if (toDate.equalsIgnoreCase("null") || toDate.equalsIgnoreCase("undefined")) {
				toDate = null;
			}
			Date newDateFrom = fromdate != null ? new Date(fromdate) : new Date(0);
			Date newDateTo = toDate != null ? new Date(toDate) : new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(newDateTo);
			c.add(Calendar.DATE, 1);
			newDateTo = c.getTime();
			details = userDetailsforSaveCpdService.findAllCpd(newDateFrom, newDateTo,status);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return details;
	}

	@GetMapping(value = "/deletecpduser")
	public Integer deletecpd(@RequestParam("id") Long Id) {

		Integer i = userDetailsforSaveCpdService.delete(Id);
		return i;
	}

	@GetMapping(value = "/getbycpdid")
	public Updatecpdbean getbyid(@RequestParam("id") Long Id) {
		Updatecpdbean user = userDetailsforSaveCpdService.getbyid(Id);
		Timestamp doj = user.getCpduserdetails().getDateofJoining();
		UserDetailsCpd uds = user.getCpduserdetails();
		uds.setDateofJoining(doj);
		user.setCpduserdetails(uds);
		return user;
	}

	@GetMapping(value = "/getbycpduserid")
	public Updatecpdbean getbyUserId(@RequestParam("id") Long Id) {
		Updatecpdbean user = userDetailsforSaveCpdService.getByUserId(Id);
		Timestamp doj = user.getCpduserdetails().getDateofJoining();
		UserDetailsCpd uds = user.getCpduserdetails();
		uds.setDateofJoining(doj);
		user.setCpduserdetails(uds);
		return user;
	}

	@ResponseBody
	@PostMapping(value = "/updateCpdUserData")
	public ResponseEntity<Response> updateeCpdUserData(
			@RequestParam(required = false, value = "fullName") String fullName,
			@RequestParam(required = false, value = "userName") String userName,
			@RequestParam(required = false, value = "mobileNo") String mobileNo,
			@RequestParam(required = false, value = "emailId") String emailId,
			@RequestParam(required = false, value = "dateofJoining") String dateofJoining,
			@RequestParam(required = false, value = "doctorLicenseNo") String doctorLicenseNo,
			@RequestParam(required = false, value = "payeeName") String payeeName,
			@RequestParam(required = false, value = "bankAccNo") String bankAccNo,
			@RequestParam(required = false, value = "ifscCode") String ifscCode,
			@RequestParam(required = false, value = "bankName") String bankName,
			@RequestParam(required = false, value = "branchName") String branchName,
			@RequestParam(required = false, value = "file") MultipartFile form,
			@RequestParam(required = false, value = "Update") String updateon,
			@RequestParam(required = false, value = "cpduserid") String cpduserid,
			@RequestParam(required = false, value = "bankid") String bankid,
			@RequestParam(required = false, value = "maxClaim") Integer maxClaim,
			@RequestParam(value = "activeStatus", required = false) Integer activeStatus,
			@RequestParam(value = "loginStatus", required = false) Integer loginStatus, Response response) {
		Integer checklicense = userDetailsforSaveCpdService.checklicense(doctorLicenseNo.trim(), userName);
		try {
			if (checklicense == 0) {
				response = userDetailsforSaveCpdService.UpdateCpd(fullName, form, mobileNo, emailId, dateofJoining,
						doctorLicenseNo.trim(), payeeName, bankAccNo, ifscCode.toUpperCase(), bankName, branchName,
						updateon, cpduserid, bankid, maxClaim, activeStatus, loginStatus);
			} else {
				UserDetailsCpd userdetailsforsavecpd1 = userdetailsforcpdrepository.findname1(doctorLicenseNo);
				if (userdetailsforsavecpd1.getBskyUserId() == Integer.parseInt(cpduserid)
						&& userdetailsforsavecpd1.getDoctorLicenseNo().equals(doctorLicenseNo.trim())) {
					response = userDetailsforSaveCpdService.UpdateCpd(fullName, form, mobileNo, emailId, dateofJoining,
							doctorLicenseNo.trim(), payeeName, bankAccNo, ifscCode.toUpperCase(), bankName, branchName,
							updateon, cpduserid, bankid, maxClaim, activeStatus, loginStatus);
				} else {
					response.setMessage("LicenseNo Already Exist");
					response.setStatus("Failed");
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/downLoadActionforcpd")
	public String commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				resp = "Passbook not found";
			} else {
				String year = fileName.substring(4, 8);
				userDetailsforSaveCpdService.downLoadPassbook(fileName, year, response);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}

	@PostMapping("/checkCPDStatus")
	public ResponseEntity<Response> checkCPDStatus(@RequestBody String username, Response response) {
		return userDetailsforSaveCpdService.checkCPDStatus(username, response);
	}

}
