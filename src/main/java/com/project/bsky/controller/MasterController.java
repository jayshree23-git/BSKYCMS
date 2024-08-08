/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.GroupMappingBean;
import com.project.bsky.bean.PrimaryLinkBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.UserMappingBean;
import com.project.bsky.model.User;
import com.project.bsky.model.UserDetails;
import com.project.bsky.service.AdminConsoleService;
import com.project.bsky.service.MasterService;
import com.project.bsky.util.ClassHelperUtils;
import com.project.bsky.util.EncryptionUtils;

/**
 * @author ronauk
 *
 */
@RestController
@RequestMapping(value = "/master")
public class MasterController {
	private final Logger logger;

	@Autowired
	public MasterController(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private MasterService service;

	@Autowired
	private AdminConsoleService adminService;

	@GetMapping("/getStateMasterDetails")
	public String getStateMasterDetails() {
		return service.getStateMasterDetails();
	}

	@GetMapping("/getSNODetails")
	public String getSNODetails() {
		return service.getSNODetails();
	}

	@GetMapping("/getSNAList")
	public String getSNAList() {
		return service.getSNAList();
	}

	@GetMapping("/getCPDDetails")
	public String getCPDDetails() {
		return service.getCPDDetails();
	}

	@GetMapping("/getDCDetails")
	public String getDCDetails() {
		return service.getDCDetails();
	}

	@GetMapping("/getUserDetails")
	public String getUserDetails() {
		return service.getUserDetails();
	}

	@GetMapping("/getMonths")
	public String getMonths() {
		return service.getMonths();
	}

	@GetMapping("/getYears")
	public String getYears() {
		return service.getYears();
	}

	@GetMapping("/getHospitalCategoryList")
	public String getHospitalCategoryList() {
		return service.getHospCatMaster();
	}

	@GetMapping("/getGlobalLinks")
	public String getGlobalLinks(@RequestParam("userId") String userId) {
		int id;
		try {
			if (userId == null || userId == "" || userId == "null" || userId.equalsIgnoreCase("")
					|| userId.equalsIgnoreCase("null")) {
				userId = "0";
			}
			id = Integer.parseInt(userId);
		} catch (NumberFormatException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			id = 0;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			id = 0;
			e.printStackTrace();
		}
		return adminService.getGlobalLinks(id).toString();
	}

	@GetMapping("/getPrimaryLinks")
	public String getPrimaryLinks(@RequestParam("userId") String userId) {
		int id;
		try {
			if (userId == null || userId == "" || userId == "null" || userId.equalsIgnoreCase("")
					|| userId.equalsIgnoreCase("null")) {
				userId = "0";
			}
			id = Integer.parseInt(userId);
		} catch (NumberFormatException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			id = 0;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			id = 0;
		}
		return adminService.getPrimaryLinksFromGlobalLink(id).toString();
	}

	@ResponseBody
	@PostMapping("/setPrimaryLinks")
	public ResponseEntity<Response> setPrimaryLinks(@RequestBody UserMappingBean bean, Response response) {
		try {
			List<PrimaryLinkBean> list = bean.getPrimaryLinks();
			response = adminService.setPrimaryLinks(bean.getUserId(), bean.getCreatedby(), list);
		} catch (Exception e) {
			response.setMessage("Some Error Happened");
			response.setStatus("Failed");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@PostMapping("/setPrimaryLinksForGroup")
	public ResponseEntity<Response> setPrimaryLinksForGroup(@RequestBody GroupMappingBean bean, Response response) {
		List<PrimaryLinkBean> list = bean.getPrimaryLinks();
		response = adminService.setPrimaryLinksForGroup(bean.getGroupId(), bean.getCreatedby(), list);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/getDistrictDetailsByStateId")
	public String getDistrictDetailsByStateId(@RequestParam("stateCode") String stateCode) {
		String districtMaster = null;
		try {
			districtMaster = service.getDistrictDetailsByStateId(stateCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return districtMaster;
	}

	@GetMapping("/getDistrictDetailsByNFSA")
	public String getDistrictDetailsByNFSA() {
		String districtMaster = null;
		try {
			districtMaster = service.getDistrictDetailsByNFSAData();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return districtMaster;
	}

	@PostMapping("/getMappedAuthDetails")
	public ResponseEntity<?> getMappedAuthDetails(@RequestBody Map<String, Object> response) {
		Map<String, Object> responseList = new HashedMap();
		try {
			responseList = service.getMappedAuthDetails(response);
		} catch (Exception e) {
			e.printStackTrace();
			responseList.put("status", 400);
			responseList.put("message", e.getMessage());
		}
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}

	@GetMapping("/getDistrictDetailsByStateAndDistrictCode")
	public String getDistrictDetailsByStateAndDistrictCode(@RequestParam("stateCode") String stateCode,
			@RequestParam("districtCode") String districtCode) {
		String districtMaster = null;
		try {
			districtMaster = service.getDistrictDetailsByStateAndDistrictCode(stateCode, districtCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return districtMaster;
	}

	@GetMapping("/getHospitalByDistrictId")
	public String getHospitalByDistrictId(@RequestParam("districtCode") String districtCode,
			@RequestParam("stateCode") String stateCode) {
		String hospitalInformation = null;
		try {
			hospitalInformation = service.getHospitalByDistrictId(districtCode, stateCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalInformation;
	}

	@GetMapping("/getblockByDistrictId")
	public String getblockByDistrictId(@RequestParam("districtCode") String districtCode,
			@RequestParam("stateCode") String stateCode) {
		String hospitalInformation = null;
		try {
			hospitalInformation = service.getblockByDistrictId(districtCode, stateCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalInformation;
	}

	@GetMapping("/getAllGroups")
	public ResponseEntity<Map<String, Object>> getAllGroups() {
		try {
			return ResponseEntity
					.ok(ClassHelperUtils.createSuccessEncryptedMap(service.getGroups(), "Data Retrived Successfully"));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return ResponseEntity.ok(ClassHelperUtils.createErrorEncryptedMap(e.getMessage()));
		}
	}

	@GetMapping("/getSNAExecutiveDetails")
	public String getSNAExecutiveDetails() {
		return service.getSNAEXDetails();
	}

	@GetMapping("/getCDMODetails")
	public String getCDMODetails() {
		return service.getCDMODetails();
	}

	@ResponseBody
	@GetMapping(value = "/getDClist")
	public List<User> getDClist() {
		List<User> list = null;
		try {
			list = service.getDClist();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;

	}

	@GetMapping("/getLockedUserList")
	public List<UserDetails> getLockedUserList() {
		return adminService.getLockedUserList();
	}

	@GetMapping("/unlockUserByUserId")
	public ResponseEntity<?> unlockUserByUserId(@RequestParam(value = "userId") int userId) {
		return ResponseEntity.ok(adminService.unlockUserByUserId(userId));
	}

	@ResponseBody
	@PostMapping(value = "/getDistrictdetailsformulticheckbox")
	public ResponseEntity<?> getDistrictListfoemultipledropdown(
			@RequestParam(value = "Statecode") List<String> Statecode) {
		Map<String, Object> details = new LinkedHashMap<String, Object>();
		try {
			details.put("status", "success");
			details.put("data", service.getDistrictByStateCode(Statecode));
		} catch (Exception e) {
			logger.error("Exception Occurred in getDistrictListfoemultipledropdown Method of MasterController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@GetMapping(value = "/getHospitaldetailsformulticheckbox")
	public ResponseEntity<?> getHospitaldetailsformulticheckbox(
			@RequestParam(value = "Statecodeforhospitallist") List<String> Statecodeforhospitallist,
			@RequestParam(value = "Districtcode") List<String> Districtcode) {
		Map<String, Object> details = new LinkedHashMap<String, Object>();
		try {
			details.put("status", "success");
			details.put("data", service.getHospitalByDistrictCode(Statecodeforhospitallist, Districtcode));
		} catch (Exception e) {
			logger.error("Exception Occurred in getHospitaldetailsformulticheckbox Method of MasterController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@GetMapping(value = "/getdcwiseHospitaldetailsformulticheckbox")
	public ResponseEntity<?> getdcwiseHospitaldetailsformulticheckbox(
			@RequestParam(value = "Statecodeforhospitallist") List<String> Statecodeforhospitallist,
			@RequestParam(value = "Districtcode") List<String> Districtcode,
			@RequestParam(value = "userid") Long userid) {
		Map<String, Object> details = new LinkedHashMap<String, Object>();
		try {
			details.put("status", "success");
			details.put("data",
					service.getdcwiseHospitaldetailsformulticheckbox(Statecodeforhospitallist, Districtcode, userid));
		} catch (Exception e) {
			logger.error("Exception Occurred in getdcwiseHospitaldetailsformulticheckbox Method of MasterController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@GetMapping(value = "/getDischargeandclaimdetailsinnerpage")
	public ResponseEntity<?> getDischargeandclaimdetailsinnerpage(@RequestParam(value = "userid") Long userid,
			@RequestParam(value = "month") String month, @RequestParam(value = "years") String years,
			@RequestParam(value = "searcby") Integer searcby, @RequestParam(value = "satedata") List<String> satedata,
			@RequestParam(value = "districtdata") List<String> districtdata,
			@RequestParam(value = "hospitaldata") List<String> hospitaldata) {
		// MainController.");
		Map<String, Object> details = new LinkedHashMap<String, Object>();
		try {
			Map<String, Object> summarymapsMap = service.getDischargeandClaimsummarydetailsinnerpage(userid, month,
					years, searcby, satedata, districtdata, hospitaldata);
			details.put("status", "success");
			details.put("data", summarymapsMap);
		} catch (Exception e) {
			logger.error("Exception Occurred in getDischargeandclaimdetailsinnerpage Method of MasterController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@GetMapping(value = "/getDClistForCDMO")
	public List<Object> getDClistForCDMO(@RequestParam(value = "userId") Long userId) {
		List<Object> list = null;
		try {
			list = service.getDClistCDMO(userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;

	}

	@ResponseBody
	@GetMapping(value = "/getDClistByStateAndDist")
	public List<Object> getDClistByStateAndDist(@RequestParam(value = "stateId", required = false) String stateId,
			@RequestParam(value = "distId", required = false) String distId) {
		List<Object> list = null;
		try {
			list = service.getDClistByStateAndDist(stateId, distId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;

	}

	@ResponseBody
	@PostMapping(value = "/getHospitaldetailsFromDCUser")
	public ResponseEntity<?> getHospitaldetailsformulticheckbox(
			@RequestParam(value = "dcUserIdList") List<Integer> dcUserIdList) {
		Map<String, Object> details = new LinkedHashMap<String, Object>();
		try {
			details.put("status", "success");
			details.put("data", service.getHospitalByDCUserId(dcUserIdList));
		} catch (Exception e) {
			logger.error("Exception Occurred in getHospitaldetailsformulticheckbox Method of MasterController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/getSchemeDetails")
	public ResponseEntity<Map<String, Object>> getScheme(@RequestBody Map<String, Object> request) {
		try {
			List<Map<String, Object>> data = service
					.getSchemeDetails(EncryptionUtils.getDecryptedData((String) request.get("request")));
			if (data != null)
				return ResponseEntity.ok(
						ClassHelperUtils.createSuccessEncryptedMap(data, "Scheme Information Fetched Successfully."));
			else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("No Data Found"));

		} catch (Exception ex) {
			logger.error("Exception Occurred in getScheme Method of PackageBlockingController : {}", ex.getMessage());
			return ResponseEntity.ok(ClassHelperUtils.createErrorEncryptedMap(ex.getMessage()));
		}
	}

	@ResponseBody
	@GetMapping(value = "/getHospitalPackageSchemeWiseDataForPackageName")
	public List<Object> getHospitalPackageSchemeWiseDataForPackageName(
			@RequestParam(value = "schemeId") String schemeId,
			@RequestParam(value = "schemeCategoryId") String schemeCategoryId) {
		List<Object> list = null;
		try {
			list = service.getHospitalPackageSchemeWiseDataForPackageNameData(schemeId, schemeCategoryId);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getHospitalPackageSchemeWiseDataForPackageName Method of MasterController : "
							+ e.getMessage());
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getHospitalPackageSchemeWiseDataForPackageHeadercode")
	public List<Object> getHospitalPackageSchemeWiseDataForPackageHeadercode(
			@RequestParam(value = "schemeId") String schemeId,
			@RequestParam(value = "schemeCategoryId") String schemeCategoryId,
			@RequestParam(value = "acronym") String acronym) {
		List<Object> list = null;
		try {
			list = service.getHospitalPackageHeadercode(schemeId, schemeCategoryId, acronym);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getHospitalPackageSchemeWiseDataForPackageHeadercode Method of MasterController : "
							+ e.getMessage());
		}
		return list;
	}

}
