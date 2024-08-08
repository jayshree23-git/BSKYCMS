/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.PhotoUploadService;
import com.project.bsky.util.ProfileUtil;

/**
 * @author priyanka.singh
 *
 */
@Service
public class PhotoUploadServiceImpl implements PhotoUploadService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private Logger logger;

	@Value("${file.path.ProfilePhoto}")
	private String file;

	@Override
	public Response saveProfilePhto(UserDetails userDetails, MultipartFile photo) {
		Response response = new Response();
		try {
			UserDetails user = userDetailsRepository.findById(userDetails.getUserId()).get();
			if (photo != null) {
				String s = userDetails.getUserId().toString();
				Map<String, String> filePath = ProfileUtil.createFileforprofile(photo, s);
				//System.out.println(filePath);
				for (Map.Entry<String, String> entry : filePath.entrySet()) {
//					if (file.contains(entry.getKey())) {
//					//System.out.println(entry.getValue());
					user.setProfilePhoto(entry.getValue());
//					}
				}
			} else {
				response.setMessage("Some Error Happen");
				response.setStatus("Failed");
				return response;
			}
			// //System.out.println("comming");
			// //System.out.println(user);
			userDetailsRepository.save(user);
			response.setMessage("Profile photo Added Successfully");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen");
			response.setStatus("Failed");
		}
		return response;
	}
}
