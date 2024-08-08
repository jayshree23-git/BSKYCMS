/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.DCConfigurationBean;
import com.project.bsky.bean.HospObj;
import com.project.bsky.bean.Hospital;
import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.HospitalInfoBean;
import com.project.bsky.bean.HospitalUserSaveBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SNOConfigurationBean;
import com.project.bsky.model.HospitalCategoryMaster;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.SNOConfiguration;
import com.project.bsky.service.AdminConsoleService;
import com.project.bsky.service.HospitalCategoryService;
import com.project.bsky.service.HospitalLogSaveService;
import com.project.bsky.service.HospitalUserSaveService;
import com.project.bsky.service.SNOConfigurationService;
import com.project.bsky.service.UserDetailsService;

@RestController
@RequestMapping(value = "/api")
public class HospitalUserSaveController {

	@Autowired
	private HospitalUserSaveService hospitalUserSaveService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private HospitalLogSaveService logService;

	@Autowired
	private SNOConfigurationService snoConfigService;

	@Autowired
	private AdminConsoleService adminService;

	@Autowired
	private HospitalCategoryService categoryService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/saveHospitalData")
	public Integer addHospital(@RequestBody HospitalUserSaveBean hospitalUserSaveBean) {
		SNOConfigurationBean snoBean = new SNOConfigurationBean();
		List<HospObj> hospital_List = new ArrayList<HospObj>();
		try {
			HospitalInformation hosp = hospitalUserSaveService.saveDetails(hospitalUserSaveBean);
			snoBean.setSnoId(hospitalUserSaveBean.getSnoUserId().intValue());
			snoBean.setCreatedBy(hospitalUserSaveBean.getCreatedBy());
			HospObj h = new HospObj();
			h.setHospitalCode(hospitalUserSaveBean.getHospitalCode());
			h.setHospitalName(hospitalUserSaveBean.getHospitalName());
			h.setStateCode(hospitalUserSaveBean.getStateId());
			h.setDistrictCode(hospitalUserSaveBean.getDistrictId());
			hospital_List.add(h);
			snoBean.setHospList(hospital_List);
			snoConfigService.saveSNOConfiguration(snoBean);
			if (hosp != null) {
				int userId = hosp.getUserId().getUserId().intValue();
				adminService.copyPrimaryLinksForHosp(userId, hospitalUserSaveBean.getCreatedBy());
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@GetMapping(value = "/saveHospLogData")
	public ResponseEntity<Response> saveHospLogData(@RequestParam("userId") Long userId,
			@RequestParam("createdBy") Integer createdBy, Response response) {
		try {
			response = logService.saveHospLog(userId, createdBy);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getlisthospital")
	public List<HospitalInformation> getHospitalUser() {
		List<HospitalInformation> hospitalInformation = null;
		try {
			hospitalInformation = hospitalUserSaveService.getDetails();
			return hospitalInformation;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalInformation;

	}

	@GetMapping(value = "/getHospitalDetails")
	public String getHospitalDetails(@RequestParam(value = "hospitalId", required = false) Integer hospitalId) {
		return hospitalUserSaveService.getHospDetails(hospitalId).toString();
	}

	@GetMapping(value = "/getlisthospitals")
	public List<HospitalBean> getHospitals(@RequestParam(value = "stateId", required = false) String stateId,
			@RequestParam(value = "districtId", required = false) String districtId,
			@RequestParam(value = "cpdApprovalRequired", required = false) String cpdApprovalRequired,
			@RequestParam(value = "snoTagged", required = false) String snoTagged,
			@RequestParam(value = "categoryId", required = false) String categoryId,
			@RequestParam(value = "tmsActive", required = false) Integer tmsActive) {
		List<HospitalBean> hospitalInformation = null;
		try {
			hospitalInformation = hospitalUserSaveService.getHospitals(stateId, districtId, cpdApprovalRequired,
					snoTagged, categoryId, tmsActive);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalInformation;
	}

	@ResponseBody
	@GetMapping(value = "/checkDuplicateHspCode")
	public ResponseEntity<Response> checkDuplicateHospitalCode(
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode, Response response) {
		try {
			Integer hospitalIdInteger = hospitalUserSaveService.checkHospitalByCode(hospitalCode);
			Integer userIdInteger = userDetailsService.checkUserDetailsByuserNameForHosp(hospitalCode);
			if ((hospitalIdInteger != 0) || (userIdInteger != 0))
				response.setStatus("Present");
			else
				response.setStatus("Absent");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@GetMapping(value = "/getDeleteHospital")
	public Integer deleteById(@RequestParam(value = "hospitalId", required = false) Integer hospitalId) {
		try {
			hospitalUserSaveService.deleteById(hospitalId);
			return 1;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return 1;
	}

	@ResponseBody
	@GetMapping(value = "/getHospitalIdForUpdate")
	public HospitalInfoBean hospitalgetById(@RequestParam(value = "hospitalId", required = false) Integer hospitalId) {
		HospitalInfoBean hospital = new HospitalInfoBean();
		SNOConfiguration conf = new SNOConfiguration();
		HospitalInformation hospitalUserSave = new HospitalInformation();
		try {
			hospitalUserSave = hospitalUserSaveService.getbyid(hospitalId);
			hospital.setHospital(hospitalUserSave);
			String hospitalCode = hospitalUserSave.getHospitalCode();
			conf = snoConfigService.getSnoConfFromHospCode(hospitalCode);
			hospital.setSno(conf);
			if (conf != null) {
				String snoName = userDetailsService.getbyid(Long.valueOf(conf.getSnoUserId())).getUserDetails()
						.getFullname();
				hospital.setSnoName(snoName);
			}
			if (hospitalUserSave.getAssigned_dc() != null) {
				String dcName = userDetailsService.getbyid(hospitalUserSave.getAssigned_dc()).getUserDetails()
						.getFullname();
				hospital.setDcName(dcName);
			}
			if (hospitalUserSave.getHospitalCategoryid() != null) {
				HospitalCategoryMaster category = categoryService
						.findCategoryNameById(Long.valueOf(hospitalUserSave.getHospitalCategoryid()));
				hospital.setCategoryName(category.getHospitalCategoryName());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospital;
	}

	@ResponseBody
	@GetMapping(value = "/getHospitalUser")
	public HospitalInformation hospitalgetByUserId(@RequestParam(value = "userId", required = false) Long userId) {
		HospitalInformation hospitalUserSave = hospitalUserSaveService.getbyuserid(userId);
		return hospitalUserSave;
	}

	@ResponseBody
	@GetMapping(value = "/getHospital")
	public HospitalInfoBean getByUserId(@RequestParam(value = "userId", required = false) Long userId) {
		HospitalInfoBean hospitalUserSave = new HospitalInfoBean();
		HospitalInformation h = hospitalUserSaveService.getbyuserid(userId);
		hospitalUserSave.setHospital(h);
		SNOConfiguration conf = snoConfigService.getSnoConfFromHospCode(h.getHospitalCode());
		if (conf != null) {
			String snoName = userDetailsService.getbyid(Long.valueOf(conf.getSnoUserId())).getUserDetails()
					.getFullname();
			hospitalUserSave.setSnoName(snoName);
			hospitalUserSave.setSno(conf);
		}
		if (h.getAssigned_dc() != null) {
			String dcName = userDetailsService.getbyid(h.getAssigned_dc()).getUserDetails().getFullname();
			hospitalUserSave.setDcName(dcName);
		}
		if (h.getHospitalCategoryid() != null) {
			hospitalUserSave.setCategoryName(categoryService
					.findCategoryNameById(Long.valueOf(h.getHospitalCategoryid())).getHospitalCategoryName());
		}
		return hospitalUserSave;

	}

	@ResponseBody
	@PostMapping(value = "/updateHospitalData")
	public Integer Updategroup(@RequestBody HospitalUserSaveBean hospitalUserSaveBean) {
		SNOConfigurationBean snoBean = new SNOConfigurationBean();
		List<HospObj> hospital_List = new ArrayList<HospObj>();
		try {
			hospitalUserSaveService.update(hospitalUserSaveBean);
			if (hospitalUserSaveBean.getSnoMappingId() == null) {
				snoBean.setSnoId(hospitalUserSaveBean.getSnoUserId().intValue());
				snoBean.setCreatedBy(hospitalUserSaveBean.getUpdatedBy());
				HospObj h = new HospObj();
				h.setHospitalCode(hospitalUserSaveBean.getHospitalCode());
				h.setHospitalName(hospitalUserSaveBean.getHospitalName());
				h.setStateCode(hospitalUserSaveBean.getStateId());
				h.setDistrictCode(hospitalUserSaveBean.getDistrictId());
				hospital_List.add(h);
				snoBean.setHospList(hospital_List);
				snoConfigService.saveSNOConfiguration(snoBean);
			} else {
				snoBean.setSnoId(hospitalUserSaveBean.getSnoUserId().intValue());
				snoBean.setUpdatedBy(hospitalUserSaveBean.getUpdatedBy());
				HospObj h = new HospObj();
				h.setHospitalCode(hospitalUserSaveBean.getHospitalCode());
				h.setHospitalName(hospitalUserSaveBean.getHospitalName());
				h.setStateCode(hospitalUserSaveBean.getStateId());
				h.setDistrictCode(hospitalUserSaveBean.getDistrictId());
				hospital_List.add(h);
				snoBean.setHospList(hospital_List);
				snoConfigService.updateSnoDetailsDataById(snoBean, hospitalUserSaveBean.getSnoMappingId());
			}
			return 1;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@ResponseBody
	@PostMapping(value = "/updateHospitalProfile")
	public Integer updateHospitalProfile(@RequestBody HospitalUserSaveBean hospitalUserSaveBean) {
		try {
			return hospitalUserSaveService.updateProfile(hospitalUserSaveBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@PostMapping("/saveDCConfiguration")
	public ResponseEntity<Response> saveDCConfiguration(@RequestBody DCConfigurationBean bean, Response response) {
		try {
			response = hospitalUserSaveService.saveDCConfiguration(bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happened");
			response.setStatus("Failed");
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/saveDCConfigurationLog")
	public ResponseEntity<Response> saveDCConfigurationLog(@RequestParam(required = false, value = "dcId") Long dcId,
			@RequestParam(required = false, value = "createdBy") Integer createdBy, Response response,
			HttpServletRequest request) {
		try {
			String remoteAddr = request.getRemoteAddr();
			response = logService.saveDcConfigurationLog(dcId, createdBy, remoteAddr);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
			response.setMessage("Some error happened");
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/updateDCConfiguration")
	public ResponseEntity<Response> updateDCConfiguration(@RequestBody DCConfigurationBean bean, Response response) {
		try {
			response = hospitalUserSaveService.updateDCConfiguration(bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getDcDetailsFilteredByDistrict")
	public List<DCConfigurationBean> getDcDetailsFilteredByDistrict(
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId) {
		return hospitalUserSaveService.getAllDcConfigurationDetails(stateId, districtId);
	}

	@GetMapping(value = "/getDCById")
	public DCConfigurationBean getConfigurationDetailsById(
			@RequestParam(required = false, value = "dcUserId") Long dcUserId) {
		return hospitalUserSaveService.getDcConfigurationDetailsById(dcUserId);
	}

	@PostMapping("/checkDCAssignedToHosp")
	public ResponseEntity<Response> checkDCAssignedToHosp(@RequestBody DCConfigurationBean bean, Response response) {
		boolean dchospiltalDuplicacy = false;
		String hospname = null;
		try {
			if (bean.getHospList() == null || bean.getHospList().size() == 0) {
				response.setMessage("Please select hospital");
				response.setStatus("Info");
			} else {
				Integer checkHospital = 0;
				for (HospObj h : bean.getHospList()) {
					checkHospital = hospitalUserSaveService.checkDCHospitalName(h.getHospitalCode(), bean.getDcId());
					if (checkHospital > 0) {
						hospname = h.getHospitalName();
						dchospiltalDuplicacy = true;
						break;
					}
				}
				if (dchospiltalDuplicacy == true) {
					response.setMessage("DC already assigned to " + hospname);
					response.setStatus("Info");
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/checkDCAssignedToHospForUpdate")
	public ResponseEntity<Response> checkDCAssignedToHospForUpdate(@RequestBody DCConfigurationBean bean,
			Response response) {
		boolean dchospiltalDuplicacy = false;
		String hospname = null;
		try {
			Integer checkHospital = 0;
			for (HospObj h : bean.getHospList()) {
				checkHospital = hospitalUserSaveService.checkDCHospitalName(h.getHospitalCode(), bean.getDcId());
				if (checkHospital > 0) {
					hospname = h.getHospitalName();
					dchospiltalDuplicacy = true;
					break;
				}
			}
			if (dchospiltalDuplicacy == true) {
				response.setMessage("DC already assigned to " + hospname);
				response.setStatus("Info");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/checkOtherDCAssignedToHosp")
	public ResponseEntity<Response> checkOtherDCAssignedToHosp(@RequestBody DCConfigurationBean bean,
			Response response) {
		boolean dchospiltalDuplicacy = false;
		if (bean.getHospitalCode() == null || bean.getHospitalCode().size() == 0) {
			response.setMessage("Please select hospital");
			response.setStatus("Error");
		} else {
			Integer checkHospital = 0;
			Hospital h = null;
			for (int i = 0; i < bean.getHospitalCode().size(); i++) {
				h = bean.getHospitalCode().get(i);
				checkHospital = hospitalUserSaveService.checkHospitalNameForOther(h.getHospitalCode(), bean.getDcId());
				if (checkHospital > 0) {
					dchospiltalDuplicacy = true;
					break;
				}
			}
			try {
				if (dchospiltalDuplicacy == true) {
					response.setMessage("DC already assigned to " + h.getHospitalName());
					response.setStatus("Info");
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getDistinctDC")
	public String getDistinctDC(@RequestParam(required = false, value = "userId") Long userId) {
		return hospitalUserSaveService.getDistinctDC(userId).toString();
	}

	@GetMapping(value = "/getDcConfigDetails")
	public String getDCConfigDetails(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId) {
		if (stateId.equalsIgnoreCase("null")) {
			stateId = null;
		}
		if (districtId.equalsIgnoreCase("null")) {
			districtId = null;
		}
		return hospitalUserSaveService.getDCConfigDetails(userId, stateId, districtId).toString();
	}

	@GetMapping(value = "/gethostlogdata")
	public Map<String, Object> gethostlogdata(@RequestParam(required = false, value = "hospoitalid") Integer hospid) {
		Map<String, Object> details = new HashMap<>();
		try {
			details.put("hospitallog", logService.getgethostlogdata(hospid));
			details.put("incentivelog", logService.getincentivelogdata(hospid));
			details.put("status", 200);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("hospitallog", "[]");
			details.put("incentivelog", "[]");
			details.put("status", 400);
		}
		return details;
	}

}
