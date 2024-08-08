package com.project.bsky.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.Updatecpdbean;
import com.project.bsky.model.UserDetailsCpd;

public interface UserDetailsForSaveCPdService {

	UserDetailsCpd saveCpd(String fullName, MultipartFile form, String userName, String mobileNo, String emailId,
			String dateofJoining, String doctorLicenseNo, String payeeName, String bankAccNo, String ifscCode,
			String bankName, String branchName, String createon, Integer maxClaim);

	Integer checkusername(String userName);

	List<UserDetailsCpd> findAll();

	List<UserDetailsCpd> findAllCpd(Date fromdate, Date toDate, Integer status) throws Exception;

	Integer delete(Long id);

	Updatecpdbean getbyid(Long id);

	Updatecpdbean getByUserId(long id);

	Response UpdateCpd(String fullName, MultipartFile form, String mobileNo, String emailId, String dateofJoining,
			String doctorLicenseNo, String payeeName, String bankAccNo, String ifscCode, String bankName,
			String branchName, String updateon, String cpduserid, String bankid, Integer maxClaim, Integer activeStatus,
			Integer loginStatus);

	Integer checklicense(String doctorLicenseNo, String checklicense);

	void downLoadPassbook(String fileName, String year, HttpServletResponse response);

	List<UserDetailsCpd> getCpdNameForLeave();

	ResponseEntity<Response> checkCPDStatus(String username, Response response);

}
