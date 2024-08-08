/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.json.JSONException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.MskGrivanceBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.Grivancetype;
import com.project.bsky.service.MskGrivanceService;

/**
 * @author rajendra.sahoo
 *
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class MskGrivanceController {
	
	@Autowired
	private MskGrivanceService mskgrivserv;
	
	@Autowired
	private Logger logger;
	
	@GetMapping(value = "/getactivegrivancetype")
    @ResponseBody
    public List<Grivancetype> getactivegrivancetype() {
        return mskgrivserv.getactivegrivancetype();
    }
	
	@PostMapping(value = "/savemskgriv")
    @ResponseBody
    public Response savemskgriv(@Valid MskGrivanceBean mskgrivbean, BindingResult br,
    		@RequestParam(required = false, value = "docfile1") MultipartFile docfile1,
    		@RequestParam(required = false, value = "vdofile1") MultipartFile vdofile1,
    		@RequestParam(required = false, value = "docfile2") MultipartFile docfile2,
    		@RequestParam(required = false, value = "vdofile2") MultipartFile vdofile2,
    		@RequestParam(required = false, value = "docfile3") MultipartFile docfile3,
    		@RequestParam(required = false, value = "vdofile3") MultipartFile vdofile3) {
		Response rsponse=new Response();
		if(!br.hasErrors()) {  
			try {
				rsponse=mskgrivserv.savemskgriv( mskgrivbean,docfile1,vdofile1,docfile2,vdofile2,docfile3,vdofile3);
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				rsponse.setStatus("400");
				rsponse.setMessage("Some Error Happen");
			}
        }else{ 
			rsponse.setStatus("400");
			rsponse.setMessage(br.getFieldError().getDefaultMessage());
        }
        return rsponse;
    }
	
	@GetMapping(value = "/mskrecordview")
    @ResponseBody
    public List<Object> mskrecordview(@RequestParam(required = false, value = "statecode") String statecode,
    		@RequestParam(required = false, value = "distcode") String distcode,
    		@RequestParam(required = false, value = "hospcode") String hospcode) {
		List<Object> list=new ArrayList<>();
			try {
				list=mskgrivserv.mskrecordview( statecode,distcode,hospcode);
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
        return list;
    }
	@GetMapping(value = "/docdetailsbyserviceid")
    @ResponseBody
    public List<Object> docdetailsbyserviceid(@RequestParam(required = false, value = "serviceid") Long serviceid) {
		List<Object> list=new ArrayList<>();
			try {
				list=mskgrivserv.docdetailsbyserviceid(serviceid);
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
        return list;
    }
	
	@ResponseBody
	@GetMapping(value = "/downlordmosarkargrivancedoc")
	public String commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		try {
			if (fileName == null || "".equals(fileName) || fileName.equalsIgnoreCase("")) {
				resp = "Passbook not found";
			} else {
				mskgrivserv.downgrivdoc(response,fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	@GetMapping(value = "/generateOtpForGrievance")
    @ResponseBody
    public ResponseEntity<String> generateOtpForGrievance(@RequestParam(required = false, value = "mobile") String mobile) {
		JSONObject response = new JSONObject();
		try {
			response = mskgrivserv.sendOtpForGrv(mobile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(response.toString());
    }
	@GetMapping(value = "/validateotpgrievance")
	@ResponseBody
	public String validateotpforgrievance(@RequestParam(value = "otp", required = false) Integer otp,
			@RequestParam(value = "mobileNo", required = false) Long mobileNo){
		String response=null;
		try {
			response=mskgrivserv.validateotpgrievance(mobileNo,otp);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
