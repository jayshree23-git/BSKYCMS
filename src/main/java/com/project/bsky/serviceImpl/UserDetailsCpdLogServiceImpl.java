package com.project.bsky.serviceImpl;

import java.sql.Timestamp;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.BankDetails;
import com.project.bsky.model.UserDetailsCpd;
import com.project.bsky.model.UserDetailsCpdLog;
import com.project.bsky.repository.Bankdetailsrepository;
import com.project.bsky.repository.UserDetailsCpdLogRepository;
import com.project.bsky.repository.UserDetailsCpdReposiitory;
import com.project.bsky.service.UserDetailsCpdLogService;

@Service
public class UserDetailsCpdLogServiceImpl implements UserDetailsCpdLogService {

	@Autowired
	private UserDetailsCpdLogRepository cpdRepo;

	@Autowired
	private UserDetailsCpdReposiitory userRepo;

	@Autowired
	private Bankdetailsrepository bankRepo;
	
	@Autowired
	private Logger logger;

	@Override
	public Response saveCpdLog(Long userId, Integer createdBy) {
		// TODO Auto-generated method stub
		Response response = new Response();
		UserDetailsCpdLog cpdUser = new UserDetailsCpdLog();
		try {
			UserDetailsCpd cpd = userRepo.findByuserid(userId);
			////System.out.println(cpd.toString());
			cpdUser.setBskyUserId(cpd.getBskyUserId());
			cpdUser.setFullName(cpd.getFullName());
			cpdUser.setUserName(cpd.getUserName());
			cpdUser.setMobileNo(cpd.getMobileNo());
			cpdUser.setCreatedBy(createdBy);
			cpdUser.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			////System.out.println("date: " + cpdUser.getCreatedDate());
			cpdUser.setEmailId(cpd.getEmailId());
			cpdUser.setDateofJoining(cpd.getDateofJoining());
			cpdUser.setDoctorLicenseNo(cpd.getDoctorLicenseNo());
			cpdUser.setStatus(cpd.getIsActive());
			cpdUser.setUserid(cpd.getUserid().getUserId());
			BankDetails bank = bankRepo.getByUserId(userId);
			if (bank != null) {
				cpdUser.setBankId(bank.getBankId());
				cpdUser.setBankAccNo(bank.getBankAccNo());
				cpdUser.setBankName(bank.getBankName());
				cpdUser.setBranchName(bank.getBranchName());
				cpdUser.setIfscCode(bank.getIfscCode());
				cpdUser.setPayeeName(bank.getPayeeName());
				cpdUser.setUploadPassbook(bank.getUploadPassbook());
			}
			cpdUser.setMaxClaim(cpd.getMaxClaim());
			cpdRepo.save(cpdUser);
			response.setMessage("Log Created Successfully");
			response.setStatus("Success");
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happened");
			response.setStatus("Failed");
			return response;
		}
	}

}
