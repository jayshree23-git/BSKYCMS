/**
 * 
 */
package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.KeySelector.Purpose;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.HospitalDoctorprofile;
import com.project.bsky.bean.PrimaryLinkBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.UserManualService;

/**
 * Cretedby:-Hrusikesh Mohnaty {@link Purpose}
 * :-Usermanula,Doctorprofile,Highlight Claims for multiple beneficiaries
 * treated by the same doctor at multiple Locations during the same period
 */
@RestController
@RequestMapping(value = "/api")
public class UserManualuploadController {

	private final Logger logger;

	@Autowired
	public UserManualuploadController(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private UserManualService userManualService;

	@ResponseBody
	@GetMapping("/getlistGrouptype")
	public List<Object> getListGroupType() {
		List<Object> list = null;
		try {
			list = userManualService.getlistof();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getPrimarylinkname")
	public List<PrimaryLinkBean> getprimarylinkname(@RequestParam("grouptype") Integer grouptype) {
		List<PrimaryLinkBean> primarylistlist = null;
		try {
			primarylistlist = userManualService.getprimarylinkname(grouptype);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return primarylistlist;
	}

	@ResponseBody
	@PostMapping(value = "/getusermanuallreportsave", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Response> saveUsermanuallreport(
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "selectedFile", required = false) MultipartFile selectedFile,
			@RequestParam(value = "primarylinkid", required = false) Long primarylinkid,
			@RequestParam(value = "primarylinname", required = false) String primarylinname,
			@RequestParam(value = "grouptype", required = false) Long grouptype,
			@RequestParam(value = "grouptypename", required = false) String grouptypename,
			@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "actionflag", required = false) Long actionflag,
			@RequestParam(value = "user_manual_id", required = false) Long user_manual_id) {
		Response response = new Response();
		try {
			response = userManualService.saveUsermanuallreport(description, selectedFile, primarylinkid, primarylinname,
					grouptype, grouptypename, userid, actionflag, user_manual_id);

		} catch (Exception e) {
			response.setStatus("Failed");
			logger.error("Exception occured in saveUsermanuallreport method of saveUsermanuallreport" + e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@GetMapping("/getallviewlist")
	public List<Object> getallviewlist(@RequestParam(value = "userid", required = false) Long userid) {
		List<Object> list = null;
		try {
			list = userManualService.getallviewlist(userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/usermanuladoc")
	public void commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		Long type = json.getLong("t");
		try {
			userManualService.downLoadFile(fileName, type, response);
		} catch (Exception e) {
			logger.error("Exception Occurred in userManualService Method of userManualService : " + e.getMessage());
		}
	}

	@ResponseBody
	@GetMapping("/getlinklistdata")
	public List<Object> getlinklistdata(@RequestParam(value = "userid", required = false) Long userid) {
		List<Object> link = null;
		try {
			link = userManualService.getlinklistdata(userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return link;
	}

	@ResponseBody
	@GetMapping("/getSearchdata")
	public List<Object> getSearchdata(@RequestParam(value = "primarylinkid", required = false) Long primarylinkid) {
		List<Object> primarylink = null;
		try {
			primarylink = userManualService.getSearchdata(primarylinkid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return primarylink;
	}

	@ResponseBody
	@GetMapping("/gethospitaldatabystate")
	public List<Object> gethospitaldatabystate(
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode) {
		List<Object> data = null;
		try {
			data = userManualService.gethospitaldatabystate(hospitalcode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return data;
	}

	@ResponseBody
	@GetMapping("/getSpecialitydetails")
	public List<Object> getSpecialitydetails(
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode) {
		List<Object> speciality = null;
		try {
			speciality = userManualService.getSpecialitydetails(hospitalcode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return speciality;
	}

	@PostMapping(value = "/getSubmitdata")
	public ResponseEntity<Response> getSubmitdata(@RequestBody HospitalDoctorprofile resbean) {
		Response response = null;
		try {
			response = userManualService.getSubmitdata(resbean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@GetMapping("/getdetailshospitaldoctorprifile")
	public List<Object> getdetailshospitaldoctorprifile(
			@RequestParam(value = "statecode", required = false) String statecode,
			@RequestParam(value = "districtcode", required = false) String districtcode,
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "userid", required = false) Long userid) {
		List<Object> getdetials = null;
		try {
			getdetials = userManualService.getdetailshospitaldoctorprifile(statecode, districtcode, hospitalcode,
					hospitalCode, userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getdetials;
	}

	@ResponseBody
	@GetMapping("/gethospitladoctordetailsprofile")
	public List<Object> gethospitladoctordetailsprofile(
			@RequestParam(value = "profilid", required = false) Long profilid) {
		List<Object> detailsreport = null;
		try {
			detailsreport = userManualService.gethospitladoctordetailsprofile(profilid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return detailsreport;
	}

	@ResponseBody
	@GetMapping("/getEditDoctorprofiledata")
	public List<Object> getEditDoctorprofiledata(@RequestParam(value = "profilid", required = false) Long profilid) {
		List<Object> detailsreport = null;
		try {
			detailsreport = userManualService.getEditDoctorprofiledata(profilid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return detailsreport;
	}

	@GetMapping(value = "/getclaimmultipledoctortreated")
	@ResponseBody
	private String getclaimmultipledoctortreated() {
		String claimist = null;
		try {
			claimist = userManualService.getclaimmultipledoctortreatedlist();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimist;
	}

	@ResponseBody
	@GetMapping("/getDoctorTaggedHospital")
	public List<Object> getDoctorTaggedHospital(@RequestParam(value = "regno", required = false) String regno) {
		List<Object> taggedlist = null;
		try {
			taggedlist = userManualService.getDoctorTaggedHospitalName(regno);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return taggedlist;
	}

	@ResponseBody
	@GetMapping("/getDoctortreatedhospital")
	public List<Object> getDoctortreatedhospital(
			@RequestParam(value = "regnonumber", required = false) String regnonumber) {
		List<Object> treatedlist = null;
		try {
			treatedlist = userManualService.getDoctortreatedhospitalDetails(regnonumber);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return treatedlist;
	}
}
