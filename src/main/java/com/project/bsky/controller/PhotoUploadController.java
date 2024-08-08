/**
 * 
 */
package com.project.bsky.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UserDetails;
import com.project.bsky.service.PhotoUploadService;
import com.project.bsky.service.UserDetailsService;

/**
 * @author priyanka.singh
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class PhotoUploadController {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PhotoUploadService photoUploadService;
	
	@Autowired
	private Logger logger;
	
	@ResponseBody
	@PostMapping(value = "/saveProfileImage")
	public Response addProfilePhoto(UserDetails userDetails,
			@RequestParam(required = false, value = "profilePhoto1") MultipartFile photo) {
		////System.out.println(photo+ "---------"+userDetails+" ");
		Response response = null;
		try {
			response = photoUploadService.saveProfilePhto(userDetails, photo);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
}
