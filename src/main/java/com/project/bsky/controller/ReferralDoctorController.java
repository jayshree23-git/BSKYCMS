/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.ReferralDoctorBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HospitalTypeMaster;
import com.project.bsky.model.ReferalDoctor;
import com.project.bsky.model.ReferralHospital;
import com.project.bsky.model.ReferralResonMst;
import com.project.bsky.service.ReferralDoctorService;

/**
 * @author rajendra.sahoo
 *
 */
@RestController
@RequestMapping(value = "/api")
public class ReferralDoctorController {
	
	@Autowired
	private ReferralDoctorService referaldocterservice;
	
	@Autowired
	private Logger logger;
	
	@ResponseBody
	@PostMapping(value = "/savereferaldoctor")
	public Response savereferaldoctor(@RequestBody ReferalDoctor bean) {
		Response response = new Response();
		try {
			response=referaldocterservice.savereferaldoctor(bean);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getreferaldoctor")
	public List<ReferalDoctor> getreferaldoctor() {
		List<ReferalDoctor> response = new ArrayList<ReferalDoctor>();
		try {
			response=referaldocterservice.getreferaldoctor();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping(value = "/updatereferaldoctor")
	public Response updatereferaldoctor(@RequestBody ReferalDoctor bean) {
		Response response = new Response();
		try {
			response=referaldocterservice.updatereferaldoctor(bean);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping(value = "/saverefdocConfiguration")
	public Response saverefdocConfiguration(@RequestBody ReferralDoctorBean referralDoctorBean) {
		Response response = new Response();
		try {
			//System.out.println(referralDoctorBean);
			response=referaldocterservice.saverefdocConfiguration(referralDoctorBean);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getdoctortaglist")
	public String getdoctortaglist() {
		String string="[]";
		try {
			string=referaldocterservice.getdoctortaglist();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return string;
	}
	
	@ResponseBody
	@PostMapping(value = "/savereferralreson")
	public Response savereferralreson(@RequestBody ReferralResonMst bean) {
		Response response = new Response();
		try {
			//System.out.println(bean);
			response=referaldocterservice.savereferralreson(bean);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getallreferralreson")
	public List<ReferralResonMst> getallreferralreson() {
		List<ReferralResonMst> response = new ArrayList<ReferralResonMst>();
		try {
			response=referaldocterservice.getallreferralreson();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping(value = "/updatereferralreson")
	public Response updatereferralreson(@RequestBody ReferralResonMst bean) {
		Response response = new Response();
		try {
			//System.out.println(bean);
			response=referaldocterservice.updatereferralreson(bean);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping(value = "/savereferalhospital")
	public Response savereferalhospital(@RequestBody ReferralHospital bean) {
		Response response = new Response();
		try {
			response=referaldocterservice.savereferalhospital(bean);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping(value = "/updatereferalhospital")
	public Response updatereferalhospital(@RequestBody ReferralHospital bean) {
		Response response = new Response();
		try {
			response=referaldocterservice.updatereferalhospital(bean);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getreferalhospitallist")
	public List<ReferralHospital> getreferalhospitallist() {
		List<ReferralHospital> response = new ArrayList<ReferralHospital>();
		try {
			response=referaldocterservice.getreferalhospitallist();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getreferralhospitaltype")
	public List<HospitalTypeMaster> getreferralhospitaltype() {
		List<HospitalTypeMaster> response = new ArrayList<HospitalTypeMaster>();
		try {
			response=referaldocterservice.getreferralhospitaltype();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getreferralhospitalbyid")
	public ReferralHospital getreferralhospitalbyid(@RequestParam(required = false, value = "hospid") Long hospitalid) {
		ReferralHospital response = new ReferralHospital();
		try {
			response=referaldocterservice.getreferralhospitalbyid(hospitalid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getrefHospitalbyDistrictId")
	public List<ReferralHospital> getrefHospitalbyDistrictId(@RequestParam(required = false, value = "dist") String distid) {
		List<ReferralHospital> response = new ArrayList<ReferralHospital>();
		try {
			response=referaldocterservice.getrefHospitalbyDistrictId(distid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	@ResponseBody
	@GetMapping(value = "/getrefHospitalbyDistrictIdblockid")
	public List<ReferralHospital> getrefHospitalbyDistrictIdblockid(@RequestParam(required = false, value = "dist") String distid,
			@RequestParam(required = false, value = "block") String block) {
		List<ReferralHospital> response = new ArrayList<ReferralHospital>();
		try {
			response=referaldocterservice.getrefHospitalbyDistrictIdblockid(distid,block);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getrefHospitalbyDistrictIdhospitaltype")
	public List<ReferralHospital> getrefHospitalbyDistrictIdhospitaltype(@RequestParam(required = false, value = "dist") String distid,
			@RequestParam(required = false, value = "hospid") Integer hosptypeid) {
		List<ReferralHospital> response = new ArrayList<ReferralHospital>();
		try {
			response=referaldocterservice.getrefHospitalbyDistrictIdhospitaltype(distid,hosptypeid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getdoctortaglistbydoctorid")
	public List<ReferralHospital> getdoctortaglistbydoctorid(@RequestParam(required = false, value = "Userid") Long Userid) {
		List<ReferralHospital> response = new ArrayList<ReferralHospital>();
		try {
			response=referaldocterservice.getdoctortaglistbydoctorid(Userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping(value = "/updatereferraldocConfiguration")
	public Response updatereferraldocConfiguration(@RequestBody ReferralDoctorBean referralDoctorBean) {
		Response response = new Response();
		try {
			//System.out.println(referralDoctorBean);			
			referaldocterservice.refdocConfigurationlog(referralDoctorBean.getCpdId(),referralDoctorBean.getUpdatedBy());
			response=referaldocterservice.updaterefdocConfiguration(referralDoctorBean);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getallhosptype")
	public List<HospitalTypeMaster> getallhosptype() {
		List<HospitalTypeMaster> response = new ArrayList<HospitalTypeMaster>();
		try {
			response=referaldocterservice.getallhosptype();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping(value = "/saverefhospitaltype")
	public Response saverefhospitaltype(@RequestBody HospitalTypeMaster hospitalTypeMaster) {
		Response response = new Response();
		try {
			//System.out.println(hospitalTypeMaster);			
			response=referaldocterservice.saverefhospitaltype(hospitalTypeMaster);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping(value = "/updaterefhospitaltype")
	public Response updaterefhospitaltype(@RequestBody HospitalTypeMaster hospitalTypeMaster) {
		Response response = new Response();
		try {
			//System.out.println(hospitalTypeMaster);			
			response=referaldocterservice.updaterefhospitaltype(hospitalTypeMaster);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

}
