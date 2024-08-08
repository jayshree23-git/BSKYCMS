/**
 * 
 */
package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Dccdmomappingbean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.AllowUserForHospitalVisit;
import com.project.bsky.model.MobileConfigurationmst;
import com.project.bsky.model.MobileUserConfiguration;
import com.project.bsky.service.DcCdmoMappingSevice;

/**
 * Rajendra
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class DcCdmoMappingController {
	
	@Autowired
	private DcCdmoMappingSevice dccdmoserv;

	@GetMapping(value = "/getuserDetailsbygroup")
	@ResponseBody
	public Map<String,Object> getuserDetailsbygroup(@RequestParam(value = "groupid", required = false) Integer groupid) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",dccdmoserv.getuserDetailsbygroup(groupid));
			map.put("status",HttpStatus.OK.value());
			map.put("message","Success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status",HttpStatus.BAD_REQUEST.value());
			map.put("error",e.getMessage());
			map.put("message","Error");
		}
		return map;
	}
	
	@PostMapping(value = "/saveDcCdmomapping")
	@ResponseBody
	public Response saveDcCdmomapping(@RequestBody Dccdmomappingbean bean) {
		Response map=new Response();
		try {
			map=dccdmoserv.saveDcCdmomapping(bean);
		} catch (Exception e) {
			e.printStackTrace();
			map.setStatus("400");
			map.setMessage(e.getMessage());
		}
		return map;
	}
	
	@GetMapping(value = "/getdccdmomaplist")
	@ResponseBody
	public Map<String,Object> getdccdmomaplist(@RequestParam(value = "dcId", required = false) Long dcId,
			@RequestParam(value = "group", required = false) Integer group) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",dccdmoserv.getdccdmomaplist(dcId,group));
			map.put("status",HttpStatus.OK.value());
			map.put("message","Success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status",HttpStatus.BAD_REQUEST.value());
			map.put("error",e.getMessage());
			map.put("message","Error");
		}
		return map;
	}
	
	@GetMapping(value = "/getmapingbydcid")
	@ResponseBody
	public Map<String,Object> getmapingbydcid(@RequestParam(value = "dcId", required = false) Long dcId) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",dccdmoserv.getmapingbydcid(dcId));
			map.put("status",HttpStatus.OK.value());
			map.put("message","Success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status",HttpStatus.BAD_REQUEST.value());
			map.put("error",e.getMessage());
			map.put("message","Error");
		}
		return map;
	}
	
	@PostMapping(value = "/updateDcCdmomapping")
	@ResponseBody
	public Response updateDcCdmomapping(@RequestBody Dccdmomappingbean bean) {
		Response map=new Response();
		try {
			map=dccdmoserv.updateDcCdmomapping(bean);
		} catch (Exception e) {
			e.printStackTrace();
			map.setStatus("400");
			map.setMessage(e.getMessage());
		}
		return map;
	}
	
	@GetMapping(value = "/getdccdmomapcount")
	@ResponseBody
	public Map<String,Object> getdccdmomapcount(@RequestParam(value = "dcId", required = false) Long dcId,
			@RequestParam(value = "group", required = false) Integer group) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",dccdmoserv.getdccdmomapcount(dcId,group));
			map.put("status",HttpStatus.OK.value());
			map.put("message","Success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status",HttpStatus.BAD_REQUEST.value());
			map.put("error",e.getMessage());
			map.put("message","Error");
		}
		return map;
	}
	
	@GetMapping(value = "/taggedlogdetails")
	@ResponseBody
	public Map<String,Object> taggedlogdetails(@RequestParam(value = "dcId", required = false) Long dcId) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",dccdmoserv.taggedlogdetails(dcId));
			map.put("status",HttpStatus.OK.value());
			map.put("message","Success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status",HttpStatus.BAD_REQUEST.value());
			map.put("error",e.getMessage());
			map.put("message","Error");
		}
		return map;
	}
	
	@PostMapping(value = "/saveDcHospitalmapping")
	@ResponseBody
	public Response saveDcHospitalmapping(@RequestBody Dccdmomappingbean bean) {
		Response map=new Response();
		try {
			map=dccdmoserv.saveDcHospitalmapping(bean);
		} catch (Exception e) {
			e.printStackTrace();
			map.setStatus("400");
			map.setMessage(e.getMessage());
		}
		return map;
	}
	
	@GetMapping(value = "/getdcgovthospmapcount")
	@ResponseBody
	public Map<String,Object> getdcgovthospmapcount(@RequestParam(value = "dcId", required = false) Long dcId,
			@RequestParam(value = "group", required = false) Integer group) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",dccdmoserv.getdcgovthospmapcount(dcId,group));
			map.put("status",HttpStatus.OK.value());
			map.put("message","Success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status",HttpStatus.BAD_REQUEST.value());
			map.put("error",e.getMessage());
			map.put("message","Error");
		}
		return map;
	}
	
	@GetMapping(value = "/getgovthospbydcid")
	@ResponseBody
	public Map<String,Object> getgovthospbydcid(@RequestParam(value = "dcId", required = false) Long dcId,
			@RequestParam(value = "group", required = false) Integer group) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",dccdmoserv.getgovthospbydcid(dcId,group));
			map.put("status",HttpStatus.OK.value());
			map.put("message","Success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status",HttpStatus.BAD_REQUEST.value());
			map.put("error",e.getMessage());
			map.put("message","Error");
		}
		return map;
	}
	
	@GetMapping(value = "/taggedHOSDClogdetails")
	@ResponseBody
	public Map<String,Object> taggedHOSDClogdetails(@RequestParam(value = "dcId", required = false) Long dcId) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",dccdmoserv.taggedHOSDClogdetails(dcId));
			map.put("status",HttpStatus.OK.value());
			map.put("message","Success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status",HttpStatus.BAD_REQUEST.value());
			map.put("error",e.getMessage());
			map.put("message","Error");
		}
		return map;
	}
	
	@PostMapping(value = "/updateDcHospitalmapping")
	@ResponseBody
	public Response updateDcHospitalmapping(@RequestBody Dccdmomappingbean bean) {
		Response map=new Response();
		try {
			map=dccdmoserv.updateDcHospitalmapping(bean);
		} catch (Exception e) {
			e.printStackTrace();
			map.setStatus("400");
			map.setMessage(e.getMessage());
		}
		return map;
	}
	
	@GetMapping(value = "/getdcfacelist")
	public Map<String,Object> getdcfacelist(@RequestParam(value = "dcUserId", required = false) Long dcId,
			@RequestParam(value = "group", required = false) Integer group) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",dccdmoserv.getdcfacelist(dcId,group));
			map.put("status",HttpStatus.OK.value());
			map.put("message","Success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status",HttpStatus.BAD_REQUEST.value());
			map.put("error",e.getMessage());
			map.put("message","Error");
		}
		return map;
	}
	
	@GetMapping(value = "/removefacedataofdc")
	public Map<String,Object> removefacedataofdc(@RequestParam(value = "faceid", required = false) Long faceid,
			@RequestParam(value = "userid", required = false) Long userid) {
		Map<String,Object> map=new HashMap<>();
		try {
			map=dccdmoserv.updatedcfacelist(faceid,userid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status",HttpStatus.BAD_REQUEST.value());
			map.put("error",e.getMessage());
			map.put("message","Error");
		}
		return map;
	}
	
	@ResponseBody
	@GetMapping(value = "/getdctaggeddetails")
	public Map<String, Object> getdctaggeddetails(@RequestParam(value = "dcUserId", required = false) Long dcid) {
		Map<String, Object> details = new HashMap<>();
		try {
			details=dccdmoserv.getdctaggeddetails(dcid);
			details.put("status", HttpStatus.OK.value());
			details.put("message", "Api called Succesfully");
		} catch (Exception e) {
			details.put("status", HttpStatus.BAD_REQUEST.value());
			details.put("message", e.getMessage());
		}
		return details;
	}
	
	@ResponseBody
	@GetMapping(value = "/getfacelogdetails")
	public Map<String, Object> getfacelogdetails(@RequestParam(value = "dcUserId", required = false) Long dcid,
			@RequestParam(value = "group", required = false) Integer group) {
		Map<String, Object> details = new HashMap<>();
		try {
			details.put("data",dccdmoserv.getfacelogdetails(dcid,group));
			details.put("status", HttpStatus.OK.value());
			details.put("message", "Api called Succesfully");
		} catch (Exception e) {
			details.put("status", HttpStatus.BAD_REQUEST.value());
			details.put("message", e.getMessage());
		}
		return details;
	}
	
	@ResponseBody
	@GetMapping(value = "/allowhospitalmobileactivitylist")
	public Map<String, Object> allowhospitalmobileactivitylist() {
		Map<String, Object> details = new HashMap<>();
		try {
			details.put("data",dccdmoserv.allowhospitalmobileactivitylist());
			details.put("status", HttpStatus.OK.value());
			details.put("message", "Api called Succesfully");
		} catch (Exception e) {
			details.put("status", HttpStatus.BAD_REQUEST.value());
			details.put("message", e.getMessage());
		}
		return details;
	}
	
	@ResponseBody
	@PostMapping(value = "/allowhospitalmobileactivity")
	public Map<String, Object> allowhospitalmobileactivity(@RequestBody Map<String,List<AllowUserForHospitalVisit>> map) {
		Map<String, Object> details = new HashMap<>();
		try {
			List<AllowUserForHospitalVisit> list=map.get("allowlist");
			details=dccdmoserv.allowhospitalmobileactivitylist(list);
		} catch (Exception e) {
			details.put("status", HttpStatus.BAD_REQUEST.value());
			details.put("message", e.getMessage());
		}
		return details;
	}
	
	@ResponseBody
	@PostMapping(value = "/savegroupmobilemast")
	public Map<String, Object> savegroupmobilemast(@RequestBody Map<String,List<MobileConfigurationmst>> map) {
		Map<String, Object> details = new HashMap<>();
		try {
			List<MobileConfigurationmst> list=map.get("selectedlist");
			details=dccdmoserv.savegroupmobilemast(list);
		} catch (Exception e) {
			details.put("status", HttpStatus.BAD_REQUEST.value());
			details.put("message", e.getMessage());
		}
		return details;
	}
	
	@ResponseBody
	@GetMapping(value = "/getconfigGroupList")
	public Map<String, Object> getconfigGroupList() {
		Map<String, Object> details = new HashMap<>();
		try {
			details.put("data", dccdmoserv.getconfigGroupList());
			details.put("status", HttpStatus.OK.value());
			details.put("message", "Success");
		} catch (Exception e) {
			details.put("status", HttpStatus.BAD_REQUEST.value());
			details.put("message", e.getMessage());
		}
		return details;
	}
	
	@ResponseBody
	@GetMapping(value = "/getconfiggroupdata")
	public Map<String, Object> getconfiggroupdata(@RequestParam(value = "userId", required = false) Long userId) {
		Map<String, Object> details = new HashMap<>();
		try {
			details.put("data", dccdmoserv.getconfiggroupdata(userId));
			details.put("status", HttpStatus.OK.value());
			details.put("message", "Success");
		} catch (Exception e) {
			details.put("status", HttpStatus.BAD_REQUEST.value());
			details.put("message", e.getMessage());
		}
		return details;
	}
	
	@ResponseBody
	@GetMapping(value = "/getconfiggroupalldata")
	public Map<String, Object> getconfiggroupalldata() {
		Map<String, Object> details = new HashMap<>();
		try {
			details.put("data", dccdmoserv.getconfiggroupalldata());
			details.put("status", HttpStatus.OK.value());
			details.put("message", "Success");
		} catch (Exception e) {
			details.put("status", HttpStatus.BAD_REQUEST.value());
			details.put("message", e.getMessage());
		}
		return details;
	}
	
	@ResponseBody
	@PostMapping(value = "/saveusermobileconfig")
	public Map<String, Object> saveusermobileconfig(@RequestBody Map<String,List<MobileUserConfiguration>> map) {
		Map<String, Object> details = new HashMap<>();
		try {
			List<MobileUserConfiguration> list=map.get("selectedlist");
			details=dccdmoserv.saveusermobileconfig(list);
		} catch (Exception e) {
			details.put("status", HttpStatus.BAD_REQUEST.value());
			details.put("message", e.getMessage());
		}
		return details;
	}
	
	@ResponseBody
	@PostMapping(value = "/savegroupwisemobileconfig")
	public Map<String, Object> savegroupwisemobileconfig(@RequestBody Map<String,List<MobileUserConfiguration>> map) {
		Map<String, Object> details = new HashMap<>();
		try {
			List<MobileUserConfiguration> list=map.get("selectedlist");
			details=dccdmoserv.savegroupwisemobileconfig(list);
		} catch (Exception e) {
			details.put("status", HttpStatus.BAD_REQUEST.value());
			details.put("message", e.getMessage());
		}
		return details;
	}
	
	@ResponseBody
	@GetMapping(value = "/getusermobileconfiglist")
	public Map<String, Object> getusermobileconfiglist(@RequestParam(value = "userId", required = false) Long userId) {
		Map<String, Object> details = new HashMap<>();
		try {
			details.put("data", dccdmoserv.getusermobileconfiglist(userId));
			details.put("status", HttpStatus.OK.value());
			details.put("message", "Success");
		} catch (Exception e) {
			details.put("status", HttpStatus.BAD_REQUEST.value());
			details.put("message", e.getMessage());
		}
		return details;
	}
}
