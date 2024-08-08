/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.BankMasterBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.BankMaster;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.BankMasterRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.BankMasterService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class BankMasterServiceImpl implements BankMasterService {

	@Autowired
	private BankMasterRepository bankMasterRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepo;

	@Autowired
	private Logger logger;

	@Override
	public Response saveBankMasterDetails(BankMasterBean bankMasterBean) {

		Response response = new Response();
		try {

			BankMaster bankMaster = new BankMaster();
			Integer count = bankMasterRepository.countRowForCheckDuplicateType(bankMasterBean.getBankName());
			if (count == 0) {
				UserDetails userdetails = userDetailsRepo.findById(Long.parseLong(bankMasterBean.getCreatedBy())).get();
				bankMaster.setBankName(bankMasterBean.getBankName());
				Calendar calendar = Calendar.getInstance();
				bankMaster.setCreatedOn((calendar.getTime()));
				bankMaster.setCreatedBy(userdetails);
				bankMaster.setStatusFlag(0);
				bankMaster.setUpdatedOn(null);
				bankMaster.setUpdatedBy(null);
				bankMasterRepository.save(bankMaster);
				response.setStatus("Success");
				response.setMessage("BankMaster Successfully Inserted");
			} else {
				response.setStatus("Failed");
				response.setMessage("BankMaster Already Exist");
			}

			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage("Something went wrong");
			return response;
		}

	}

	@Override
	public List<BankMaster> getBankDetails() {
		JSONArray jsonArray = new JSONArray();
		List<BankMaster> getBankData = null;
		try {
			getBankData = bankMasterRepository.findDetails();
			for (BankMaster bank : getBankData) {
				JSONObject json = new JSONObject();
				json.put("bankId", bank.getBankId());
				json.put("bankName", bank.getBankName());
				json.put("statusFlag", bank.getStatusFlag());
				json.put("createdBy", bank.getCreatedBy());
				json.put("createdOn", bank.getCreatedOn());
				json.put("updatedBy", bank.getUpdatedBy());
				json.put("updatedOn", bank.getUpdatedOn());
				jsonArray.put(json);
			}
		} catch (JSONException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getBankData;
	}

	@Override
	public BankMaster getBankById(Integer bankId) {
		try {
			return bankMasterRepository.findById(bankId).get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Response updateBankMaster(BankMasterBean bankMasterBean) {
		Response response = new Response();
		try {
			BankMaster bankMaster = bankMasterRepository.findById(bankMasterBean.getBankId()).get();
			UserDetails userdetails = userDetailsRepo.findById(Long.parseLong(bankMasterBean.getUpdatedBy())).get();
			Integer count = bankMasterRepository.countRowForCheckDuplicateType(bankMasterBean.getBankName());
			if (count == 0) {

				bankMaster.setBankName(bankMasterBean.getBankName());
				Calendar calendar = Calendar.getInstance();
				bankMaster.setUpdatedBy(userdetails);
				bankMaster.setStatusFlag(bankMasterBean.getStatusFlag());
				bankMaster.setUpdatedOn(calendar.getTime());
				bankMasterRepository.save(bankMaster);
				response.setStatus("Success");
				response.setMessage("BankMaster Sunncessfully Updated");
			} else if (bankMaster.getBankName().equals(bankMasterBean.getBankName())
					&& bankMaster.getBankId() == bankMasterBean.getBankId()) {
				bankMaster.setBankName(bankMasterBean.getBankName());
				bankMaster.setStatusFlag(bankMasterBean.getStatusFlag());
				Calendar calendar = Calendar.getInstance();
				bankMaster.setUpdatedBy(userdetails);
				bankMaster.setUpdatedOn(calendar.getTime());
				bankMasterRepository.save(bankMaster);
				response.setStatus("Success");
				response.setMessage("BankMaster Successfully Updated");
			} else {
				response.setStatus("Failed");
				response.setMessage("BankMaster Already Exist");
			}
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return response;
	}

}
