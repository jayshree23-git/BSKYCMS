/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.MskGrivanceBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.Grivancetype;

/**
 * @author rajendra.sahoo
 *
 */
public interface MskGrivanceService {

	List<Grivancetype> getactivegrivancetype();

	Response savemskgriv(MskGrivanceBean mskgrivbean,
			MultipartFile docfile1, MultipartFile vdofile1, 
			MultipartFile docfile2, MultipartFile vdofile2, 
			MultipartFile docfile3, MultipartFile vdofile3);

	List<Object> mskrecordview(String statecode, String distcode, String hospcode);

	List<Object> docdetailsbyserviceid(Long serviceid);

	void downgrivdoc(HttpServletResponse response, String fileName);
	
	JSONObject sendOtpForGrv(String mobile);
	String validateotpgrievance(Long mobileNo, Integer otp) throws JSONException;

}
