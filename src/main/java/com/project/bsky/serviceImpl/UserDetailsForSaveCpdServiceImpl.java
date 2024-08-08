package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.Updatecpdbean;
import com.project.bsky.model.BankDetails;
import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsCpd;
import com.project.bsky.repository.Bankdetailsrepository;
import com.project.bsky.repository.CPDConfigurationRepository;
import com.project.bsky.repository.GroupTypeRepository;
import com.project.bsky.repository.UserDetailsCpdReposiitory;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.UserDetailsForSaveCPdService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.CpdDocUp;

@Service
public class UserDetailsForSaveCpdServiceImpl implements UserDetailsForSaveCPdService {

	private static ResourceBundle bskyResourcesBundel3 = ResourceBundle.getBundle("fileConfiguration");

	@Value("${file.path.Bankdetails}")
	private String file;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UserDetailsCpdReposiitory userdetailsforcpdrepository;

	@Autowired
	private Bankdetailsrepository bankdetailsrepo;

	@Autowired
	private CPDConfigurationRepository cpdConfig;

	@Autowired
	private GroupTypeRepository groupRepo;
	
	@Autowired
	private UserDetailsRepository userRepo;
	
	@Autowired
	private Logger logger;

	@Autowired
	private Environment env;

	@Override
	public UserDetailsCpd saveCpd(String fullName, MultipartFile form, String userName, String mobileNo, String emailId,
			String dateofJoining, String doctorLicenseNo, String payeeName, String bankAccNo, String ifscCode,
			String bankName, String branchName, String createon, Integer maxClaim) {
		String password = passwordEncoder.encode(env.getProperty("configKey"));
		UserDetails userdetails = new UserDetails();
		UserDetailsCpd userdetailsforsavecpd = new UserDetailsCpd();
		BankDetails bankdetails = new BankDetails();
		try {
			if (emailId.equalsIgnoreCase("null")) {
				emailId = null;
			}
			if (doctorLicenseNo.equalsIgnoreCase("null")) {
				doctorLicenseNo = null;
			}
			if (bankAccNo.equalsIgnoreCase("null")) {
				bankAccNo = null;
			}
			if (ifscCode.equalsIgnoreCase("null")) {
				ifscCode = null;
			}
			userName = userName.toLowerCase();
			userdetailsforsavecpd.setFullName(fullName);
			userdetailsforsavecpd.setCreatedBy(Integer.parseInt(createon));
			userdetailsforsavecpd.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			Date d = new SimpleDateFormat("dd-MMM-yyyy").parse(dateofJoining);
			String s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(d);
			userdetailsforsavecpd.setDateofJoining(Timestamp.valueOf(s));
			userdetailsforsavecpd.setDistrictId(null);
			userdetailsforsavecpd.setDistrictId(null);
			userdetailsforsavecpd.setStatecode(null);
			userdetailsforsavecpd.setDoctorLicenseNo(doctorLicenseNo);
			userdetailsforsavecpd.setEmailId(emailId);
			userdetailsforsavecpd.setHospitalId(null);
			userdetailsforsavecpd.setIsActive(0);
			userdetailsforsavecpd.setLastUpdatedBy(null);
			userdetailsforsavecpd.setLastUpdatedDate(null);
			if (mobileNo != null && mobileNo != "" && !mobileNo.equalsIgnoreCase("")
					&& !mobileNo.equalsIgnoreCase("null")) {
				userdetailsforsavecpd.setMobileNo(Long.parseLong(mobileNo));
			} else {
				userdetailsforsavecpd.setMobileNo(null);
			}
			userdetailsforsavecpd.setSpecialityId(null);
			userdetailsforsavecpd.setUserName(userName);
			userdetails.setUserName(userName);
			userdetails.setCompanyCode(null);
			userdetails.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
			userdetails.setEmail(emailId);
			GroupTypeDetails gr = new GroupTypeDetails();
			gr = groupRepo.findByTypeId(3);
			userdetails.setGroupId(gr);
			userdetails.setCreatedUserName(createon);
			userdetails.setStatus(0);
			userdetails.setPassword(password);
			userdetails.setIsOtpAllowed(1);
			if (mobileNo != null && mobileNo != "" && !mobileNo.equalsIgnoreCase("")
					&& !mobileNo.equalsIgnoreCase("null")) {
				userdetails.setPhone(Long.parseLong(mobileNo));
			} else {
				userdetails.setPhone(null);
			}
			userdetails.setTPACode(null);
			userdetails.setFullname(fullName);
			userdetailsforsavecpd.setUserid(userdetails);
			userdetailsforsavecpd.setMaxClaim(maxClaim);
			userdetailsforsavecpd = userdetailsforcpdrepository.save(userdetailsforsavecpd);
			bankdetails.setBankAccNo(bankAccNo);
			bankdetails.setBankName(bankName);
			bankdetails.setBranchName(branchName);
			bankdetails.setCreatedBY(Integer.parseInt(createon));
			bankdetails.setCreatedON(new Timestamp(System.currentTimeMillis()));
			bankdetails.setIfscCode(ifscCode);
			bankdetails.setIsActive(0);
			bankdetails.setPayeeName(payeeName);
			bankdetails.setUpdatedBy(null);
			bankdetails.setUpdatedON(null);
			String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			Map<String, String> filePath = CpdDocUp.createFileforcpdbnksave(year, form);
			for (Map.Entry<String, String> entry : filePath.entrySet()) {
				if (file.contains(entry.getKey()))
				bankdetails.setUploadPassbook(entry.getValue());
			}
			bankdetails.setUserid(userdetailsforsavecpd.getUserid());
			bankdetailsrepo.save(bankdetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			userdetailsforsavecpd = null;
		}
		return userdetailsforsavecpd;
	}

	@Override
	public Integer checkusername(String userName) {
		int count = userdetailsforcpdrepository.checkusername(userName);
		return count;
	}

	@Override
	public List<UserDetailsCpd> findAll() {
		return userdetailsforcpdrepository.findAll(Sort.by(Sort.Direction.ASC, "fullName"));
	}

	@Override
	public Integer delete(Long id) {
		try {

			UserDetailsCpd userdetailcpd = userdetailsforcpdrepository.findById(id.intValue()).get();
			UserDetails userdetails = userdetailcpd.getUserid();
			BankDetails bankdetails = bankdetailsrepo.getByid(userdetails);
			userdetails.setIsActive(0);
			userdetailcpd.setIsActive(0);
			userdetailcpd.setUserid(userdetails);
			bankdetails.setIsActive(0);
			userdetailsforcpdrepository.save(userdetailcpd);
			bankdetailsrepo.save(bankdetails);
			return 1;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}

	}

	@Override
	public Updatecpdbean getbyid(Long id) {
		UserDetailsCpd usercpd = userdetailsforcpdrepository.findById(id.intValue()).get();
		UserDetails userdetails = usercpd.getUserid();
		BankDetails bankdetails = bankdetailsrepo.getByid(userdetails);
		Updatecpdbean updatecpd = new Updatecpdbean();
		updatecpd.setUserdetails(userdetails);
		updatecpd.setBankdetails(bankdetails);
		updatecpd.setCpduserdetails(usercpd);
		return updatecpd;
	}

	@Override
	public Response UpdateCpd(String fullName, MultipartFile form, String mobileNo, String emailId, String dateofJoining, String doctorLicenseNo, 
			String payeeName, String bankAccNo, String ifscCode, String bankName, String branchName, String updateon, String cpduserid, String bankid, 
			Integer maxClaim, Integer activeStatus, Integer loginStatus) {
		Response response = new Response();
		BankDetails bankdetails = null;
		try {
			UserDetailsCpd userdetailsforsavecpd = userdetailsforcpdrepository.findById(Integer.parseInt(cpduserid)).get();
			UserDetails userdetails = userdetailsforsavecpd.getUserid();
			userdetails.setStatus(loginStatus);
			userRepo.save(userdetails);
			userdetailsforsavecpd.setFullName(fullName);
			Date d = new SimpleDateFormat("dd-MMM-yyyy").parse(dateofJoining);
			String s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(d);
			if (emailId.equalsIgnoreCase("null")) {
				emailId = null;
			}
			if (doctorLicenseNo.equalsIgnoreCase("null")) {
				doctorLicenseNo = null;
			}
			if (bankAccNo.equalsIgnoreCase("null")) {
				bankAccNo = null;
			}
			if (ifscCode.equalsIgnoreCase("null")) {
				ifscCode = null;
			}
			if(userRepo.findById(Long.parseLong(updateon)).get().getGroupId().getTypeId()==1) {
				userdetailsforsavecpd.setDateofJoining(Timestamp.valueOf(s));
			}
			userdetailsforsavecpd.setDoctorLicenseNo(doctorLicenseNo);
			userdetailsforsavecpd.setEmailId(emailId);
			userdetailsforsavecpd.setLastUpdatedBy(Integer.parseInt(updateon));
			userdetailsforsavecpd.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));
			if (mobileNo != null && mobileNo != "" && !mobileNo.equalsIgnoreCase("")
					&& !mobileNo.equalsIgnoreCase("null")) {
				userdetailsforsavecpd.setMobileNo(Long.parseLong(mobileNo));
			} else {
				userdetailsforsavecpd.setMobileNo(null);
			}
			userdetails.setEmail(emailId);
			GroupTypeDetails gr = new GroupTypeDetails();
			gr = groupRepo.findByTypeId(3);
			userdetails.setGroupId(gr);
			if (mobileNo != null && mobileNo != "" && !mobileNo.equalsIgnoreCase("")
					&& !mobileNo.equalsIgnoreCase("null")) {
				userdetails.setPhone(Long.parseLong(mobileNo));
			} else {
				userdetails.setPhone(null);
			}
			userdetails.setFullname(fullName);
			userdetailsforsavecpd.setUserid(userdetails);
			if(userRepo.findById(Long.parseLong(updateon)).get().getGroupId().getTypeId()==1) {
				userdetailsforsavecpd.setMaxClaim(maxClaim);
			}
			userdetailsforsavecpd.setIsActive(activeStatus);
			userdetailsforcpdrepository.save(userdetailsforsavecpd);
			if (bankAccNo != null && bankAccNo != "" && bankAccNo != "null" && bankAccNo != "undefined"
					&& !bankAccNo.equalsIgnoreCase("") && !bankAccNo.equalsIgnoreCase("undefined")) {
				if (bankid == null || bankid == "" || bankid == "null" || bankid == "undefined"
						|| bankid.equalsIgnoreCase("") || bankid.equalsIgnoreCase("undefined")) {
					bankdetails = bankdetailsrepo.getByUserId(userdetailsforsavecpd.getUserid().getUserId());
					if (bankdetails == null) {
						bankdetails = new BankDetails();
						bankdetails.setCreatedBY(Integer.parseInt(updateon));
						bankdetails.setCreatedON(new Timestamp(System.currentTimeMillis()));
					} else {
						bankdetails.setUpdatedBy(Integer.parseInt(updateon));
						bankdetails.setUpdatedON(new Timestamp(System.currentTimeMillis()));
					}
					bankdetails.setBankAccNo(bankAccNo);
					bankdetails.setBankName(bankName);
					bankdetails.setBranchName(branchName);
					bankdetails.setIfscCode(ifscCode);
					bankdetails.setIsActive(0);
					bankdetails.setPayeeName(payeeName);
					bankdetails.setUpdatedBy(null);
					bankdetails.setUpdatedON(null);
					String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Map<String, String> filePath = CpdDocUp.createFileforcpdbnksave(year, form);
					for (Map.Entry<String, String> entry : filePath.entrySet()) {
						if (file.contains(entry.getKey()))
						bankdetails.setUploadPassbook(entry.getValue());
					}
					bankdetails.setUserid(userdetailsforsavecpd.getUserid());
					bankdetailsrepo.save(bankdetails);
				} else {
					bankdetails = bankdetailsrepo.findById(Integer.parseInt(bankid)).get();
					bankdetails.setBankAccNo(bankAccNo);
					bankdetails.setBankName(bankName);
					bankdetails.setBranchName(branchName);
					bankdetails.setIfscCode(ifscCode);
					bankdetails.setPayeeName(payeeName);
					bankdetails.setUpdatedBy(Integer.parseInt(updateon));
					bankdetails.setUpdatedON(new Timestamp(System.currentTimeMillis()));
					if (form == null) {
					} else {
						String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
						Map<String, String> filePath = CpdDocUp.createFileforcpdbnksave(year, form);
						for (Map.Entry<String, String> entry : filePath.entrySet()) {
							if (file.contains(entry.getKey()))
							bankdetails.setUploadPassbook(entry.getValue());
						}
					}
					bankdetailsrepo.save(bankdetails);
				}
			}
			response.setMessage("CPD Updated Successfully");
			response.setStatus("Success");
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
			return response;
		}
	}

	@Override
	public void downLoadPassbook(String fileName, String year, HttpServletResponse response) {
		String folderName = null;
		try {
			folderName = bskyResourcesBundel3.getString("folder.Bankdetails");
			CommonFileUpload.downLoadPassbook(fileName, year, response, folderName);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public Integer checklicense(String doctorLicenseNo, String userName) {
		int count = userdetailsforcpdrepository.checklicense(doctorLicenseNo, userName);
		return count;
	}

	@Override
	public Updatecpdbean getByUserId(long id) {
		UserDetailsCpd usercpd = userdetailsforcpdrepository.findByuserid(id);
		UserDetails userdetails = usercpd.getUserid();
		BankDetails bankdetails = bankdetailsrepo.getByid(userdetails);
		Updatecpdbean updatecpd = new Updatecpdbean();
		updatecpd.setBankdetails(bankdetails);
		updatecpd.setCpduserdetails(usercpd);
		return updatecpd;
	}

	public List<UserDetailsCpd> getCpdNameForLeave() {

		return userdetailsforcpdrepository.getCpdName();
	}

	@Override
	public ResponseEntity<Response> checkCPDStatus(String username, Response response) {
		try {
			Integer bskyUserId = userdetailsforcpdrepository.findByUserName(username).get().getBskyUserId();
			int count = cpdConfig.checkCpdNameDulicacy(bskyUserId);
			if (count > 0) {
				response.setMessage("CPD restricted to Hospital, cannot be made Inactive!!");
				response.setStatus("Info");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happened");
			response.setStatus("Error");
		}
		return ResponseEntity.ok(response);
	}

	@Override
	public List<UserDetailsCpd> findAllCpd(Date fromdate, Date toDate, Integer status) throws Exception {
		List<UserDetailsCpd> list=new ArrayList<>();
		try {
			if(status!=null) {
				list=userdetailsforcpdrepository.findAllCpdbystatus(fromdate, toDate,status);
			}else {
				list=userdetailsforcpdrepository.findAllCpd(fromdate, toDate);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

}
