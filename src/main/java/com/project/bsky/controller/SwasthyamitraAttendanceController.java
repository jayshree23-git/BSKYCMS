/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CPDConfigurationBean;
import com.project.bsky.bean.Hospital;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.Smattendancereportbean;
import com.project.bsky.bean.SwasthyaMitraBean;
import com.project.bsky.bean.SwasthyaMitraGeoTagBean;
import com.project.bsky.service.SwasthyamitraAttendanceService;

/**
 * @author rajendra.sahoo
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class SwasthyamitraAttendanceController {

	@Autowired
	private SwasthyamitraAttendanceService smserv;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getsmattendancereport")
	@ResponseBody
	public List<Smattendancereportbean> getsmattendancereport(
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month) {
		List<Smattendancereportbean> list = new ArrayList<Smattendancereportbean>();
		try {

			list = smserv.getsmattendancereport(year, month);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@PostMapping(value = "/saveswasthyamitra")
	@ResponseBody
	public Response saveswasthyamitra(@RequestBody CPDConfigurationBean cpdConfigurationBean) {
		Response response = new Response();
		try {
			Long userid = Long.valueOf(cpdConfigurationBean.getCpdId());
			List<Hospital> hospital = cpdConfigurationBean.getHospitalCode();
			Integer created = cpdConfigurationBean.getCreatedBy();
			response = smserv.saveswasthyamitra(userid, hospital, created);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen! Please Try Later");
			response.setStatus("400");
		}
		return response;
	}

	@PostMapping(value = "/updateswasthyamitra")
	@ResponseBody
	public Response updateswasthyamitra(@RequestBody CPDConfigurationBean cpdConfigurationBean) {
		Response response = new Response();
		try {
			Long userid = Long.valueOf(cpdConfigurationBean.getCpdId());
			List<Hospital> hospital = cpdConfigurationBean.getHospitalCode();
			Integer updated = cpdConfigurationBean.getCreatedBy();
			Integer status = cpdConfigurationBean.getStatus();

			response = smserv.updateswasthyamitra(userid, hospital, updated, status);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen! Please Try Later");
			response.setStatus("400");
		}
		return response;
	}

	@GetMapping(value = "/getsmmappinglist")
	@ResponseBody
	public List<Object> getsmmappinglist() {
		List<Object> list = new ArrayList<Object>();
		try {
			list = smserv.getsmmappinglist();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/getsmtaggedhospital")
	@ResponseBody
	public List<Object> getsmtaggedhospital(@RequestParam(value = "userid", required = false) Long userid) {
		List<Object> list = new ArrayList<Object>();
		try {
			list = smserv.getsmtaggedhospital(userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/inactiveSwasthyaMitra")
	@ResponseBody
	public Response inactiveSwasthyaMitra(@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "bskyid", required = false) Long bskyid,
			@RequestParam(value = "statusflag", required = false) Integer statusflag) {
		Response response = new Response();
		try {
			response = smserv.inactiveSwasthyaMitra(userid, bskyid, statusflag);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Some thing Went Wrong");
		}
		return response;
	}

	@GetMapping(value = "/getswasthyaMappingDetail")
	public List<Object> getDistinctCPD(@RequestParam(required = false, value = "userId") Integer userId) {
		List<Object> list = new ArrayList<Object>();
		try {
			list = smserv.getDistinctSwasthyaMitra(userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
		return list;
	}

	@GetMapping(value = "/getswasthyaMitraMasterData")
	public List<SwasthyaMitraBean> getswasthyaMitraDetail(
			@RequestParam(required = false, value = "groupId") Integer groupId,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId) {
		List<SwasthyaMitraBean> list = new ArrayList<SwasthyaMitraBean>();
		try {
			list = smserv.getswasthyaMitraDetails(groupId, stateId, districtId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/getswasthyaMitraFilterData")
	public List<SwasthyaMitraBean> getswasthyaMitrFilter(
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId) {
		List<SwasthyaMitraBean> list = new ArrayList<SwasthyaMitraBean>();
		try {
			list = smserv.getswasthyaMitraFilter(stateId, districtId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/getsmlistbyhospital")
	@ResponseBody
	public List<Object> getsmlistbyhospital(@RequestParam(value = "hosp", required = false) String hospital) {
		List<Object> list = new ArrayList<Object>();
		try {
			list = smserv.getsmlistbyhospital(hospital);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/getsmlistforregistaration")
	public List<Object> getsmlistforregistaration(@RequestParam(required = false, value = "state") String state,
			@RequestParam(required = false, value = "dist") String dist,
			@RequestParam(required = false, value = "hosp") String hospital,
			@RequestParam(required = false, value = "smid") String smid) {
		List<Object> list = new ArrayList<Object>();
		try {
			list = smserv.getsmlistforregistaration(state, dist, hospital, smid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/getsmdetailsforregister")
	@ResponseBody
	public Map<String, Object> getsmdetailsforregister(@RequestParam(value = "smid", required = false) Long smid) {
		Map<String, Object> details = new HashMap<>();
		try {
			details = smserv.getsmdetailsforregister(smid);
			List<Object> hosplist = smserv.getsmtaggedhospital(smid);
			details.put("hospital", hosplist);
			List<Object> logdetails = smserv.getsmlogdetails(smid);
			details.put("logdetails", logdetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("error", e);
		}
		return details;
	}

	@GetMapping(value = "/allowsmforregister")
	@ResponseBody
	public Response allowsmforregister(@RequestParam(value = "smid", required = false) Integer smid,
			@RequestParam(value = "updateby", required = false) Integer updateby) {
		Response response = new Response();
		try {
			response = smserv.allowsmforregister(smid, updateby);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen! Please Try Later");
			response.setStatus("400");
		}
		return response;
	}

	@GetMapping(value = "/getapprovesmlistforregistaration")
	public List<Object> getapprovesmlistforregistaration(@RequestParam(required = false, value = "state") String state,
			@RequestParam(required = false, value = "dist") String dist,
			@RequestParam(required = false, value = "hosp") String hospital,
			@RequestParam(required = false, value = "smid") String smid) {
		List<Object> list = new ArrayList<>();
		try {
			list = smserv.getapprovesmlistforregistaration(state, dist, hospital, smid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@PostMapping(value = "/swasthyamitrageotagupdate")
	public Map<String, Object> swasthyaMitraGeoTagUpdate(@RequestBody SwasthyaMitraGeoTagBean geoTagBean) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = smserv.swasthyaMitraGeoTagUpdate(geoTagBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
